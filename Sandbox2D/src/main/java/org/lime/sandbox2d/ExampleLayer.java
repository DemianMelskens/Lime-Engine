package org.lime.sandbox2d;

import imgui.ImGui;
import org.joml.Vector4f;
import org.lime.core.Layer;
import org.lime.core.controllers.OrthographicCameraController;
import org.lime.core.events.Event;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer;
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
    private VertexArray vertexArray;
    private Shader flatColorShader;
    private Vector4f color;

    public ExampleLayer() {
        super("Example");
    }

    @Override
    public void onAttach() {
        this.cameraController = new OrthographicCameraController(1280.0f / 720.0f, false);
        this.color = new Vector4f(0.2f, 0.3f, 0.8f, 1.0f);
        vertexArray = VertexArray.create();

        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f
        };
        VertexBuffer vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position")
        );
        vertexBuffer.setLayout(layout);
        vertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2, 2, 3, 0};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        vertexArray.setIndexBuffer(indexBuffer);

        flatColorShader = Shader.create("/shaders/FlatColor.glsl");
    }

    @Override
    public void onDetach() {
        vertexArray.tearDown();
        flatColorShader.tearDown();
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        RenderCommand.setClearColor(0.1f, 0.1f, 0.1f, 1f);
        RenderCommand.clear();

        cameraController.onUpdate(timestep);

        Renderer.beginScene(cameraController.getCamera());

        flatColorShader.bind();
        ((OpenGLShader) flatColorShader).uploadUniformFloat4("u_Color", color);
        Renderer.submit(flatColorShader, vertexArray);

        Renderer.endScene();
    }

    @Override
    public void onImGuiRender() {
        ImGui.begin("Properties");
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
