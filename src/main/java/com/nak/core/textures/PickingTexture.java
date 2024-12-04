package com.nak.core.textures;

import com.nak.core.rendering.RenderEngine;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class PickingTexture {

    private int pickingTextureId;
    private int fbo;
    private int depthTexture;

    public PickingTexture(int width, int height) {
        if (!init(width, height)) {
            assert false : "Error initializing picking texture.";
        }
    }

    public boolean init(int width, int height) {
        // Create FBO
        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

        // Create textures
        pickingTextureId = GL30.glGenTextures();
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, pickingTextureId);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, width, height, 0, GL30.GL_RGB, GL11.GL_FLOAT, 0);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, pickingTextureId, 0);

        // Create depth buffer object
        depthTexture = GL30.glGenTextures();
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, 0);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error completing framebuffer";
            return false;
        }

        // Unbind texture and buffer
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        return true;
    }

    public boolean resizePickingTexture(int width, int height) {
        // Create FBO
        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

        // Create textures
        pickingTextureId = GL30.glGenTextures();
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, pickingTextureId);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, width, height, 0, GL30.GL_RGB, GL11.GL_FLOAT, 0);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, pickingTextureId, 0);

        // Create depth buffer object
        GL30.glEnable(GL11.GL_DEPTH_TEST);
        depthTexture = GL30.glGenTextures();
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, 0);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error completing framebuffer";
            return false;
        }

        // Unbind texture and buffer
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        return true;
    }

    public void enableWriting() {
        int drawFbo = 0;
        if (RenderEngine.isPickingVisualizer())
            drawFbo = 1;
        else
            drawFbo = 2;
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, drawFbo);
    }

    public void disableWriting() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int x, int y) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo);

        GL30.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
        float[] pixels = new float[3];
        GL30.glReadPixels(x, y, 1, 1, GL30.GL_RGB, GL11.GL_FLOAT, pixels);

        GL30.glReadBuffer(GL11.GL_NONE);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);

        return (int) (pixels[0] * 256);
    }


}
