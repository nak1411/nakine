package com.nak.core.rendering;

import com.nak.core.EngineManager;
import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import com.nak.core.entities.SceneManager;
import com.nak.core.io.KeyInput;
import com.nak.core.io.MouseInput;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.entities.Entity;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Framebuffer;
import com.nak.core.textures.PickingTexture;
import com.nak.core.util.Constants;
import com.nak.core.util.Utils;
import com.nak.test.Launcher;
import com.nak.test.TestGame;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.sql.SQLOutput;
import java.util.*;

public class RenderEngine {

    private final WindowManager window;
    ShaderManager shader;
    private EntityRenderer entityRenderer;
    private static Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private Camera camera;

    private static boolean isCulling = false;
    private static boolean isWireframe = false;
    private static boolean depthVisualizer = false;
    private static boolean pickingVisualizer = false;
    private int clickedObject;
    private int totalEntities = 0;
    private int totalVertices = 0;
    private static int numLights;


    public RenderEngine() throws Exception {
        window = Launcher.getWindow();
        shader = new ShaderManager();
        entityRenderer = new EntityRenderer();
        framebuffer = new Framebuffer(WindowManager.getWidth(), WindowManager.getHeight());
        pickingTexture = new PickingTexture(WindowManager.getWidth(), WindowManager.getHeight());
        GL30.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
    }



    public void init() throws Exception {
        entityRenderer.init();

        shader.createShaders();

        shader.useNormalShader();
        shader.createUniform("depthVisualizer", shader.getShaderProgram());
        shader.createUniform("transformationMatrix", shader.getShaderProgram());
        shader.createUniform("projectionMatrix", shader.getShaderProgram());
        shader.createUniform("viewMatrix", shader.getShaderProgram());
        shader.createUniform("textureSampler", shader.getShaderProgram());
        shader.createUniform("ambientLight", shader.getShaderProgram());
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower", shader.getShaderProgram());
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightListUniform("pointLights", 5);
        shader.createSpotLightListUniform("spotLights", 5);

        shader.useOutlineShader();
        shader.createUniform("transformationMatrixOutline", shader.getShaderOutlineProgram());
        shader.createUniform("projectionMatrixOutline", shader.getShaderOutlineProgram());
        shader.createUniform("viewMatrixOutline", shader.getShaderOutlineProgram());
        shader.createUniform("outlineScale", shader.getShaderOutlineProgram());
        shader.createUniform("outlineColor", shader.getShaderOutlineProgram());

        shader.usePickingShader();
        shader.createUniform("transformationMatrixPicking", shader.getShaderPickingProgram());
        shader.createUniform("projectionMatrixPicking", shader.getShaderPickingProgram());
        shader.createUniform("viewMatrixPicking", shader.getShaderPickingProgram());
        shader.createUniform("pickingColor", shader.getShaderPickingProgram());
    }

    public void render(Camera camera, SceneManager scene) {
        this.camera = camera;

        framebuffer.bind();
        //clear();

        // Resize buffers/viewport
        if (WindowManager.isResize()) {
            framebuffer.resizeFrameBuffer(WindowManager.getWidth(), WindowManager.getHeight());
            pickingTexture.resizePickingTexture(WindowManager.getWidth(), WindowManager.getHeight());
            GL30.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
        }
        WindowManager.setResize(false);

        // RENDER PICKING PASS
        if (RenderEngine.isPickingVisualizer())
            renderPicking(camera, scene);

        if (MouseInput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            renderPicking(camera, scene);
            MouseInput.get().setMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT, false);
        }
        //RENDER NORMAL PASS
        renderNormal(clickedObject,camera, scene);

        // RENDER OUTLINE PASS
        renderOutlines(clickedObject, camera, scene);

        framebuffer.unbind();
    }

    public void renderNormal(int clickedObject,Camera camera, SceneManager scene) {
        clear();
        GL30.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL30.glStencilMask(0xFF);
        entityRenderer.render(clickedObject, shader, camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
    }

    public void renderOutlines(int clickedObject, Camera camera, SceneManager scene) {
        GL30.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL30.glStencilMask(0x00);
        GL30.glDisable(GL11.GL_DEPTH_TEST);
        entityRenderer.renderOutline(clickedObject, shader, camera);
        GL30.glStencilMask(0xFF);
        GL30.glStencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
        GL30.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void renderPicking(Camera camera, SceneManager scene) {
        GL30.glDisable(GL11.GL_STENCIL_TEST);
        pickingTexture.enableWriting();
        GL30.glClearColor(0, 0, 0, 1);
        GL30.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        entityRenderer.renderPicking(shader, camera);

        clickedObject = -1;

        int x = (int) MouseInput.getScreenX();
        int y = (int) MouseInput.getScreenY();
        if (pickingTexture.readPixel(x, y) != 0) {
            clickedObject = pickingTexture.readPixel(x, y);
        }
        pickingTexture.disableWriting();
        GL30.glEnable(GL30.GL_STENCIL_TEST);
    }

    public static void enableCulling() {
        if (!isCulling) {
            GL30.glEnable(GL11.GL_CULL_FACE);
            GL30.glCullFace(GL11.GL_BACK);
            isCulling = true;
        }
    }

    public static void disableCulling() {
        if (isCulling) {
            GL30.glDisable(GL11.GL_CULL_FACE);
            isCulling = false;
        }
    }

    public void clear() {
        WindowManager.setClearColor();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
    }

    public void imgui() {
        ImGui.begin("Scene Debug");

        ImGui.text("Entities:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(totalEntities));

        ImGui.text("Outline Width");
        ImFloat floatStep = Constants.OUTLINE_SCALE;
        ImGui.inputFloat("Outline Scale", floatStep, 0.01f, 0.1f, "%.2f", ImGuiInputTextFlags.EnterReturnsTrue);
        Constants.OUTLINE_SCALE.set(floatStep);

        ImGui.text("Total Vertices:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(totalVertices * totalEntities));

        ImGui.text("Camera Position:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        if (camera != null)
            ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, "X: " + String.format("%.1f", Camera.getPosition().x) + " Y: " + String.format("%.1f", Camera.getPosition().y) + " Z: " + String.format("%.1f", Camera.getPosition().z));

        ImGui.text("Camera Rotation:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        if (camera != null)
            ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, "X: " + String.format("%.1f", camera.getRotation().x) + " Y: " + String.format("%.1f", camera.getRotation().y) + " Z: " + String.format("%.1f", camera.getRotation().z));

        ImGui.text("Mouse position:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, "X: " + String.format("%.1f", MouseInput.getScreenX()) + " Y: " + String.format("%.1f", MouseInput.getScreenY()));

        ImGui.text("Camera Increment:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, "X: " + String.format("%.1f", KeyInput.getCameraInc().x) + " Y: " + String.format("%.1f", KeyInput.getCameraInc().y) + " Z: " + String.format("%.1f", KeyInput.getCameraInc().z));

        ImGui.text("Mouse Pressed:");
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(MouseInput.getCurrentButton()));

        ImGui.text("Key Pressed:");
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(KeyInput.getCurrentKey()));

        ImGui.separator();

        float[] pickerColor = {Constants.CLEAR_COLOR.x, Constants.CLEAR_COLOR.y, Constants.CLEAR_COLOR.z, Constants.CLEAR_COLOR.w};
        float w = (ImGui.getContentRegionAvail().x - ImGui.getStyle().getItemSpacing().y) * 0.40f;
        ImGui.setNextItemWidth(w);
        ImGui.colorPicker4("Clear Color", pickerColor);
        Constants.CLEAR_COLOR.set(pickerColor[0], pickerColor[1], pickerColor[2], pickerColor[3]);
        ImGui.end();
        entityRenderer.imgui();
    }


    public void cleanup() {
        entityRenderer.cleanup();
    }

    public void processEntities(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null) {
            entityList.add(entity);
            totalEntities = entityList.size();
            totalVertices = entity.getModel().getVertexCount();
        } else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }

        shader.useNormalShader();
        if (!depthVisualizer)
            shader.setUniform("material", entity.getModel().getMaterial());
        if (!RenderEngine.isDepthVisualizer())
            shader.setUniform("textureSampler", 0);
    }

    public void processOutlines(Entity entity) {
        List<Entity> outlineList = entityRenderer.getOutlines().get(entity.getModel());
        if (outlineList != null) {
            outlineList.add(entity);
        }
        else {
            List<Entity> newOutlineList = new ArrayList<>();
            newOutlineList.add(entity);
            entityRenderer.getOutlines().put(entity.getModel(), newOutlineList);
        }
    }

    public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight
            directionalLight, ShaderManager shader) {
        if (!depthVisualizer) {
            shader.setUniform("ambientLight", Constants.AMBIENT_COLOR);
            shader.setUniform("specularPower", Constants.SPECULAR_POWER);
        }


        numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            if (!depthVisualizer)
                shader.setUniform("spotLights", spotLights[i], i);
        }

        numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            if (!depthVisualizer)
                shader.setUniform("pointLights", pointLights[i], i);
        }
        if (!depthVisualizer)
            shader.setUniform("directionalLight", directionalLight);
    }

    public static boolean isDepthVisualizer() {
        return depthVisualizer;
    }

    public static boolean isPickingVisualizer() {
        return pickingVisualizer;
    }

    public static void setDepthVisualizer(boolean depthVisualizer) {
        RenderEngine.depthVisualizer = depthVisualizer;
    }

    public static void setPickingVisualizer(boolean pickingVisualizer) {
        RenderEngine.pickingVisualizer = pickingVisualizer;
    }

    public static boolean isWireframe() {
        return isWireframe;
    }

    public static void setIsWireframe(boolean isWireframe) {
        RenderEngine.isWireframe = isWireframe;
    }

    public static Framebuffer getFramebuffer() {
        return framebuffer;
    }
}


