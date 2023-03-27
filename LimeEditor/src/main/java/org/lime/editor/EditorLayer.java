package org.lime.editor;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lime.core.Application;
import org.lime.core.Layer;
import org.lime.core.Window;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.Color;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.buffers.FrameBuffer;
import org.lime.core.renderer.textures.SubTexture2D;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.time.TimeStep;

public class EditorLayer extends Layer {
    private OrthographicCameraController cameraController;
    private Color color;
    private Texture2D checkerBoardTexture;
    private Texture2D spriteSheet;

    private FrameBuffer frameBuffer;
    private SubTexture2D chairSprite;

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
                Application.getInstance().getWindow().getWidth(),
                Application.getInstance().getWindow().getHeight()
        );
        this.frameBuffer = FrameBuffer.create(specification);
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        Renderer2D.resetStatistics();
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

        ImGui.begin("Viewport");
        ImGui.image(frameBuffer.getColorAttachment(), 1280.0f, 720.0f);
        ImGui.end();
    }

    @Override
    public void onEvent(Event event) {
        cameraController.onEvent(event);
    }
}
