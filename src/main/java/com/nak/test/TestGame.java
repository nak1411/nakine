package com.nak.test;

import com.nak.core.*;
import com.nak.core.entities.Camera;
import com.nak.core.entities.Entity;
import com.nak.core.entities.SceneManager;
import com.nak.core.gui.ImGuiLayer;
import com.nak.core.io.KeyInput;
import com.nak.core.io.MouseInput;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Loader;
import com.nak.core.opengl.Model;
import com.nak.core.opengl.ModelLoader;
import com.nak.core.rendering.RenderEngine;
import com.nak.core.terrain.Chunk;
import com.nak.core.textures.Texture;
import com.nak.core.util.Constants;
import com.nak.core.util.MousePicker;
import imgui.ImGui;
import imgui.type.ImFloat;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.Random;

public class TestGame implements Logic {

    private float color = 0.0f;

    private final RenderEngine renderer;
    private final WindowManager window;
    private final SceneManager sceneManager;
    private final KeyInput keyInput;
    private final Loader loader;
    private ImGuiLayer imGuiLayer;

    private Chunk chunk;

    private Model lightWidget;

    private Camera camera;
    Vector3f cameraInc;
    float increment;
    private Vector2f displVec;
    private Vector2f rotVec;

    public TestGame() throws Exception {
        renderer = new RenderEngine();
        window = Launcher.getWindow();
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        cameraInc = new Vector3f(0, 0, 0);
        sceneManager = new SceneManager(80);
        keyInput = new KeyInput(camera);
        loader = new Loader();
        displVec = new Vector2f();
        rotVec = new Vector2f();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        chunk = new Chunk(sceneManager, loader);
        new Thread(() -> {
            while (!GLFW.glfwWindowShouldClose(window.getWindow())) {
                chunk.renderThreadOne();
            }
        }).start();

        new Thread(() -> {
            while (!GLFW.glfwWindowShouldClose(window.getWindow())) {
                chunk.renderThreadTwo();
            }
        }).start();

        lightWidget = ModelLoader.loadModel(loader, "/models/TestCube.obj");
        lightWidget.getMaterial().setDisableCulling(false);
        lightWidget.setTexture(new Texture(loader.loadTexture("textures/missingtexture.png")), 1.0f);

        //Lighting
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(0, 10, -5);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);

        Vector3f coneDir = new Vector3f(0, 0, 1);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0, 0, 1f), lightIntensity, 0, 0, 1), coneDir, cutoff);

        sceneManager.setDirectionalLight(new DirectionalLight(lightPosition, lightColor, lightIntensity));
        sceneManager.setPointLights(new PointLight[]{pointLight});
        sceneManager.setSpotLights(new SpotLight[]{spotLight});

        ImFloat cameraWidgetScale = new ImFloat(0.2f);
        sceneManager.addEntity(new Entity(5000, lightWidget, new Vector3f(lightPosition.x, lightPosition.y, lightPosition.z), new Vector3f(0, 0, 0), cameraWidgetScale));

        this.imGuiLayer = new ImGuiLayer(window.getWindow());
        this.imGuiLayer.initImGui();
    }

    @Override
    public void input() {
        if (MouseInput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            updateCameraRotation();
            camera.moveRotation(rotVec.x * Constants.MOUSE_MOVE_SPEED, rotVec.y * Constants.MOUSE_MOVE_SPEED, 0);
        }
        keyInput.update();
    }

    private void updateCameraRotation() {
        rotVec = displVec;
        displVec.x = 0;
        displVec.y = 0;
        if (MouseInput.getLastX() > 0 && MouseInput.getLastY() > 0) {
            double x = MouseInput.getX() - MouseInput.getLastX();
            double y = MouseInput.getY() - MouseInput.getLastY();
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;

            if (rotateX)
                displVec.y = (float) x;
            if (rotateY)
                displVec.x = (float) y;
        }
        MouseInput.setLastX(MouseInput.getX());
        MouseInput.setLastY(MouseInput.getY());
    }


    public void update(float frameTime) {
        // Lighting Updates
        float factor = 1 - (Math.abs(sceneManager.getLightAngle()) - 80) / 10.0f;
        sceneManager.getDirectionalLight().setIntensity(factor);
        sceneManager.getDirectionalLight().getColor().y = Math.max(factor, 0.9f);
        sceneManager.getDirectionalLight().getColor().z = Math.max(factor, 0.5f);

//        double angRad = Math.toRadians(sceneManager.getLightAngle());
//        sceneManager.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);s
//        sceneManager.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);

        for (int i = 0; i < sceneManager.getEntities().size(); i++) {
            int distX = (int) (Camera.getPosition().x - sceneManager.getEntities().get(i).getPos().x);
            int distZ = (int) (Camera.getPosition().z - sceneManager.getEntities().get(i).getPos().z);

            if (distX < 0)
                distX = -distX;
            if (distZ < 0)
                distZ = -distZ;

            if ((distX <= Constants.NUM_ENTITIES.get()) && (distZ <= Constants.NUM_ENTITIES.get())) {
                renderer.processEntities(sceneManager.getEntities().get(i));
            }

            if (sceneManager.getEntities().get(i).getModel() == lightWidget)
                sceneManager.getEntities().get(i).setPos(new Vector3f(sceneManager.getDirectionalLight().getDirection().x, sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z));
        }

        for (int i = 0; i < sceneManager.getOutlines().size(); i++) {
            int distX = (int) (Camera.getPosition().x - sceneManager.getEntities().get(i).getPos().x);
            int distZ = (int) (Camera.getPosition().z - sceneManager.getEntities().get(i).getPos().z);

            if (distX < 0)
                distX = -distX;
            if (distZ < 0)
                distZ = -distZ;

            if ((distX <= Constants.NUM_ENTITIES.get()) && (distZ <= Constants.NUM_ENTITIES.get())) {
                sceneManager.getOutlines().get(i).setRotation(new Vector3f(increment, increment, increment));
                renderer.processOutlines(sceneManager.getOutlines().get(i));
            }
        }
        imGuiLayer.update(frameTime, renderer);
    }

    @Override
    public void render() {
        renderer.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}