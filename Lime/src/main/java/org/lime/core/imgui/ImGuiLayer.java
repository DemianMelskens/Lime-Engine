package org.lime.core.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.lime.core.Layer;
import org.lwjgl.glfw.GLFW;

public class ImGuiLayer extends Layer {

    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl3;

    public ImGuiLayer() {
        super("ImGui Layer");
        this.imGuiGlfw = new ImGuiImplGlfw();
        this.imGuiGl3 = new ImGuiImplGl3();
    }

    @Override
    public void onAttach() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setConfigFlags(
                ImGuiConfigFlags.NavEnableKeyboard |
                        ImGuiConfigFlags.DockingEnable |
                        ImGuiConfigFlags.ViewportsEnable
        );
        ImGui.styleColorsDark();

        ImGuiStyle style = ImGui.getStyle();
        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, 0.0f, 0.0f, 0.0f, 1.0f);
        }
        imGuiGlfw.init(GLFW.glfwGetCurrentContext(), true);
        imGuiGl3.init("#version 410");
    }

    @Override
    public void onDetach() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public void begin() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void end() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long windowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(windowPtr);
        }
    }

    @Override
    public void onImGuiRender() {
        ImGui.showDemoWindow(new ImBoolean(true));
    }
}
