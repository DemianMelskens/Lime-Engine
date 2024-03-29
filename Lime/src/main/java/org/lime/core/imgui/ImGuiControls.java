package org.lime.core.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector3f;
import org.lime.core.renderer.Color;
import org.lime.core.utils.FileDialogs;

import java.util.List;
import java.util.function.Consumer;

public class ImGuiControls {

    public static void button(String label, Runnable onClick) {
        if (ImGui.button(label)) {
            onClick.run();
        }
    }

    public static void button(String label, float width, float height, Runnable onClick) {
        if (ImGui.button(label, width, height)) {
            onClick.run();
        }
    }

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

    public static void dragFloat(String label, float value, float speed, float min, float max, String format, Consumer<Float> onChange) {
        float[] output = new float[]{value};
        if (ImGui.dragFloat(label, output, speed, min, max, format)) {
            onChange.accept(output[0]);
        }
    }

    public static void dragFloat3(String label, Vector3f value, Consumer<Vector3f> onChange) {
        dragFloat3(label, value, onChange, 0.0f, 80.0f);
    }

    public static void dragFloat3(String label, Vector3f value, Consumer<Vector3f> onChange, float resetValue, float columnWidth) {
        ImGuiFonts fonts = ImGuiFonts.get();

        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.sameLine();

        ImGui.nextColumn();
        float btnSize = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        float itemWidth = (ImGui.getContentRegionAvailX() - (3 * btnSize)) / 3;

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0.0f, 0.0f);

        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushFont(fonts.getFont("OpenSans-Bold"));
        if (ImGui.button("X", btnSize, btnSize))
            onChange.accept(new Vector3f(resetValue, value.y, value.z));
        ImGui.popFont();
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        ImGui.pushItemWidth(itemWidth);
        dragFloat("##X", value.x, 0.1f, 0.0f, 0.0f, "%.2f", output -> onChange.accept(new Vector3f(output, value.y, value.z)));
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushFont(fonts.getFont("OpenSans-Bold"));
        if (ImGui.button("Y", btnSize, btnSize))
            onChange.accept(new Vector3f(value.x, resetValue, value.z));
        ImGui.popFont();
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        ImGui.pushItemWidth(itemWidth);
        dragFloat("##Y", value.y, 0.1f, 0.0f, 0.0f, "%.2f", output -> onChange.accept(new Vector3f(value.x, output, value.z)));
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.25f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.35f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.25f, 0.8f, 1.0f);
        ImGui.pushFont(fonts.getFont("OpenSans-Bold"));
        if (ImGui.button("Z", btnSize, btnSize))
            onChange.accept(new Vector3f(value.x, value.y, resetValue));
        ImGui.popFont();
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        ImGui.pushItemWidth(itemWidth);
        dragFloat("##Z", value.z, 0.1f, 0.0f, 0.0f, "%.2f", output -> onChange.accept(new Vector3f(value.x, value.y, output)));
        ImGui.popItemWidth();

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void colorEdit4(String label, Color value, Consumer<Color> onChange) {
        float[] output = new float[]{value.r, value.g, value.b, value.a};
        if (ImGui.colorEdit4(label, output)) {
            onChange.accept(Color.create(output[0], output[1], output[2], output[3]));
        }
    }

    public static void inputText(String label, String value, Consumer<String> onChange) {
        ImString output = new ImString(value, 256);
        if (ImGui.inputText(label, output)) {
            onChange.accept(output.get());
        }
    }

    public static void popupContextWindow(String id, Runnable children) {
        int popupFlags = ImGuiPopupFlags.MouseButtonRight;
        if (ImGui.beginPopupContextWindow(id, popupFlags)) {
            children.run();
            ImGui.endPopup();
        }
    }

    public static void menuBar(Runnable children) {
        if (ImGui.beginMenuBar()) {
            children.run();
            ImGui.endMenuBar();
        }
    }

    public static void menu(String label, Runnable children) {
        if (ImGui.beginMenu(label)) {
            children.run();
            ImGui.endMenu();
        }
    }

    public static void popup(String id, Runnable children) {
        if (ImGui.beginPopup(id)) {
            children.run();
            ImGui.endPopup();
        }
    }

    public static void popupContextItem(Runnable children) {
        int popupFlags = ImGuiPopupFlags.MouseButtonRight;
        if (ImGui.beginPopupContextItem(popupFlags)) {
            children.run();
            ImGui.endPopup();
        }
    }

    public static void popupContextWindow(Runnable children) {
        int popupFlags = ImGuiPopupFlags.MouseButtonRight;
        if (ImGui.beginPopupContextWindow(popupFlags)) {
            children.run();
            ImGui.endPopup();
        }
    }

    public static void menuItem(String label, Runnable onClick) {
        if (ImGui.menuItem(label)) {
            onClick.run();
        }
    }

    public static void openFile(String title, String description, List<String> accept, Consumer<String> onOpen) {
        String path = FileDialogs.openFile(title, description, accept);
        if (path != null)
            onOpen.accept(path);
    }

    public static void saveFile(String title, String description, List<String> accept, Consumer<String> onSave) {
        String path = FileDialogs.saveFile(title, description, accept);
        if (path != null)
            onSave.accept(path);
    }
}
