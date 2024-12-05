package com.nak.core.terrain;

import com.nak.core.opengl.Model;
import com.nak.core.util.Constants;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import org.joml.Vector3f;

public class Block {

    private Model model;
    private Vector3f pos, rotation;
    private ImFloat scale;

    public Block(Model model, Vector3f pos, Vector3f rotation, ImFloat scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public static float[] vertices = new float[]{
            -1.0f, 1.0f, -1.0f, // V0
            -1.0f, -1.0f, -1.0f, // V1
            1.0f, -1.0f, -1.0f, // V2
            1.0f, 1.0f, -1.0f, // V3

            -1.0f, 1.0f, 1.0f, // V4
            -1.0f, -1.0f, 1.0f, // V5
            1.0f, -1.0f, 1.0f, // V6
            1.0f, 1.0f, 1.0f, // V7

            1.0f, 1.0f, -1.0f, // V8
            1.0f, -1.0f, -1.0f, // V9
            1.0f, -1.0f, 1.0f, // V10
            1.0f, 1.0f, 1.0f, // V11

            -1.0f, 1.0f, -1.0f, // V12
            -1.0f, -1.0f, -1.0f, // V13
            -1.0f, -1.0f, 1.0f, // V14
            -1.0f, 1.0f, 1.0f, // V15

            -1.0f, 1.0f, 1.0f, // V16
            -1.0f, 1.0f, -1.0f, // V17
            1.0f, 1.0f, -1.0f, // V18
            1.0f, 1.0f, 1.0f, // V19

            -1.0f, -1.0f, 1.0f, // V20
            -1.0f, -1.0f, -1.0f, // V21
            1.0f, -1.0f, -1.0f, // V22
            1.0f, -1.0f, 1.0f, // V23
    };

    public static float[] texturePos = new float[]{
            // Side
            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            // Top
            2.01f / 3f, 1.01f / 3f,
            2.01f / 3f, 1.99f / 3f,
            2.99f / 3f, 1.99f / 3f,
            2.99f / 3f, 1.01f / 3f,

            // Bottom
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,
    };

    public static int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
            4, 5, 7, 7, 5, 6,
            8, 9, 11, 11, 9, 10,
            12, 13, 15, 15, 13, 14,
            16, 17, 19, 19, 17, 18,
            20, 21, 23, 23, 21, 22
    };

    public static float[] normals = new float[]{
            -0.5f, 0.5f, -0.5f, // V0
            -0.5f, -0.5f, -0.5f, // V1
            0.5f, -0.5f, -0.5f, // V2
            0.5f, 0.5f, -0.5f, // V3

            -0.5f, 0.5f, 0.5f, // V4
            -0.5f, -0.5f, 0.5f, // V5
            0.5f, -0.5f, 0.5f, // V6
            0.5f, 0.5f, 0.5f, // V7

            0.5f, 0.5f, -0.5f, // V8
            0.5f, -0.5f, -0.5f, // V9
            0.5f, -0.5f, 0.5f, // V10
            0.5f, 0.5f, 0.5f, // V11

            -0.5f, 0.5f, -0.5f, // V12
            -0.5f, -0.5f, -0.5f, // V13
            -0.5f, -0.5f, 0.5f, // V14
            -0.5f, 0.5f, 0.5f, // V15

            -0.5f, 0.5f, 0.5f, // V16
            -0.5f, 0.5f, -0.5f, // V17
            0.5f, 0.5f, -0.5f, // V18
            0.5f, 0.5f, 0.5f, // V19

            -0.5f, -0.5f, 0.5f, // V20
            -0.5f, -0.5f, -0.5f, // V21
            0.5f, -0.5f, -0.5f, // V22
            0.5f, -0.5f, 0.5f, // V23
    };

    public void imgui() {

        ImGui.text("Position");
        float[] posVec = {pos.x, pos.y, pos.z};
        ImGui.inputFloat3(" ", posVec, "%.2f", ImGuiInputTextFlags.EnterReturnsTrue);
        setPos(new Vector3f(posVec[0], posVec[1], posVec[2]));

        ImGui.text("Rotation:");
        ImGui.sameLine(ImGui.getWindowWidth() - 215);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, "X: " + String.format("%.1f", rotation.x) + " Y: " + String.format("%.1f", rotation.y) + " Z: " + String.format("%.1f", rotation.z));

        ImGui.text("Scale:");
        ImFloat scaleInput = new ImFloat(scale);
        ImGui.inputFloat("Scale", scaleInput, 0.01f, 0.1f, "%.2f", ImGuiInputTextFlags.EnterReturnsTrue);
        setScale(scaleInput);

        ImGui.text("Vertices:");
        ImGui.sameLine(ImGui.getWindowWidth() - 215);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(model.getVertexCount()));

        ImGui.separator();

        float w = (ImGui.getContentRegionAvail().x - ImGui.getStyle().getItemSpacing().y) * 0.40f;
        ImGui.setNextItemWidth(w);
        float[] pickerColor = {Constants.OUTLINE_COLOR.x, Constants.OUTLINE_COLOR.y, Constants.OUTLINE_COLOR.z, Constants.OUTLINE_COLOR.w};
        ImGui.colorPicker4("Outline Color", pickerColor);
        Constants.OUTLINE_COLOR.set(pickerColor[0], pickerColor[1], pickerColor[2], pickerColor[3]);
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public ImFloat getScale() {
        return scale;
    }

    public void setScale(ImFloat scale) {
        this.scale = scale;
    }
}
