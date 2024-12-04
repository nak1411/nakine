package com.nak.core.rendering;

import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import com.nak.core.entities.Entity;
import com.nak.core.io.KeyInput;
import com.nak.core.io.MouseInput;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Model;
import com.nak.core.opengl.ModelLoader;
import com.nak.core.util.Constants;
import com.nak.core.util.Utils;
import com.nak.test.Launcher;
import imgui.ImGui;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements Renderer {

    private Map<Model, List<Entity>> entities;
    private Map<Model, List<Entity>> outlines;

    private Entity selectedEntity = null;

    public EntityRenderer() throws Exception {
        entities = new HashMap<>();
        outlines = new HashMap<>();
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void render(int clickedObject, ShaderManager shader, Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
        RenderEngine.renderLights(pointLights, spotLights, directionalLight, shader);

        for (Model model : entities.keySet()) {
            bind(model);
            List<Entity> entityList = entities.get(model);
            for (Entity entity : entityList) {
                if (entity.getUid() == clickedObject) {
                    selectedEntity = entity;
                    // Movement
                    KeyInput.setEntityInc();
                    entity.incPos(KeyInput.entityInc);
                    entity.setPos(selectedEntity.getPos());
                    entity.setScale(selectedEntity.getScale());
                }
                prepareNormalTransform(shader, entity, camera);
                int renderMode = GL11.GL_TRIANGLES;
                if (RenderEngine.isWireframe())
                    renderMode = GL11.GL_LINE_LOOP;
                else
                    renderMode = GL11.GL_TRIANGLES;
                GL30.glDrawElements(renderMode, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            if (clickedObject == -1){
                selectedEntity = null;
            }
            unbind();
        }
        entities.clear();
    }

    public void renderPicking(ShaderManager shader, Camera camera) {
        for (Model model : entities.keySet()) {
            bind(model);
            List<Entity> entityList = entities.get(model);
            for (Entity entity : entityList) {
                int r = (entity.getUid() & 0x000000FF);
                int g = (entity.getUid() & 0x0000FF00);
                int b = (entity.getUid() & 0x00FF0000);
                preparePickingTransform(shader, entity, camera);
                shader.setUniform("pickingColor", new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f));
                GL30.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();

        }
        //entities.clear();
    }

    public void renderOutline(int clickedObject, ShaderManager shader, Camera camera) {
        for (Model model : outlines.keySet()) {
            bind(model);
            List<Entity> outlineList = outlines.get(model);
            for (Entity outline : outlineList) {

                if (outline.getUid() == clickedObject) {
                    // Movement
                    KeyInput.setEntityInc();
                    outline.incPos(KeyInput.entityInc);
                    outline.setPos(selectedEntity.getPos());
                    outline.setScale(selectedEntity.getScale());

                    prepareOutlineTransform(shader, outline, camera);
                    shader.setUniform("outlineScale", Constants.OUTLINE_SCALE);
                    shader.setUniform("outlineColor", new Vector4f(Constants.OUTLINE_COLOR));
                    GL30.glDrawElements(GL11.GL_TRIANGLES, outline.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                }
                if (clickedObject == -1){
                    selectedEntity = null;
                }
            }
            unbind();
        }
        outlines.clear();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        //GL30.glEnableVertexAttribArray(2);

        if (model.getMaterial().isDisableCulling())
            RenderEngine.disableCulling();
        else
            RenderEngine.enableCulling();

        // Render textures
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        //GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void preparePickingTransform(ShaderManager shader, Object entity, Camera camera) {
        shader.usePickingShader();
        shader.setUniform("projectionMatrixPicking", Launcher.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrixPicking", Utils.getViewMatrix(camera));
        shader.setUniform("transformationMatrixPicking", Utils.createTransformationMatrix((Entity) entity));
    }

    public void prepareNormalTransform(ShaderManager shader, Object entity, Camera camera) {
        shader.useNormalShader();
        shader.setUniform("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrix", Utils.getViewMatrix(camera));
        shader.setUniform("depthVisualizer", RenderEngine.isDepthVisualizer() ? 1 : 0);
        shader.setUniform("transformationMatrix", Utils.createTransformationMatrix((Entity) entity));
    }

    public void prepareOutlineTransform(ShaderManager shader, Object entity, Camera camera) {
        shader.useOutlineShader();
        shader.setUniform("projectionMatrixOutline", Launcher.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrixOutline", Utils.getViewMatrix(camera));
        shader.setUniform("transformationMatrixOutline", Utils.createTransformationMatrix((Entity) entity));
    }

    public void imgui() {
        ImGui.begin("Entity Debug");
        if (selectedEntity != null)
            selectedEntity.imgui();
        ImGui.end();
    }

    @Override
    public void cleanup() {
        entities.clear();
    }

    public Map<Model, List<Entity>> getEntities() {
        return entities;
    }

    public Map<Model, List<Entity>> getOutlines() {
        return outlines;
    }
}
