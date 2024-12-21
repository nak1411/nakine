package com.nak.core.rendering;

import com.nak.core.entities.Camera;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Model;
import com.nak.core.terrain.Block;
import com.nak.core.terrain.BlockModel;
import com.nak.core.terrain.ChunkMesh;
import com.nak.core.util.Utils;
import com.nak.test.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerrainRenderer implements Renderer {

    private Map<Model, List<Block>> blocks;

    private Block selectedBlock = null;
    private int totalVertices = 0;

    public TerrainRenderer() throws Exception {
        blocks = new HashMap<>();
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void render(int clickedObject, ShaderManager shader, Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
        RenderEngine.renderLights(pointLights, spotLights, directionalLight, shader);

        for (Model model : blocks.keySet()) {
            bind(model);
            List<Block> blockList = blocks.get(model);
            for (Block block : blockList) {
                totalVertices = ChunkMesh.getVertices().size();
//                if (block.getUid() == clickedObject) {
//                    selectedEntity = block;
//                }
                prepareNormalTransform(shader, block, camera);
                int renderMode;
                if (RenderEngine.isWireframe())
                    renderMode = GL11.GL_LINE_LOOP;
                else
                    renderMode = GL11.GL_TRIANGLES;
               GL30.glDrawArrays(renderMode, 0, block.getModel().getVertexCount());
            }
//            if (clickedObject == -1) {
//                selectedBlock = null;
//            }
            unbind();
        }
        blocks.clear();
    }

    public void renderPicking(ShaderManager shader, Camera camera) {
        for (Model model : blocks.keySet()) {
            bind(model);
            List<Block> blockList = blocks.get(model);
            for (Block block : blockList) {
//                int r = (block.getUid() & 0x000000FF);
//                int g = (block.getUid() & 0x0000FF00);
//                int b = (block.getUid() & 0x00FF0000);
                preparePickingTransform(shader, block, camera);
                //shader.setUniform("pickingColor", new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f));
                GL30.glDrawElements(GL11.GL_TRIANGLES, BlockModel.vertices.length, GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();

        }
        //entities.clear();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

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
        GL30.glBindVertexArray(0);
    }

    public void preparePickingTransform(ShaderManager shader, Object block, Camera camera) {
        shader.usePickingShader();
        shader.setUniform("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrix", Utils.getViewMatrix(camera));
        shader.setUniform("transformationMatrix", Utils.createTransformationMatrix((Block) block));
    }

    public void prepareNormalTransform(ShaderManager shader, Object block, Camera camera) {
        shader.useTerrainShader();
        shader.setUniform("projectionMatrixTerrain", Launcher.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrixTerrain", Utils.getViewMatrix(camera));
        shader.setUniform("depthVisualizer", RenderEngine.isDepthVisualizer() ? 1 : 0);
        shader.setUniform("transformationMatrixTerrain", Utils.createTransformationMatrix((Block) block));
    }

    public void imgui() {
//        ImGui.begin("Block Debug");
//        if (selectedBlockModel != null)
//            selectedBlockModel.imgui();
//        ImGui.end();
    }

    @Override
    public void cleanup() {
        blocks.clear();
    }

    public Map<Model, List<Block>> getBlocks() {
        return blocks;
    }

    public int getTotalVertices() {
        return totalVertices;
    }
}
