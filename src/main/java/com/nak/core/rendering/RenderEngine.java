package com.nak.core.rendering;

import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import com.nak.core.entities.Entity;
import com.nak.core.entities.SceneManager;
import com.nak.core.io.KeyInput;
import com.nak.core.io.MouseInput;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Framebuffer;
import com.nak.core.terrain.Block;
import com.nak.core.textures.PickingTexture;
import com.nak.core.util.Constants;
import com.nak.test.Launcher;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class RenderEngine {

    private final WindowManager window;
    ShaderManager shader;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private static Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private Camera camera;

    private static boolean isCulling = false;
    private static boolean isWireframe = false;
    private static boolean depthVisualizer = false;
    private static boolean pickingVisualizer = false;
    private static boolean enableFog = true;
    private int clickedObject;
    int clicked = 0;
    private int totalVertices = 0;
    private static int numLights;


    public RenderEngine() throws Exception {
        window = Launcher.getWindow();
        shader = new ShaderManager();
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        framebuffer = new Framebuffer(WindowManager.getWidth(), WindowManager.getHeight());
        pickingTexture = new PickingTexture(WindowManager.getWidth(), WindowManager.getHeight());
        GL30.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
    }


    public void init() throws Exception {
        entityRenderer.init();
        terrainRenderer.init();

        shader.createShaders();
        createEntityShader();
        createTerrainShader();
        createOutlineShader();
        createPickingShader();
    }

    private void createEntityShader() throws Exception {
        shader.useEntityShader();
        shader.createUniform("depthVisualizer", shader.getEntityShaderProgram());
        shader.createUniform("transformationMatrixEntity", shader.getEntityShaderProgram());
        shader.createUniform("projectionMatrixEntity", shader.getEntityShaderProgram());
        shader.createUniform("viewMatrixEntity", shader.getEntityShaderProgram());
        shader.createUniform("textureSampler", shader.getEntityShaderProgram());
        shader.createUniform("ambientLight", shader.getEntityShaderProgram());
        shader.createMaterialUniform("material", shader.getEntityShaderProgram());
        shader.createUniform("specularPower", shader.getEntityShaderProgram());
        shader.createDirectionalLightUniform("directionalLight", shader.getEntityShaderProgram());
        shader.createPointLightListUniform("pointLights", 5, shader.getEntityShaderProgram());
        shader.createSpotLightListUniform("spotLights", 5, shader.getEntityShaderProgram());
    }

    private void createTerrainShader() throws Exception {
        shader.useTerrainShader();
        shader.createUniform("depthVisualizer", shader.getTerrainShaderProgram());
        shader.createUniform("transformationMatrixTerrain", shader.getTerrainShaderProgram());
        shader.createUniform("projectionMatrixTerrain", shader.getTerrainShaderProgram());
        shader.createUniform("viewMatrixTerrain", shader.getTerrainShaderProgram());
        shader.createUniform("textureSampler", shader.getTerrainShaderProgram());
        shader.createUniform("ambientLight", shader.getTerrainShaderProgram());
        shader.createMaterialUniform("material", shader.getTerrainShaderProgram());
        shader.createUniform("specularPower", shader.getTerrainShaderProgram());
        shader.createDirectionalLightUniform("directionalLight", shader.getTerrainShaderProgram());
        shader.createPointLightListUniform("pointLights", 5, shader.getTerrainShaderProgram());
        shader.createSpotLightListUniform("spotLights", 5, shader.getTerrainShaderProgram());
        shader.createUniform("skyColor", shader.getTerrainShaderProgram());
        shader.createUniform("enableFog", shader.getTerrainShaderProgram());
        shader.createUniform("fogDensity", shader.getTerrainShaderProgram());
        shader.createUniform("fogGradient", shader.getTerrainShaderProgram());
    }

    private void createOutlineShader() throws Exception {
        shader.useOutlineShader();
        shader.createUniform("transformationMatrix", shader.getOutlineShaderProgram());
        shader.createUniform("projectionMatrix", shader.getOutlineShaderProgram());
        shader.createUniform("viewMatrix", shader.getOutlineShaderProgram());
        shader.createUniform("outlineScale", shader.getOutlineShaderProgram());
        shader.createUniform("outlineColor", shader.getOutlineShaderProgram());
    }

    private void createPickingShader() throws Exception {
        shader.usePickingShader();
        shader.createUniform("transformationMatrix", shader.getShaderPickingProgram());
        shader.createUniform("projectionMatrix", shader.getShaderPickingProgram());
        shader.createUniform("viewMatrix", shader.getShaderPickingProgram());
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
        renderNormal(clickedObject, camera, scene);

        // RENDER OUTLINE PASS
        renderOutlines(clickedObject, camera, scene);

        framebuffer.unbind();
    }

    public void renderNormal(int clickedObject, Camera camera, SceneManager scene) {
        clear();
        GL30.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL30.glStencilMask(0xFF);
        shader.useEntityShader();
        entityRenderer.render(clickedObject, shader, camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
        shader.useTerrainShader();
        shader.setUniform("skyColor", Constants.CLEAR_COLOR);
        shader.setUniform("fogDensity", Constants.FOG_DENSITY);
        shader.setUniform("fogGradient", Constants.FOG_GRADIENT);
        terrainRenderer.render(clickedObject, shader, camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
    }

    public void renderOutlines(int clickedObject, Camera camera, SceneManager scene) {
        shader.useOutlineShader();
        GL30.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL30.glStencilMask(0x00);
        GL30.glDisable(GL11.GL_DEPTH_TEST);
        entityRenderer.renderOutline(clickedObject, shader, camera);
        GL30.glStencilMask(0xFF);
        GL30.glStencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
        GL30.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void renderPicking(Camera camera, SceneManager scene) {
        shader.usePickingShader();
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
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(entityRenderer.getEntities().size()));

        ImGui.text("Chunks:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(terrainRenderer.getBlocks().size()));

        ImGui.text("Outline Width");
        ImFloat floatStep = Constants.OUTLINE_SCALE;
        ImGui.inputFloat("Outline Scale", floatStep, 0.01f, 0.1f, "%.2f", ImGuiInputTextFlags.EnterReturnsTrue);
        Constants.OUTLINE_SCALE.set(floatStep);

        ImGui.text("Total Vertices:");
        ImGui.sameLine(ImGui.getWindowWidth() - 175);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf((terrainRenderer.getTotalVertices() + entityRenderer.getTotalVertices())));

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


        if (ImGui.button("Toggle Fog"))
            enableFog = !enableFog;

        ImFloat fogDensityFloatStep = Constants.FOG_DENSITY;
        ImGui.inputFloat("Fog Density", fogDensityFloatStep, 0.001f, 0.01f, "%.3f", ImGuiInputTextFlags.EnterReturnsTrue);
        Constants.FOG_DENSITY.set(fogDensityFloatStep);

        ImFloat fogGradientFloatStep = Constants.FOG_GRADIENT;
        ImGui.inputFloat("Fog Gradient", fogGradientFloatStep, 0.1f, 1.0f, "%.2f", ImGuiInputTextFlags.EnterReturnsTrue);
        Constants.FOG_GRADIENT.set(fogGradientFloatStep);


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
        terrainRenderer.cleanup();
    }

    public void processEntities(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null) {
            entityList.add(entity);
        } else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }

        shader.useEntityShader();
        if (!depthVisualizer)
            shader.setUniform("material", entity.getModel().getMaterial());
        if (!RenderEngine.isDepthVisualizer())
            shader.setUniform("textureSampler", 0);
    }

    public void processChunks(Block block) {
        List<Block> blockList = terrainRenderer.getBlocks().get(block.getModel());
        if (blockList != null) {
            blockList.add(block);
        } else {
            List<Block> newBlockList = new ArrayList<>();
            newBlockList.add(block);
            terrainRenderer.getBlocks().put(block.getModel(), newBlockList);
        }

        shader.useTerrainShader();


        if (!depthVisualizer)
            shader.setUniform("material", block.getModel().getMaterial());
        if (!RenderEngine.isDepthVisualizer())
            shader.setUniform("textureSampler", 0);
        if (!enableFog)
            shader.setUniform("enableFog", 0);
        else
            shader.setUniform("enableFog", 1);
    }

    public void processOutlines(Entity entity) {
        List<Entity> outlineList = entityRenderer.getOutlines().get(entity.getModel());
        if (outlineList != null) {
            outlineList.add(entity);
        } else {
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


