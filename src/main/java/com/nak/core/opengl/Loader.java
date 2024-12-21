package com.nak.core.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public Model loadModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = setupVAO();
        setupEBO(indices);
        setupVBO(0, 3, vertices);
        setupVBO(1, 2, textureCoords);
        setupVBO(2, 3, normals);
        return new Model(vaoID, indices.length);
    }

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int vaoID = setupVAO();
        setupEBO(indices);
        setupVBO(0, 3, vertices);
        setupVBO(1, 2, textureCoords);
        return new Model(vaoID, indices.length);
    }

    public Model loadModel(float[] vertices, float[] textureCoords) {
        int vaoID = setupVAO();
        setupVBO(0, 3, vertices);
        setupVBO(1, 2, textureCoords);
        return new Model(vaoID, vertices.length);
    }

    public int loadTexture(String filename) {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null)
                throw new Exception("Image file " + filename + " failed to load! " + STBImage.stbi_failure_reason());
            width = w.get();
            height = h.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int id = GL30.glGenTextures();
        textures.add(id);
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, id);

        GL30.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Mip Mapping
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR_MIPMAP_LINEAR);
        GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_LOD_BIAS, -1);

        // Anisotropic filtering
        float amount = Math.min(4.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
        GL30.glTexParameterf(GL30.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);

        STBImage.stbi_image_free(buffer);
        return id;
    }

    public void setupVBO(int attribLocation, int vertexCount, float[] data) {
        int VBO = GL30.glGenBuffers();
        vbos.add(VBO);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        FloatBuffer buffer = createFloatBuffer(data);
        // Store data into VBO statically, won't ever be edited
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
        GL30.glEnableVertexAttribArray(attribLocation);
        GL30.glVertexAttribPointer(attribLocation, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void setupEBO(int[] data) {
        int VBO = GL30.glGenBuffers();
        vbos.add(VBO);
        GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO);
        IntBuffer buffer = createIntBuffer(data);
        // Store data into EBO statically, won't ever be edited
        GL30.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
    }

    public int setupVAO() {
        int VAO = GL30.glGenVertexArrays();
        vaos.add(VAO);
        GL30.glBindVertexArray(VAO);
        return VAO;
    }

    public void cleanup() {
        for (int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            GL30.glDeleteBuffers(vbo);
        for (int texture : textures)
            GL30.glDeleteTextures(texture);
    }

    /**
     * Create Float Buffer
     **/
    private static FloatBuffer createFloatBuffer(float[] data) {
        // Vertex array data must be stored in float buffer
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        // Put vertex array into the float buffer
        buffer.put(data);
        // Prepares float buffer to be read from
        buffer.flip();
        return buffer;
    }

    /**
     * Create Int Buffer
     **/
    private static IntBuffer createIntBuffer(int[] data) {
        // Vertex array data must be stored in float buffer
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        // Put vertex array into the float buffer
        buffer.put(data);
        // Prepares float buffer to be read from
        buffer.flip();
        return buffer;
    }
}
