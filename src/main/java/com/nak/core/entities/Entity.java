package com.nak.core.entities;

import com.nak.core.WindowManager;
import com.nak.core.opengl.Model;
import com.nak.core.util.Constants;
import com.nak.core.util.Utils;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImFloat;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Entity {

    private Model model;
    private Vector3f pos, rotation;
    private ImFloat scale;
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public Entity(int uid, Model model, Vector3f pos, Vector3f rotation, ImFloat scale) {
        this.uid = uid;
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void imgui() {
        // Entity ID
        ImGui.text("ID:");
        ImGui.sameLine(ImGui.getWindowWidth() - 215);
        ImGui.textColored(0.0f, 1.0f, 1.0f, 1.0f, String.valueOf(uid + 1));

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

    public void incPos(Vector3f pos) {
        this.pos.x += pos.x * Constants.MOUSE_MOVE_SPEED;
        this.pos.y += pos.y * Constants.MOUSE_MOVE_SPEED;
        this.pos.z += pos.z * Constants.MOUSE_MOVE_SPEED;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void incRotation(Vector3f rotation) {
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
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

    public void genId() {
        if (this.uid == -1)
            this.uid = ID_COUNTER++;
    }

    public int getUid() {
        return this.uid + 1;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
}
