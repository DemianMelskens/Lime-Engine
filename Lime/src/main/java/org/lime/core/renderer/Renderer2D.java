package org.lime.core.renderer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.renderer.shader.Shader;
import org.lime.core.renderer.shader.ShaderDataType;
import org.lime.platform.opengl.renderer.OpenGLShader;

public class Renderer2D {

    private static Data data;

    public static void init() {
        data = new Data();
        data.quadVertexArray = VertexArray.create();

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
        data.quadVertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2, 2, 3, 0};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        data.quadVertexArray.setIndexBuffer(indexBuffer);

        data.flatColorShader = Shader.create("/shaders/FlatColor.glsl");
    }

    public static void tearDown() {
        data.delete();
    }

    public static void beginScene(final OrthographicCamera camera) {
        data.flatColorShader.bind();
        ((OpenGLShader) data.flatColorShader).uploadUniformMat4("u_ViewProjection", camera.getViewProjectionMatrix());
        ((OpenGLShader) data.flatColorShader).uploadUniformMat4("u_Transform", new Matrix4f());
    }

    public static void endScene() {
    }

    public static void drawQuad(Vector2f position, Vector2f size, Vector4f color) {
        drawQuad(new Vector3f(position, 0.0f), size, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        data.flatColorShader.bind();
        ((OpenGLShader) data.flatColorShader).uploadUniformFloat4("u_Color", color);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    @NoArgsConstructor
    private static class Data {
        public VertexArray quadVertexArray;
        public Shader flatColorShader;

        public void delete() {
            this.quadVertexArray.tearDown();
            this.quadVertexArray = null;
            this.flatColorShader.tearDown();
            this.flatColorShader = null;
        }
    }
}
