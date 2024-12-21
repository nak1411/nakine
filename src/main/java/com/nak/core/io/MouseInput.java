package com.nak.core.io;

import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInput {
    private static MouseInput instance;
    private static Camera camera;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private static int currentButton = -1;


    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    private boolean inWindow = false;

    public MouseInput(Camera camera) {
        MouseInput.camera = camera;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseInput get() {
        if (MouseInput.instance == null) {
            MouseInput.instance = new MouseInput(camera);
        }
        return MouseInput.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        currentButton = button;
        if (action == GLFW.GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }
        if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                currentButton = -1;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getLastX() {
        return (float) get().lastX;
    }

    public static float getLastY() {
        return (float) get().lastY;
    }

    public static MouseInput getInstance() {
        return instance;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static void setLastX(float lastX) {
        get().lastX = lastX;
    }

    public static void setLastY(float lastY) {
        get().lastY = lastY;
    }

    public static float getScreenX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * WindowManager.getWidth();
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = WindowManager.getHeight() - (currentY / get().gameViewportSize.y) * WindowManager.getHeight();
        return currentY;
    }

    public static boolean isInWindow() {
        if ((getScreenX() < WindowManager.getWidth()) && (getScreenX() > 0) && (getScreenY() < WindowManager.getHeight() && (getScreenY() > 0)))
            return true;
        else {
            currentButton = -1;
            return false;
        }
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public void setMouseButtonPressed(int button, boolean state) {
        get().mouseButtonPressed[button] = state;
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) {
        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {
        get().gameViewportSize.set(gameViewportSize);
    }

    public static Vector2f getGameViewportPos() {
        return get().gameViewportPos;
    }

    public static Vector2f getGameViewportSize() {
        return get().gameViewportSize;
    }

    public static int getCurrentButton() {
        return currentButton;
    }
}
