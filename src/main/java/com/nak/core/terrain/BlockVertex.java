package com.nak.core.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class BlockVertex {

    public Vector3f positions, normals;
    public Vector2f texturePos;

    public BlockVertex(Vector3f positions, Vector2f texturePos, Vector3f normals) {
        this.positions = positions;
        this.texturePos = texturePos;
        this.normals = normals;
    }
}
