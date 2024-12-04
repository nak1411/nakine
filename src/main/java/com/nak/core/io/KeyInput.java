package com.nak.core.io;

import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import com.nak.core.rendering.RenderEngine;
import com.nak.core.util.Constants;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class KeyInput {
    private static KeyInput instance;
    private static Camera camera;
    public static Vector3f cameraInc;
    public static Vector3f entityInc;
    private boolean keyPressed[] = new boolean[350];
    public static int currentKey = -1;

    public KeyInput(Camera camera) {
        KeyInput.camera = camera;
        cameraInc = new Vector3f(0, 0, 0);
        entityInc = new Vector3f(0, 0, 0);
    }

    public static KeyInput get() {
        if (KeyInput.instance == null) {
            KeyInput.instance = new KeyInput(camera);
        }
        return KeyInput.instance;
    }

    public static void setCameraInc() {
        cameraInc.set(0, 0, 0);
        if (keyDown(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (keyDown(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
        if (keyDown(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (keyDown(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if (keyDown(GLFW.GLFW_KEY_Q))
            cameraInc.y = 1;
        if (keyDown(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;

    }

    public static void setEntityInc() {
        entityInc.set(0, 0, 0);
        if (keyDown(GLFW.GLFW_KEY_UP))
            entityInc.z = -1;
        if (keyDown(GLFW.GLFW_KEY_DOWN))
            entityInc.z = 1;
        if (keyDown(GLFW.GLFW_KEY_LEFT))
            entityInc.x = -1;
        if (keyDown(GLFW.GLFW_KEY_RIGHT))
            entityInc.x = 1;
        if (keyDown(GLFW.GLFW_KEY_SPACE))
            entityInc.y = 1;
        if (keyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
            entityInc.y = -1;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        currentKey = key;
        if (action == GLFW.GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key] = false;
            cameraInc.set(0, 0, 0);
            currentKey = -1;
        }

        if (key == GLFW.GLFW_KEY_1 && action == GLFW.GLFW_RELEASE)
            RenderEngine.setDepthVisualizer(!RenderEngine.isDepthVisualizer());
        if (key == GLFW.GLFW_KEY_F1 && action == GLFW.GLFW_RELEASE)
            RenderEngine.setIsWireframe(!RenderEngine.isWireframe());
        if (key == GLFW.GLFW_KEY_2 && action == GLFW.GLFW_RELEASE)
            RenderEngine.setPickingVisualizer(!RenderEngine.isPickingVisualizer());
    }

    public static boolean keyDown(int key) {
        return get().keyPressed[key];
    }

    public void update() {
        KeyInput.setCameraInc();
        camera.movePos(cameraInc.x * Constants.CAMERA_MOVE_SPEED, cameraInc.y * Constants.CAMERA_MOVE_SPEED, cameraInc.z * Constants.CAMERA_MOVE_SPEED);
    }

    public static Vector3f getCameraInc() {
        return cameraInc;
    }

    public static void setKeyPressed(boolean state) {
        get().keyPressed[currentKey] = state;
    }

    public static boolean getKeyPressed(int currentKey) {
        return KeyInput.currentKey == currentKey;
    }

    public static void setCameraInc(Vector3f cameraInc) {
        KeyInput.cameraInc = cameraInc;
    }


    public static int getCurrentKey() {
        return currentKey;
    }
}
