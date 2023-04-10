package org.lime.core.imgui;

import imgui.ImGui;

public class ImGuiUtils {

    public static float getLineHeight() {
        return ImGui.getFontSize() + (ImGui.getStyle().getFramePaddingY() * 2);
    }
}
