package com.nak.core;

import com.nak.core.io.KeyInput;
import com.nak.core.util.Constants;
import com.nak.test.Launcher;
import com.nak.test.TestGame;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

public class EngineManager {

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frameTime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private Logic gameLogic;

    private void init() throws Exception {
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        gameLogic.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;


            while (unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;

                if (window.windowShouldClose())
                    stop();
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Constants.TITLE + getFps());
                    frames = 0;
                    frameCounter = 0;
                }

            }

            if (render) {
                update(frameTime);
                input();
                render();
                frames++;

            }
        }
        cleanup();
    }

    private void stop() {
        if (!isRunning)
            return;
        isRunning = false;
    }

    private void input() {
        gameLogic.input();
    }

    private void render() {
        gameLogic.render();
    }

    private void update(float frameTime) {
        if (KeyInput.keyDown(GLFW.GLFW_KEY_ESCAPE))
            GLFW.glfwSetWindowShouldClose(window.getWindow(), true);

        Launcher.getGame().update(frameTime);
        GLFW.glfwSwapBuffers(window.getWindow());
        GLFW.glfwPollEvents();
    }

    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
