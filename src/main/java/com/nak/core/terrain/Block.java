package com.nak.core.terrain;

import com.nak.core.opengl.Model;
import imgui.type.ImFloat;
import org.joml.Vector3f;

public class Block {

    private Vector3f pos, rotation;
    private Model model;
    private ImFloat scale;

    public static enum TYPE {
        DIRT, GRASS;
    }

    public TYPE type;

    public Block(Model model, Vector3f pos, Vector3f rotation, ImFloat scale, TYPE type) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
        this.type = type;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public ImFloat getScale() {
        return scale;
    }
}
