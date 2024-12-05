package com.nak.core.rendering;

import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.textures.Material;
import com.nak.core.util.FileUtils;
import imgui.type.ImFloat;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShaderManager {

    private int entityVertexShader, entityFragmentShader, outlineVertexShader, outlineFragmentShader, pickingVertexShader,
            pickingFragmentShader, terrainVertexShader, terrainFragmentShader;
    private int entityShaderProgram, outlineShaderProgram, shaderPickingProgram, terrainShaderProgram;

    private final Map<String, Integer> uniforms;

    public ShaderManager() {
        uniforms = new HashMap<>();
    }

    public void createShaders() {

        /// *********ENTITY****************************************************** ///
        // Create vertex shader
        entityVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(entityVertexShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[0]);
        GL30.glCompileShader(entityVertexShader);
        checkShaderErrors(entityVertexShader, "VERTEX");
        // Create fragment shader
        entityFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(entityFragmentShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[1]);
        GL30.glCompileShader(entityFragmentShader);
        checkShaderErrors(entityFragmentShader, "FRAGMENT");

        /// *********OUTLINE****************************************************** ///
        // Create vertex outline shader
        outlineVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(outlineVertexShader, FileUtils.loadShaderFile("/shaders/outline_shader.shader")[0]);
        GL30.glCompileShader(outlineVertexShader);
        checkShaderErrors(outlineVertexShader, "VERTEX");
        // Create fragment outline shader
        outlineFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(outlineFragmentShader, FileUtils.loadShaderFile("/shaders/outline_shader.shader")[1]);
        GL30.glCompileShader(outlineFragmentShader);
        checkShaderErrors(outlineFragmentShader, "FRAGMENT");

        /// *********TERRAIN****************************************************** ///
        // Create terrain vertex shader
        terrainVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(terrainVertexShader, FileUtils.loadShaderFile("/shaders/terrain_shader.shader")[0]);
        GL30.glCompileShader(terrainVertexShader);
        checkShaderErrors(terrainVertexShader, "VERTEX");
        // Create terrain fragment shader
        terrainFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(terrainFragmentShader, FileUtils.loadShaderFile("/shaders/terrain_shader.shader")[1]);
        GL30.glCompileShader(terrainFragmentShader);
        checkShaderErrors(terrainFragmentShader, "FRAGMENT");

        /// *********PICKING****************************************************** ///
        // Create vertex picking shader
        pickingVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(pickingVertexShader, FileUtils.loadShaderFile("/shaders/picking_shader.shader")[0]);
        GL30.glCompileShader(pickingVertexShader);
        checkShaderErrors(pickingVertexShader, "VERTEX");
        // Create fragment picking shader
        pickingFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(pickingFragmentShader, FileUtils.loadShaderFile("/shaders/picking_shader.shader")[1]);
        GL30.glCompileShader(pickingFragmentShader);
        checkShaderErrors(pickingFragmentShader, "FRAGMENT");


        entityShaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(entityShaderProgram, entityVertexShader);
        GL30.glAttachShader(entityShaderProgram, entityFragmentShader);
        GL30.glLinkProgram(entityShaderProgram);
        GL30.glValidateProgram(entityShaderProgram);
        checkShaderErrors(entityShaderProgram, "PROGRAM");

        terrainShaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(terrainShaderProgram, terrainVertexShader);
        GL30.glAttachShader(terrainShaderProgram, terrainFragmentShader);
        GL30.glLinkProgram(terrainShaderProgram);
        GL30.glValidateProgram(terrainShaderProgram);
        checkShaderErrors(terrainShaderProgram, "PROGRAM");

        outlineShaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(outlineShaderProgram, outlineVertexShader);
        GL30.glAttachShader(outlineShaderProgram, outlineFragmentShader);
        GL30.glLinkProgram(outlineShaderProgram);
        GL30.glValidateProgram(outlineShaderProgram);
        checkShaderErrors(outlineShaderProgram, "PROGRAM");

        shaderPickingProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderPickingProgram, pickingVertexShader);
        GL30.glAttachShader(shaderPickingProgram, pickingFragmentShader);
        GL30.glLinkProgram(shaderPickingProgram);
        GL30.glValidateProgram(shaderPickingProgram);
        checkShaderErrors(shaderPickingProgram, "PROGRAM");

        GL30.glDeleteShader(entityVertexShader);
        GL30.glDeleteShader(entityFragmentShader);
        GL30.glDeleteShader(terrainVertexShader);
        GL30.glDeleteShader(terrainFragmentShader);
        GL30.glDeleteShader(outlineVertexShader);
        GL30.glDeleteShader(outlineFragmentShader);
        GL30.glDeleteShader(pickingVertexShader);
        GL30.glDeleteShader(pickingFragmentShader);
    }

    public void useEntityShader() {
        GL30.glUseProgram(entityShaderProgram);
    }

    public void useTerrainShader() {
        GL30.glUseProgram(terrainShaderProgram);
    }

    public void useOutlineShader() {
        GL30.glUseProgram(outlineShaderProgram);
    }

    public void usePickingShader() {
        GL30.glUseProgram(shaderPickingProgram);
    }

    public void stop() {
        GL30.glUseProgram(0);
    }

    private void checkShaderErrors(int shader, String type) {
        if (!Objects.equals(type, "PROGRAM")) {
            GL30.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
            GL30.glGetShaderInfoLog(shader, 1024);
        } else {
            GL30.glGetProgrami(shader, GL20.GL_LINK_STATUS);
            GL30.glGetProgramInfoLog(shader, 1024);
        }
    }

    public void createUniform(String uniformName, int shaderProgram) throws Exception {
        int uniformLocation = GL30.glGetUniformLocation(shaderProgram, uniformName);
        if (uniformLocation < 0)
            throw new Exception("Could not find uniform " + uniformName + "!");
        uniforms.put(uniformName, uniformLocation);
    }

    public void createMaterialUniform(String uniformName, int shaderProgram) throws Exception {
        createUniform(uniformName + ".ambient", shaderProgram);
        createUniform(uniformName + ".diffuse", shaderProgram);
        createUniform(uniformName + ".specular", shaderProgram);
        createUniform(uniformName + ".hasTexture", shaderProgram);
        createUniform(uniformName + ".reflectance", shaderProgram);
    }

    public void createDirectionalLightUniform(String uniformName, int shaderProgram) throws Exception {
        createUniform(uniformName + ".color", shaderProgram);
        createUniform(uniformName + ".direction", shaderProgram);
        createUniform(uniformName + ".intensity", shaderProgram);

    }

    public void createPointLightUniform(String uniformName, int shaderProgram) throws Exception {
        createUniform(uniformName + ".color", shaderProgram);
        createUniform(uniformName + ".position", shaderProgram);
        createUniform(uniformName + ".intensity", shaderProgram);
        createUniform(uniformName + ".constant", shaderProgram);
        createUniform(uniformName + ".linear", shaderProgram);
        createUniform(uniformName + ".exponent", shaderProgram);
    }

    public void createSpotLightUniform(String uniformName, int shaderProgram) throws Exception {
        createPointLightUniform(uniformName + ".pl", shaderProgram);
        createUniform(uniformName + ".conedir", shaderProgram);
        createUniform(uniformName + ".cutoff", shaderProgram);
    }

    public void createPointLightListUniform(String uniformName, int size, int shaderProgram) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]", shaderProgram);
        }
    }

    public void createSpotLightListUniform(String uniformName, int size, int shaderProgram) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]", shaderProgram);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutoff());
    }

    public void setUniform(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights[i], i);
        }
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights[i], i);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight) {
        setUniform(uniformName + ".color", directionalLight.getColor());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
        setUniform(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".constant", pointLight.getConstant());
        setUniform(uniformName + ".linear", pointLight.getLinear());
        setUniform(uniformName + ".exponent", pointLight.getExponent());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.hasTexture() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL30.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        GL30.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        GL30.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        GL30.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, boolean value) {
        float res = 0;
        if (value)
            res = 1;
        GL30.glUniform1f(uniforms.get(uniformName), res);
    }

    public void setUniform(String uniformName, float value) {
        GL30.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, ImFloat value) {
        GL30.glUniform1f(uniforms.get(uniformName), value.get());
    }

    public int getEntityShaderProgram() {
        return entityShaderProgram;
    }

    public int getTerrainShaderProgram() {
        return terrainShaderProgram;
    }

    public int getOutlineShaderProgram() {
        return outlineShaderProgram;
    }

    public int getShaderPickingProgram() {
        return shaderPickingProgram;
    }

}
