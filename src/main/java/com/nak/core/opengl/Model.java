package com.nak.core.opengl;

import com.nak.core.textures.Material;
import com.nak.core.textures.Texture;

public class Model {

    private int vaoID, vertexCount;
    private Material material;
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;


    public Model(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.material = new Material();
    }

    public Model(int vaoID, int vertexCount, Texture texture) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.material = new Material(texture);
    }

    public Model(Model model, Texture texture) {
        this.vaoID = model.getVaoID();
        this.vertexCount = model.getVertexCount();
        this.material = model.getMaterial();
        this.material.setTexture(texture);
    }

    public Model(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                 float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }


    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Material getMaterial() {
        return material;
    }

    public Texture getTexture() {
        return material.getTexture();
    }

    public void setTexture(Texture texture) {
        material.setTexture(texture);
    }

    public void setTexture(Texture texture, float reflectance) {
        material.setTexture(texture);
        this.material.setReflectance(reflectance);
    }
}
