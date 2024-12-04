package com.nak.core;

import com.nak.core.gui.ImGuiLayer;
import com.nak.core.io.KeyInput;
import com.nak.core.io.MouseInput;
import com.nak.core.opengl.Framebuffer;
import com.nak.core.util.Constants;
import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.util.Objects;

public class WindowManager {

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    private final String title;
    private static int width, height;
    private static boolean resize, vsync;
    private long window;

    private final Matrix4f projectionMatrix;


    public WindowManager(String title, int width, int height, boolean vsync) {
        this.title = title;
        WindowManager.width = width;
        WindowManager.height = height;
        WindowManager.vsync = vsync;
        projectionMatrix = new Matrix4f();
        init();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();


        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        // AA
        //GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        boolean maximized = false;
        if (width == 0 || height == 0) {
            width = width;
            height = height;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        // Create window
        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window.");

        // Mouse callbacks
        GLFW.glfwSetCursorPosCallback(window, MouseInput::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(window, MouseInput::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(window, MouseInput::mouseScrollCallback);
        // Key callbacks
        GLFW.glfwSetKeyCallback(window, KeyInput::keyCallback);

        // Window size callback
        GLFW.glfwSetWindowSizeCallback(window, (w, newWidth, newHeight) -> {
            System.out.println("Sizing...");
            resize = true;
            WindowManager.setWidth(newWidth);
            WindowManager.setHeight(newHeight);
        });

        // Center window to primary screen
        if (maximized)
            GLFW.glfwMaximizeWindow(window);
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            assert vidMode != null;
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        //Make context current
        GLFW.glfwMakeContextCurrent(window);

        // Set vsync
        if (isVsync())
            GLFW.glfwSwapInterval(1);

        // Make window visible
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        setClearColor();
        GL30.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glDepthFunc(GL11.GL_LESS);
        GL30.glEnable(GL30.GL_STENCIL_TEST);
        GL30.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL30.glStencilOp(GL30.GL_KEEP, GL30.GL_KEEP, GL30.GL_REPLACE);
    }

    public void cleanup() {
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    public static void setClearColor() {
        GL30.glClearColor(Constants.CLEAR_COLOR.x, Constants.CLEAR_COLOR.y, Constants.CLEAR_COLOR.z, Constants.CLEAR_COLOR.w);
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        WindowManager.vsync = vsync;
    }

    public static boolean isResize() {
        return resize;
    }

    public static void setResize(boolean resize) {
        WindowManager.resize = resize;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setWidth(int newWidth) {
        WindowManager.width = newWidth;
    }

    public static void setHeight(int newHeight) {
        WindowManager.height = newHeight;
    }

    public long getWindow() {
        return window;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}
