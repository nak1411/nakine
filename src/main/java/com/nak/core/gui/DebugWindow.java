package com.nak.core.gui;

import imgui.ImGui;

public class DebugWindow {

    private boolean showText = false;

    public void debugWindow() {
        ImGui.begin("Debug");

        if (ImGui.button("Test")) {
            showText = true;
        }

        if (showText) {
            ImGui.text("Test Click");
            ImGui.sameLine();
            if (ImGui.button("Stop Test")) {
                showText = false;
            }
        }
        ImGui.end();
    }
}
