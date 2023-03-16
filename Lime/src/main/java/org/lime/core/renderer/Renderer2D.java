package org.lime.core.renderer;

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
import org.lime.core.renderer.textures.Texture2D;

public class Renderer2D {

    private static Data data;

    private Renderer2D() {
    }

    public static void init() {
        data = new Data();
        data.quadVertexArray = VertexArray.create();

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.0f, 0.0f, 1.0f
        };

        VertexBuffer vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float2, "a_TexCoord")
        );
        vertexBuffer.setLayout(layout);
        data.quadVertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2, 2, 3, 0};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        data.quadVertexArray.setIndexBuffer(indexBuffer);

        data.whiteTexture = Texture2D.create(1, 1);
        data.whiteTexture.setData(Color.white().getBuffer());

        data.textureShader = Shader.create("/shaders/Texture.glsl");
        data.textureShader.bind();
        data.textureShader.setInt("u_Texture", 0);
    }

    public static void tearDown() {
        data.delete();
    }

    public static void beginScene(final OrthographicCamera camera) {
        data.textureShader.bind();
        data.textureShader.setMat4("u_ViewProjection", camera.getViewProjectionMatrix());
    }

    public static void endScene() {
    }

    public static void drawQuad(Vector2f position, Vector2f size, Vector4f color) {
        drawQuad(new Vector3f(position, 0.0f), size, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        data.textureShader.setFloat4("u_Color", color);
        data.whiteTexture.bind();

        Matrix4f transform = new Matrix4f().translate(position).scale(new Vector3f(size, 1.0f));
        data.textureShader.setMat4("u_Transform", transform);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    public static void drawQuad(Vector2f position, Vector2f size, Texture2D texture) {
        drawQuad(new Vector3f(position, 0.0f), size, texture);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture) {
        data.textureShader.setFloat4("u_Color", Color.white().getValue());
        texture.bind();

        Matrix4f transform = new Matrix4f().translate(position).scale(new Vector3f(size, 1.0f));
        data.textureShader.setMat4("u_Transform", transform);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    public static void drawQuad(Vector2f position, Vector2f size, Texture2D texture, Vector4f color) {
        drawQuad(new Vector3f(position, 0.0f), size, texture, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, Vector4f color) {
        data.textureShader.setFloat4("u_Color", color);
        texture.bind();

        Matrix4f transform = new Matrix4f().translate(position).scale(new Vector3f(size, 1.0f));
        data.textureShader.setMat4("u_Transform", transform);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    @NoArgsConstructor
    private static class Data {
        public VertexArray quadVertexArray;
        public Shader textureShader;
        public Texture2D whiteTexture;

        public void delete() {
            this.quadVertexArray.tearDown();
            this.quadVertexArray = null;
            this.textureShader.tearDown();
            this.textureShader = null;
        }
    }
}
