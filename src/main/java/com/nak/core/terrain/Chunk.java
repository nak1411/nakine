package com.nak.core.terrain;

import com.nak.core.entities.Camera;
import com.nak.core.entities.Entity;
import com.nak.core.entities.SceneManager;
import com.nak.core.opengl.Loader;
import com.nak.core.opengl.Model;
import com.nak.core.opengl.ModelLoader;
import com.nak.core.textures.Texture;
import com.nak.core.util.Constants;
import imgui.type.ImFloat;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private List<Block> blocks;
    private Vector3f origin;

    public Chunk(List<Block> blocks, Vector3f origin) {
        this.setBlocks(blocks);
        this.origin = origin;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }
}
