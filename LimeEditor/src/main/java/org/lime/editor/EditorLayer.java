package org.lime.editor;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImGuiWindowClass;
import imgui.ImVec2;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lime.core.Application;
import org.lime.core.Layer;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.Color;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.buffers.FrameBuffer;
import org.lime.core.renderer.textures.SubTexture2D;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.time.TimeStep;

import java.util.Objects;

import static org.lime.core.utils.Log.LM_CORE_WARN;

public class EditorLayer extends Layer {
    private OrthographicCameraController cameraController;
    private Color color;
    private Texture2D checkerBoardTexture;
    private Texture2D spriteSheet;
    private FrameBuffer frameBuffer;
    private SubTexture2D chairSprite;
    private ImVec2 viewPortSize;
    private boolean viewportFocused = false;
    private boolean viewportHovered = false;

    public EditorLayer() {
        super("Example");
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
        this.color = Color.create(0.8f, 0.2f, 0.3f, 1.0f);
    }

    @Override
    public void onAttach() {
        this.checkerBoardTexture = Texture2D.create("/textures/Checkerboard.png");
        this.spriteSheet = Texture2D.create("/textures/RPG_sheet.png");
        this.chairSprite = SubTexture2D.create(spriteSheet, new Vector2f(7.0f, 6.0f), new Vector2f(128.0f, 128.0f));
        FrameBuffer.Specification specification = FrameBuffer.createSpec(
                Application.getWindow().getWidth(),
                Application.getWindow().getHeight()
        );
        this.frameBuffer = FrameBuffer.create(specification);
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
        Renderer2D.drawQuad(new Vector3f(-1.0f, 0.0f, 0.0f), new Vector2f(0.8f, 0.8f), color.getValue());
        Renderer2D.drawQuad(new Vector3f(0.5f, -0.5f, 0.0f), new Vector2f(0.5f, 0.75f), Color.blue().getValue());
        Renderer2D.drawQuad(new Vector3f(0.0f, 0.0f, -0.1f), new Vector2f(20.0f, 20.0f), checkerBoardTexture, 10.0f);

        for (float y = -5.0f; y < 5.0f; y += 0.5f) {
            for (float x = -5.0f; x < 5.0f; x += 0.5f) {
                Renderer2D.drawQuad(
                        new Vector3f(x, y, 0.0f),
                        new Vector2f(0.45f, 0.45f),
                        Color.create((x + 5.0f) / 10.0f, 0.4f, (y + 5.0f) / 10.0f, 0.7f).getValue()
                );
            }
        }
        Renderer2D.endScene();

        Renderer2D.beginScene(cameraController.getCamera());
        Renderer2D.drawQuad(new Vector3f(0.0f, 0.0f, 0.1f), new Vector2f(1.0f, 1.0f), chairSprite);
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
                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }

        ImGui.dockSpace(ImGui.getID("Dockspace"));

        ImGui.end();

        ImGui.begin("Settings");
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

        ImGuiWindowClass viewPortClass = new ImGuiWindowClass();
        viewPortClass.addDockNodeFlagsOverrideSet(ImGuiDockNodeFlags.AutoHideTabBar);
        ImGui.setNextWindowClass(viewPortClass);
        ImGui.begin("Viewport");

        viewportFocused = ImGui.isWindowFocused();
        viewportHovered = ImGui.isWindowHovered();
        Application.get().getImGuiLayer().setBlockEvents(!viewportFocused || !viewportHovered);

        ImVec2 viewPortPanelSize = ImGui.getContentRegionAvail();
        if (!Objects.equals(viewPortSize, viewPortPanelSize) && viewPortPanelSize.x > 0 && viewPortPanelSize.y > 0) {
            frameBuffer.resize((int) viewPortPanelSize.x, (int) viewPortPanelSize.y);
            viewPortSize = viewPortPanelSize;
            cameraController.onResize(viewPortPanelSize.x, viewPortPanelSize.y);
        }
        ImGui.image(frameBuffer.getColorAttachment(), viewPortSize.x, viewPortSize.y, 0, 1, 1, 0);

        ImGui.end();
        ImGui.popStyleVar();
    }

    @Override
    public void onEvent(Event event) {
        cameraController.onEvent(event);
    }
}
