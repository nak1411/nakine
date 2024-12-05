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
            // Front Face
            -0.5f, 0.5f, 0.5f, // V0
            -0.5f, -0.5f, 0.5f, // V1
            0.5f, -0.5f, 0.5f, // V2
            0.5f, 0.5f, 0.5f, // V3

            // Top Face
            -0.5f, 0.5f, -0.5f, // V4
            0.5f, 0.5f, -0.5f, // V5
            -0.5f, 0.5f, 0.5f, // V6
            0.5f, 0.5f, 0.5f, // V7

            // Right Face
            0.5f, 0.5f, 0.5f, // V8
            0.5f, -0.5f, 0.5f, // V9
            0.5f, -0.5f, -0.5f, // V10
            0.5f, 0.5f, -0.5f, // V11

            // Left Face
            -0.5f, 0.5f, 0.5f, // V12
            -0.5f, 0.5f, -0.5f, // V13
            -0.5f, -0.5f, -0.5f, // V14
            -0.5f, -0.5f, 0.5f, // V15

            // Bottom Face
            -0.5f, -0.5f, -0.5f, // V16
            -0.5f, -0.5f, 0.5f, // V17
            0.5f, -0.5f, -0.5f, // V18
            0.5f, -0.5f, 0.5f, // V19

            // Back Face
            -0.5f, 0.5f, -0.5f, // V20
            0.5f, 0.5f, -0.5f, // V21
            -0.5f, -0.5f, -0.5f, // V22
            0.5f, -0.5f, -0.5f, // V23
    };

    public static float[] texturePos = new float[]{
            // Front Face
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            // Top Face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // Right Face
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            // Left Face
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // Bottom Face
            0.5f, 0.5f,
            0.5f, 0.0f,
            1.0f, 0.5f,
            1.0f, 0.0f,

            // Back Face
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.5f,
            0.0f, 0.5f,
    };

    public static int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,  // Front Face
            7, 5, 6, 6, 5, 4,  // Top Face
            8, 9, 11, 11, 9, 10, // Right Face
            12, 13, 15, 15, 13, 14, // Left Face
            19, 17, 18, 18, 17, 16, // Bottom Face
            20, 21, 22, 22, 21, 23  // Back Face
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
