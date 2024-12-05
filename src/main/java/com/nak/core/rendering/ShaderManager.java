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

    private int vertexShader, geometryShader, fragmentShader, outlineVertexShader, outlineFragmentShader, pickingVertexShader, pickingFragmentShader;
    private int shaderProgram, shaderOutlineProgram, shaderPickingProgram;

    private final Map<String, Integer> uniforms;

    public ShaderManager() {
        uniforms = new HashMap<>();
    }

    public void createShaders() {
        // Create vertex shader
        vertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(vertexShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[0]);
        GL30.glCompileShader(vertexShader);
        checkShaderErrors(vertexShader, "VERTEX");
        // Create fragment shader
        fragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(fragmentShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[1]);
        GL30.glCompileShader(fragmentShader);
        checkShaderErrors(fragmentShader, "FRAGMENT");
        // Create vertex outline shader
        outlineVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(outlineVertexShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[2]);
        GL30.glCompileShader(outlineVertexShader);
        checkShaderErrors(outlineVertexShader, "OUTLINE_VERTEX");
        // Create fragment outline shader
        outlineFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(outlineFragmentShader, FileUtils.loadShaderFile("/shaders/entity_shader.shader")[3]);
        GL30.glCompileShader(outlineFragmentShader);
        checkShaderErrors(outlineFragmentShader, "OUTLINE_FRAGMENT");
        // Create vertex picking shader
        pickingVertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(pickingVertexShader, FileUtils.loadShaderFile("/shaders/picking_shader.shader")[4]);
        GL30.glCompileShader(pickingVertexShader);
        checkShaderErrors(pickingVertexShader, "PICKING_VERTEX");
        // Create fragment picking shader
        pickingFragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(pickingFragmentShader, FileUtils.loadShaderFile("/shaders/picking_shader.shader")[5]);
        GL30.glCompileShader(pickingFragmentShader);
        checkShaderErrors(pickingFragmentShader, "PICKING_FRAGMENT");


        shaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderProgram, vertexShader);
        GL30.glAttachShader(shaderProgram, fragmentShader);
        GL30.glLinkProgram(shaderProgram);
        GL30.glValidateProgram(shaderProgram);
        checkShaderErrors(shaderProgram, "PROGRAM");

        shaderOutlineProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderOutlineProgram, outlineVertexShader);
        GL30.glAttachShader(shaderOutlineProgram, outlineFragmentShader);
        GL30.glLinkProgram(shaderOutlineProgram);
        GL30.glValidateProgram(shaderOutlineProgram);
        checkShaderErrors(shaderOutlineProgram, "PROGRAM");

        shaderPickingProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderPickingProgram, pickingVertexShader);
        GL30.glAttachShader(shaderPickingProgram, pickingFragmentShader);
        GL30.glLinkProgram(shaderPickingProgram);
        GL30.glValidateProgram(shaderPickingProgram);
        checkShaderErrors(shaderPickingProgram, "PROGRAM");

        GL30.glDeleteShader(vertexShader);
        GL30.glDeleteShader(fragmentShader);
        GL30.glDeleteShader(outlineVertexShader);
        GL30.glDeleteShader(outlineFragmentShader);
        GL30.glDeleteShader(pickingVertexShader);
        GL30.glDeleteShader(pickingFragmentShader);
    }

    public void useNormalShader() {
        GL30.glUseProgram(shaderProgram);
    }

    public void useOutlineShader() {
        GL30.glUseProgram(shaderOutlineProgram);

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

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient", shaderProgram);
        createUniform(uniformName + ".diffuse", shaderProgram);
        createUniform(uniformName + ".specular", shaderProgram);
        createUniform(uniformName + ".hasTexture", shaderProgram);
        createUniform(uniformName + ".reflectance", shaderProgram);
    }

    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color", shaderProgram);
        createUniform(uniformName + ".direction", shaderProgram);
        createUniform(uniformName + ".intensity", shaderProgram);

    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color", shaderProgram);
        createUniform(uniformName + ".position", shaderProgram);
        createUniform(uniformName + ".intensity", shaderProgram);
        createUniform(uniformName + ".constant", shaderProgram);
        createUniform(uniformName + ".linear", shaderProgram);
        createUniform(uniformName + ".exponent", shaderProgram);
    }

    public void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir", shaderProgram);
        createUniform(uniformName + ".cutoff", shaderProgram);
    }

    public void createPointLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
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

    public int getShaderProgram() {
        return shaderProgram;
    }

    public void setShaderProgram(int shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public int getShaderOutlineProgram() {
        return shaderOutlineProgram;
    }

    public void setShaderOutlineProgram(int shaderOutlineProgram) {
        this.shaderOutlineProgram = shaderOutlineProgram;
    }

    public int getShaderPickingProgram() {
        return shaderPickingProgram;
    }

    public void setShaderPickingProgram(int shaderPickingProgram) {
        this.shaderPickingProgram = shaderPickingProgram;
    }
}
