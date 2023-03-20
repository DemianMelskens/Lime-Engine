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
import org.lime.core.renderer.shader.ShaderLibrary;
import org.lime.core.renderer.textures.Texture2D;

public class Renderer2D {

    private static final String TILING_FACTOR = "u_TilingFactor";
    private static final String COLOR = "u_Color";
    private static final String TRANSFORM = "u_Transform";
    private static final String SHADER_NAME = "Texture";
    private static Data data;

    private Renderer2D() {
    }

    public static void init() {
        data = new Data();
        data.shaderLibrary = new ShaderLibrary();
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

        Shader textureShader = data.shaderLibrary.load("/shaders/Texture.glsl");
        textureShader.bind();
        textureShader.setInt("u_Texture", 0);
    }

    public static void shutdown() {
        data.delete();
    }

    public static void beginScene(final OrthographicCamera camera) {
        Shader textureShader = data.shaderLibrary.get(SHADER_NAME);
        textureShader.bind();
        textureShader.setMat4("u_ViewProjection", camera.getViewProjectionMatrix());
    }

    public static void endScene() {
        data.shaderLibrary.get(SHADER_NAME).unbind();
    }

    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        drawQuad(position, size, data.whiteTexture, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture) {
        drawQuad(position, size, texture, Color.white().getValue());
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, float tilingFactor) {
        drawQuad(position, size, texture, Color.white().getValue(), tilingFactor);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, Vector4f color) {
        drawQuad(position, size, texture, color, 1.0f);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, Vector4f color, float tilingFactor) {
        Shader textureShader = data.shaderLibrary.get(SHADER_NAME);
        textureShader.setFloat4(COLOR, color);
        textureShader.setFloat(TILING_FACTOR, tilingFactor);
        texture.bind();

        Matrix4f transform = new Matrix4f()
                .translate(position)
                .scale(new Vector3f(size, 1.0f));
        textureShader.setMat4(TRANSFORM, transform);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Vector4f color) {
        drawRotatedQuad(position, size, radians, data.whiteTexture, color);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture) {
        drawRotatedQuad(position, size, radians, texture, Color.white().getValue());
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, float tilingFactor) {
        drawRotatedQuad(position, size, radians, texture, Color.white().getValue(), tilingFactor);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, Vector4f color) {
        drawRotatedQuad(position, size, radians, texture, color, 1.0f);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, Vector4f color, float tilingFactor) {
        Shader textureShader = data.shaderLibrary.get(SHADER_NAME);
        textureShader.setFloat4(COLOR, color);
        textureShader.setFloat(TILING_FACTOR, tilingFactor);
        texture.bind();

        Matrix4f transform = new Matrix4f()
                .translate(position)
                .rotate(radians, new Vector3f(0.0f, 0.0f, 1.0f))
                .scale(new Vector3f(size, 1.0f));
        textureShader.setMat4(TRANSFORM, transform);

        data.quadVertexArray.bind();
        RenderCommand.drawIndexed(data.quadVertexArray);
    }

    @NoArgsConstructor
    private static class Data {
        public VertexArray quadVertexArray;
        public ShaderLibrary shaderLibrary;
        public Texture2D whiteTexture;

        public void delete() {
            this.quadVertexArray.shutdown();
            this.quadVertexArray = null;
            this.shaderLibrary.shutdown();
            this.shaderLibrary = null;
        }
    }
}
