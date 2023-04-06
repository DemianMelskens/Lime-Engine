package org.lime.core.imgui;

import imgui.ImGui;
import imgui.type.ImString;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class ImGuiControls {

    public static void checkbox(String label, boolean value, Consumer<Boolean> onChange) {
        if (ImGui.checkbox(label, value)) {
            onChange.accept(!value);
        }
    }

    public static void dragFloat(String label, float value, Consumer<Float> onChange) {
        float[] output = new float[]{value};
        if (ImGui.dragFloat(label, output)) {
            onChange.accept(output[0]);
        }
    }

    public static void dragFloat3(String label, Vector3f value, Consumer<Vector3f> onChange) {
        float[] output = new float[]{value.x, value.y, value.z};
        if (ImGui.dragFloat3(label, output)) {
            onChange.accept(new Vector3f(output[0], output[1], output[2]));
        }
    }

    public static void inputText(String label, String value, Consumer<String> onChange) {
        ImString output = new ImString(value, 256);
        if (ImGui.inputText(label, output)) {
            onChange.accept(output.get());
        }
    }
}
