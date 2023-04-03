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
import org.lime.core.renderer.Color;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.buffers.FrameBuffer;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.time.TimeStep;

import java.util.Objects;

import static org.lime.core.utils.Log.LM_CORE_DEBUG;

public class EditorLayer extends Layer {
    private Scene activeScene;
    private Entity entity;
    private OrthographicCameraController cameraController;
    private Texture2D spriteSheet;
    private FrameBuffer frameBuffer;
    private ImVec2 viewPortSize;
    private boolean viewportFocused = false;
    private boolean viewportHovered = false;

    public EditorLayer() {
        super("Example");
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
    }

    @Override
    public void onAttach() {
        this.spriteSheet = Texture2D.create("/textures/RPG_sheet.png");
        FrameBuffer.Specification specification = FrameBuffer.createSpec(
                Application.getWindow().getWidth(),
                Application.getWindow().getHeight()
        );
        this.frameBuffer = FrameBuffer.create(specification);
        this.activeScene = new Scene();

        entity = activeScene.createEntity();
        entity.addComponent(new TransformComponent());
        entity.addComponent(new SpriteRendererComponent(Color.create(0.8f, 0.2f, 0.3f, 1.0f)));
    }

    @Override
    public void onDetach() {
        frameBuffer.shutdown();
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        Renderer2D.resetStatistics();
        if (viewportFocused)
            cameraController.onUpdate(timestep);


        frameBuffer.bind();
        RenderCommand.setClearColor(0.1f, 0.1f, 0.1f, 1f);
        RenderCommand.clear();

        Renderer2D.beginScene(cameraController.getCamera());
        activeScene.onUpdate(timestep);
        Renderer2D.endScene();

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
                if (ImGui.menuItem("close")) {
                    Application.get().shutdown();
                }
                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }

        ImGui.dockSpace(ImGui.getID("Dockspace"));

        ImGui.end();

        ImGui.begin("Project");
        ImGui.end();

        ImGui.begin("Inspector");

        Color color = entity.getComponent(SpriteRendererComponent.class).color;
        float[] value = new float[]{color.r(), color.g(), color.b(), color.a()};

        if (ImGui.colorEdit4("Color", value)) {
            color.set(value[0], value[1], value[2], value[3]);
        }

        ImGui.text(String.format("%d drawCalls", Renderer2D.getStatistics().drawCalls));
        ImGui.text(String.format("%d quad Count", Renderer2D.getStatistics().quadCount));
        ImGui.text(String.format("%d vertex Count", Renderer2D.getStatistics().getTotalVertexCount()));
        ImGui.text(String.format("%d index Count", Renderer2D.getStatistics().getTotalIndexCount()));
        ImGui.end();

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        ImGui.begin("Scene");

        viewportFocused = ImGui.isWindowFocused();
        viewportHovered = ImGui.isWindowHovered();
        Application.get().getImGuiLayer().setBlockEvents(!viewportFocused || !viewportHovered);

        ImVec2 viewPortPanelSize = ImGui.getContentRegionAvail();
        ImGui.image(frameBuffer.getColorAttachment(), viewPortPanelSize.x, viewPortPanelSize.y, 0, 1, 1, 0);
        if (!Objects.equals(viewPortSize, viewPortPanelSize) && viewPortPanelSize.x > 0 && viewPortPanelSize.y > 0) {
            frameBuffer.resize((int) viewPortPanelSize.x, (int) viewPortPanelSize.y);
            viewPortSize = viewPortPanelSize;
            cameraController.onResize(viewPortPanelSize.x, viewPortPanelSize.y);
        }

        ImGui.end();
        ImGui.popStyleVar();
    }

    @Override
    public void onEvent(Event event) {
        cameraController.onEvent(event);
    }
}
