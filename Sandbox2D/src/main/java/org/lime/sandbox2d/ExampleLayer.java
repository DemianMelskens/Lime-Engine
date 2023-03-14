package org.lime.sandbox2d;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lime.core.Layer;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.VertexArray;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.shader.Shader;
import org.lime.core.renderer.shader.ShaderDataType;
import org.lime.core.time.TimeStep;
import org.lime.platform.opengl.renderer.OpenGLShader;

public class ExampleLayer extends Layer {
    private OrthographicCameraController cameraController;
    private Vector4f color;

    public ExampleLayer() {
        super("Example");
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
        this.color = new Vector4f(0.8f, 0.2f, 0.3f, 1.0f);
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

        Renderer2D.drawQuad(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f), color);

        Renderer2D.endScene();
    }

    @Override
    public void onImGuiRender() {
        ImGui.begin("Settings");
        float[] value = new float[]{color.x, color.y, color.z, color.w};
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
