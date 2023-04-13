package org.lime.editor;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.lime.core.Application;
import org.lime.core.Layer;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.buffers.FrameBuffer;
import org.lime.core.scene.Scene;
import org.lime.core.scene.serialization.SceneDeserializer;
import org.lime.core.scene.serialization.SceneSerializer;
import org.lime.core.time.TimeStep;
import org.lime.editor.panels.SceneHierarchyPanel;
import org.lime.editor.panels.StatisticsPanel;

public class EditorLayer extends Layer {
    private Scene activeScene;
    private SceneHierarchyPanel sceneHierarchyPanel;
    private StatisticsPanel statisticsPanel;
    private OrthographicCameraController cameraController;
    private FrameBuffer frameBuffer;
    private ImVec2 viewPortSize = new ImVec2(0.0f, 0.0f);
    private boolean viewportFocused = false;
    private boolean viewportHovered = false;

    public EditorLayer() {
        super("Example");
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
    }

    @Override
    public void onAttach() {
        this.sceneHierarchyPanel = new SceneHierarchyPanel();
        this.statisticsPanel = new StatisticsPanel();

        FrameBuffer.Specification specification = FrameBuffer.createSpec(
            Application.getWindow().getWidth(),
            Application.getWindow().getHeight()
        );
        this.frameBuffer = FrameBuffer.create(specification);
        setScene(new Scene());
    }

    @Override
    public void onDetach() {
        frameBuffer.shutdown();
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        Renderer2D.resetStatistics();

        FrameBuffer.Specification specification = frameBuffer.getSpecification();
        if (viewPortSize.x > 0.0f && viewPortSize.y > 0.0f &&
            (specification.width != viewPortSize.x || specification.height != viewPortSize.y)) {
            frameBuffer.resize((int) viewPortSize.x, (int) viewPortSize.y);
            cameraController.onResize(viewPortSize.x, viewPortSize.y);
            activeScene.onViewportResize((int) viewPortSize.x, (int) viewPortSize.y);
        }

        if (viewportFocused)
            cameraController.onUpdate(timestep);


        frameBuffer.bind();
        RenderCommand.setClearColor(0.1f, 0.1f, 0.1f, 1f);
        RenderCommand.clear();

        activeScene.onUpdate(timestep);

        frameBuffer.unbind();
    }

    @Override
    public void onImGuiRender() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGuiViewport viewport = ImGui.getMainViewport();
        ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
        ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
        ImGui.setNextWindowViewport(viewport.getID());

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize;
        windowFlags |= ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        ImGui.begin("dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(3);

        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("Close"))
                    Application.get().shutdown();

                if (ImGui.menuItem("Open"))
                    setScene(SceneDeserializer.create().deserialize("scenes/scene.lime"));

                if (ImGui.menuItem("Save"))
                    SceneSerializer.create(activeScene).serialize("scenes/scene.lime");

                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }

        ImVec2 minWindowSize = ImGui.getStyle().getWindowMinSize();
        ImGui.getStyle().setWindowMinSize(370.0f, minWindowSize.y);

        ImGui.dockSpace(ImGui.getID("Dockspace"));

        ImGui.getStyle().setWindowMinSize(minWindowSize.x, minWindowSize.y);

        ImGui.end();

        sceneHierarchyPanel.onImGuiRender();
        statisticsPanel.onImGuiRender();

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        ImGui.begin("Scene");

        viewportFocused = ImGui.isWindowFocused();
        viewportHovered = ImGui.isWindowHovered();
        Application.get().getImGuiLayer().setBlockEvents(!viewportFocused || !viewportHovered);

        viewPortSize = ImGui.getContentRegionAvail();
        ImGui.image(frameBuffer.getColorAttachment(), viewPortSize.x, viewPortSize.y, 0, 1, 1, 0);

        ImGui.end();
        ImGui.popStyleVar();
    }

    @Override
    public void onEvent(Event event) {
        cameraController.onEvent(event);
    }

    private void setScene(Scene scene) {
        this.activeScene = scene;
        this.sceneHierarchyPanel.setContext(scene);
        activeScene.onViewportResize((int) viewPortSize.x, (int) viewPortSize.y);
    }
}
