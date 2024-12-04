package com.nak.core.lighting;

import org.joml.Vector3f;

public class DirectionalLight {
    private Vector3f direction, color;
    private float intensity;

    public DirectionalLight(Vector3f direction, Vector3f color, float intensity) {
        this.direction = direction;
        this.color = color;
        this.intensity = intensity;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
