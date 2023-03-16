package org.lime.sandbox2d;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lime.core.Layer;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.Color;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.time.TimeStep;

public class ExampleLayer extends Layer {
    private OrthographicCameraController cameraController;
    private Color color;

    private Texture2D checkerBoardTexture;

    public ExampleLayer() {
        super("Example");
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
        this.color = Color.create(0.8f, 0.2f, 0.3f, 1.0f);
        this.checkerBoardTexture = Texture2D.create("/textures/Checkerboard.png");
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        cameraController.onUpdate(timestep);

        RenderCommand.setClearColor(0.1f, 0.1f, 0.1f, 1f);
        RenderCommand.clear();

        Renderer2D.beginScene(cameraController.getCamera());

        Renderer2D.drawQuad(new Vector2f(-1.0f, 0.0f), new Vector2f(0.8f, 0.8f), color.getValue());
        Renderer2D.drawQuad(new Vector2f(0.5f, -0.5f), new Vector2f(0.5f, 0.75f), checkerBoardTexture, color.getValue());
        Renderer2D.drawQuad(new Vector3f(0.0f, 0.0f, -0.1f), new Vector2f(10.0f, 10.0f), checkerBoardTexture);

        Renderer2D.endScene();
    }

    @Override
    public void onImGuiRender() {
        ImGui.begin("Settings");
        float[] value = new float[]{color.r(), color.g(), color.b(), color.a()};
        if (ImGui.colorEdit4("Color", value)) {
            color.set(value[0], value[1], value[2], value[3]);
        }
        ImGui.end();
    }

    @Override
    public void onEvent(Event event) {
        cameraController.onEvent(event);
    }
}
