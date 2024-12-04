package com.nak.core.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class Texture {

    private int id;
    private int width, height;

    public Texture(int id) {
        this.id = id;
    }

    public Texture(int width, int height) {
        id = GL30.glGenTextures();
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

    }

    public int getId() {
        return id;
    }
}
