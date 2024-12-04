package com.nak.core.util;

import com.nak.core.WindowManager;
import com.nak.core.entities.Camera;
import com.nak.core.io.MouseInput;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class MousePicker {

    private Vector3f currentRay;
    private Matrix4f projectionMatrix, viewMatrix;
    private Camera camera;

    public MousePicker(Matrix4f projectionMatrix, Camera camera) {
        this.projectionMatrix = projectionMatrix;
        this.camera = camera;
        this.viewMatrix = Utils.getViewMatrix(camera);
    }

    private Vector3f calcMouseRay() {
        float mouseX = (float) MouseInput.getX();
        float mouseY = (float) MouseInput.getY();
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);

        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = viewMatrix.invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = projectionMatrix.invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
        float x = (2f * mouseX) / WindowManager.getWidth() - 1f;
        float y = (2f * mouseY) / WindowManager.getHeight() - 1f;
        return new Vector2f(x, -y);
    }

    public void update() {
        viewMatrix = Utils.getViewMatrix(camera);
        currentRay = calcMouseRay();
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }
}
