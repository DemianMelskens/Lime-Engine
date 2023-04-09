package org.lime.core.imgui;

import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lombok.Setter;
import org.lime.core.Layer;
import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;
import org.lime.core.time.TimeStep;
import org.lime.core.utils.Resources;
import org.lwjgl.glfw.GLFW;

public class ImGuiLayer extends Layer {

    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl3;

    @Setter
    private boolean blockEvents = true;

    public ImGuiLayer() {
        super("ImGui Layer");
        this.imGuiGlfw = new ImGuiImplGlfw();
        this.imGuiGl3 = new ImGuiImplGl3();

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();

        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setConfigViewportsNoTaskBarIcon(true);

        ImGui.styleColorsDark();
        initFonts(io);

        ImGuiStyle style = ImGui.getStyle();
        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, 0.0f, 0.0f, 0.0f, 1.0f);
        }
        imGuiGlfw.init(GLFW.glfwGetCurrentContext(), true);
        imGuiGl3.init("#version 410");
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    @Override
    public void onUpdate(TimeStep timestep) {
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
    }

    @Override
    public void onEvent(Event event) {
        if (blockEvents) {
            ImGuiIO io = ImGui.getIO();
            event.isHandled |= event.isInCategory(EventCategory.MOUSE) && io.getWantCaptureMouse();
            event.isHandled |= event.isInCategory(EventCategory.KEYBOARD) && io.getWantCaptureKeyboard();
        }
    }

    private void initFonts(ImGuiIO io) {
        ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(ImGuiIcons._IconRange);

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(true);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/open-sans/OpenSans-Regular.ttf"), 20.0f, fontConfig, glyphRanges);
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/open-sans/OpenSans-Bold.ttf"), 20.0f, fontConfig, glyphRanges);
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/fontawesome/fa-regular-400.ttf"), 10.0f, fontConfig, glyphRanges);
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/fontawesome/fa-solid-900.ttf"), 10.0f, fontConfig, glyphRanges);
        io.getFonts().build();

        fontConfig.destroy();
    }

}
