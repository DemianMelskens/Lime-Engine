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
import org.lime.core.renderer.buffers.vertices.QuadVertex;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.renderer.shader.Shader;
import org.lime.core.renderer.shader.ShaderDataType;
import org.lime.core.renderer.shader.ShaderLibrary;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.renderer.textures.TextureSlots;

import java.util.ArrayList;
import java.util.List;

public class Renderer2D {

    private static final String TILING_FACTOR = "u_TilingFactor";
    private static final String COLOR = "u_Color";
    private static final String TRANSFORM = "u_Transform";
    private static final String SHADER_NAME = "Texture";
    private static final Data data = new Data();

    private Renderer2D() {
    }

    public static void init() {
        data.shaderLibrary = new ShaderLibrary();
        data.quadVertexArray = VertexArray.create();
        data.textureSlots = TextureSlots.create();

        data.quadVertexBuffer = VertexBuffer.create(data.maxVertices * QuadVertex.getSize());
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float4, "a_Color"),
                BufferElement.of(ShaderDataType.Float2, "a_TexCoord"),
                BufferElement.of(ShaderDataType.Float, "a_TexIndex")
        );
        data.quadVertexBuffer.setLayout(layout);
        data.quadVertexArray.addVertexBuffer(data.quadVertexBuffer);

        data.quadVertexBase = new ArrayList<>();

        int[] quadIndices = new int[data.maxIndices];

        int offset = 0;
        for (int i = 0; i < data.maxIndices; i += 6) {
            quadIndices[i] = offset;
            quadIndices[i + 1] = offset + 1;
            quadIndices[i + 2] = offset + 2;

            quadIndices[i + 3] = offset + 2;
            quadIndices[i + 4] = offset + 3;
            quadIndices[i + 5] = offset;

            offset += 4;
        }

        IndexBuffer indexBuffer = IndexBuffer.create(quadIndices);
        data.quadVertexArray.setIndexBuffer(indexBuffer);

        data.whiteTexture = Texture2D.create(1, 1);
        data.whiteTexture.setData(Color.white().getBuffer());

        int[] samplers = new int[TextureSlots.MAX_SLOTS];
        for (int i = 0; i < TextureSlots.MAX_SLOTS; i++)
            samplers[i] = i;

        Shader textureShader = data.shaderLibrary.load("/shaders/Texture.glsl");
        textureShader.bind();
        textureShader.setIntArray("u_Textures", samplers);

        data.textureSlots.add(data.whiteTexture);
    }

    public static void shutdown() {
        data.delete();
    }

    public static void beginScene(final OrthographicCamera camera) {
        data.quadVertexBase.clear();
        Shader textureShader = data.shaderLibrary.get(SHADER_NAME);
        textureShader.bind();
        textureShader.setMat4("u_ViewProjection", camera.getViewProjectionMatrix());
    }

    public static void endScene() {
        data.quadVertexBuffer.setData(data.getQuadVertexBaseData());
        flush();

        data.shaderLibrary.get(SHADER_NAME).unbind();
    }

    public static void flush() {
        for (int i = 0; i < data.textureSlots.size(); i++)
            data.textureSlots.get(i).bind(i);

        RenderCommand.drawIndexed(data.quadVertexArray, data.quadIndexCount);
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
        float textureIndex = data.textureSlots.add(texture);

        data.quadVertexBase.add(new QuadVertex(
                position,
                color,
                new Vector2f(0.0f, 0.0f),
                textureIndex
        ));

        data.quadVertexBase.add(new QuadVertex(
                new Vector3f(position.x + size.x, position.y, 0.0f),
                color,
                new Vector2f(1.0f, 0.0f),
                textureIndex
        ));

        data.quadVertexBase.add(new QuadVertex(
                new Vector3f(position.x + size.x, position.y + size.y, 0.0f),
                color,
                new Vector2f(1.0f, 1.0f),
                textureIndex
        ));

        data.quadVertexBase.add(new QuadVertex(
                new Vector3f(position.x, position.y + size.y, 0.0f),
                color,
                new Vector2f(0.0f, 1.0f),
                textureIndex
        ));

        data.quadIndexCount += 6;
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
        public final int maxQuad = 10000;
        public final int maxVertices = maxQuad * 4;
        public final int maxIndices = maxQuad * 6;
        public final int maxTextureSlots = 32;

        public VertexArray quadVertexArray;
        public VertexBuffer quadVertexBuffer;
        public ShaderLibrary shaderLibrary;
        public Texture2D whiteTexture;

        public int quadIndexCount;
        public List<QuadVertex> quadVertexBase;
        public int quadVertexBasePtr = 0;
        public TextureSlots textureSlots;

        public float[] getQuadVertexBaseData() {
            float[] temp = new float[quadVertexBase.size() * QuadVertex.getSize()];

            for (int i = 0; i < quadVertexBase.size(); i++) {
                quadVertexBase.get(i).getData(temp, i * 10);
            }

            return temp;
        }

        public void delete() {
            this.quadVertexArray.shutdown();
            this.quadVertexArray = null;
            this.shaderLibrary.shutdown();
            this.shaderLibrary = null;
        }
    }
}
