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
import org.lime.core.renderer.textures.SubTexture2D;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.renderer.textures.TextureSlots;

import java.util.ArrayList;
import java.util.List;

public class Renderer2D {

    private static final String SHADER_NAME = "Texture";
    private static final Data data = new Data();

    private Renderer2D() {
    }

    public static void init() {
        data.shaderLibrary = new ShaderLibrary();
        data.quadVertexArray = VertexArray.create();
        data.textureSlots = TextureSlots.create();

        data.quadVertexBuffer = VertexBuffer.create(Data.MAX_VERTICES * QuadVertex.getBytes());
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float4, "a_Color"),
                BufferElement.of(ShaderDataType.Float2, "a_TexCoord"),
                BufferElement.of(ShaderDataType.Float, "a_TexIndex"),
                BufferElement.of(ShaderDataType.Float, "a_TexTilingFactor")
        );
        data.quadVertexBuffer.setLayout(layout);
        data.quadVertexArray.addVertexBuffer(data.quadVertexBuffer);

        data.quadVertexBase = new ArrayList<>();

        int[] quadIndices = new int[Data.MAX_INDICES];

        int offset = 0;
        for (int i = 0; i < Data.MAX_INDICES; i += 6) {
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
        Shader textureShader = data.shaderLibrary.get(SHADER_NAME);
        textureShader.bind();
        textureShader.setMat4("u_ViewProjection", camera.getViewProjectionMatrix());
    }

    public static void endScene() {
        flush();
        data.shaderLibrary.get(SHADER_NAME).unbind();
    }

    public static void flush() {
        data.quadVertexBuffer.setData(data.getQuadVertexBaseData());

        for (int i = 0; i < data.textureSlots.size(); i++)
            data.textureSlots.get(i).bind(i);

        RenderCommand.drawIndexed(data.quadVertexArray, data.quadIndexCount);
        data.quadVertexBase.clear();
        data.quadIndexCount = 0;
        data.statistics.drawCalls++;
    }

    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        drawQuad(position, size, data.whiteTexture, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture) {
        drawQuad(position, size, texture, Color.white().getValue());
    }

    public static void drawQuad(Vector3f position, Vector2f size, SubTexture2D subTexture) {
        drawQuad(position, size, subTexture, Color.white().getValue());
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, float tilingFactor) {
        drawQuad(position, size, texture, Color.white().getValue(), tilingFactor);
    }

    public static void drawQuad(Vector3f position, Vector2f size, SubTexture2D subTexture, float tilingFactor) {
        drawQuad(position, size, subTexture, Color.white().getValue(), tilingFactor);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, Vector4f color) {
        drawQuad(position, size, texture, color, 1.0f);
    }

    public static void drawQuad(Vector3f position, Vector2f size, SubTexture2D subTexture, Vector4f color) {
        drawQuad(position, size, subTexture, color, 1.0f);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Texture2D texture, Vector4f color, float tilingFactor) {
        Matrix4f transform = new Matrix4f()
                .translate(position)
                .scale(new Vector3f(size, 1.0f));

        drawTransformedQuad(transform, texture, color, tilingFactor);
    }

    public static void drawQuad(Vector3f position, Vector2f size, SubTexture2D subTexture, Vector4f color, float tilingFactor) {
        Matrix4f transform = new Matrix4f()
                .translate(position)
                .scale(new Vector3f(size, 1.0f));

        drawTransformedQuad(transform, subTexture.getTexture(), subTexture.getTextureCoordinates(), color, tilingFactor);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Vector4f color) {
        drawRotatedQuad(position, size, radians, data.whiteTexture, color);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture) {
        drawRotatedQuad(position, size, radians, texture, Color.white().getValue());
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, SubTexture2D subTexture) {
        drawRotatedQuad(position, size, radians, subTexture, Color.white().getValue());
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, float tilingFactor) {
        drawRotatedQuad(position, size, radians, texture, Color.white().getValue(), tilingFactor);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, SubTexture2D subTexture, float tilingFactor) {
        drawRotatedQuad(position, size, radians, subTexture, Color.white().getValue(), tilingFactor);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, Vector4f color) {
        drawRotatedQuad(position, size, radians, texture, color, 1.0f);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, SubTexture2D subTexture, Vector4f color) {
        drawRotatedQuad(position, size, radians, subTexture, color, 1.0f);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, Texture2D texture, Vector4f color, float tilingFactor) {
        Matrix4f transform = new Matrix4f()
                .translate(position)
                .rotate(radians, new Vector3f(0.0f, 0.0f, 1.0f))
                .scale(new Vector3f(size, 1.0f));

        drawTransformedQuad(transform, texture, color, tilingFactor);
    }

    public static void drawRotatedQuad(Vector3f position, Vector2f size, float radians, SubTexture2D subTexture, Vector4f color, float tilingFactor) {
        Matrix4f transform = new Matrix4f()
                .translate(position)
                .rotate(radians, new Vector3f(0.0f, 0.0f, 1.0f))
                .scale(new Vector3f(size, 1.0f));

        drawTransformedQuad(transform, subTexture.getTexture(), subTexture.getTextureCoordinates(), color, tilingFactor);
    }

    private static void drawTransformedQuad(Matrix4f transform, Texture2D texture, Vector4f color, float tilingFactor) {
        drawTransformedQuad(transform, texture, Data.QUAD_TEXTURE_COORDS, color, tilingFactor);
    }

    private static void drawTransformedQuad(Matrix4f transform, Texture2D texture, Vector2f[] textureCoordinates, Vector4f color, float tilingFactor) {
        if (data.quadIndexCount >= Data.MAX_INDICES)
            flush();

        float textureIndex = data.textureSlots.add(texture);

        for (int i = 0; i < 4; i++) {
            data.quadVertexBase.add(new QuadVertex(
                    new Vector4f(Data.QUAD_VERTEX_POSITIONS[i]).mul(transform),
                    color,
                    textureCoordinates[i],
                    textureIndex,
                    tilingFactor
            ));
        }

        data.quadIndexCount += 6;

        data.statistics.quadCount++;
    }

    public static void resetStatistics() {
        data.statistics = new RendererStatistics();
    }

    public static RendererStatistics getStatistics() {
        return data.statistics;
    }

    @NoArgsConstructor
    private static class Data {
        public static final int MAX_QUAD = 20_000;
        public static final int MAX_VERTICES = MAX_QUAD * 4;
        public static final int MAX_INDICES = MAX_QUAD * 6;
        public static final int MAX_TEXTURE_SLOTS = 32;
        public static final Vector2f[] QUAD_TEXTURE_COORDS = new Vector2f[]{
                new Vector2f(0.0f, 0.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(1.0f, 1.0f),
                new Vector2f(0.0f, 1.0f)
        };

        public static final Vector4f[] QUAD_VERTEX_POSITIONS = new Vector4f[]{
                new Vector4f(-0.5f, -0.5f, 0.0f, 1.0f),
                new Vector4f(0.5f, -0.5f, 0.0f, 1.0f),
                new Vector4f(0.5f, 0.5f, 0.0f, 1.0f),
                new Vector4f(-0.5f, 0.5f, 0.0f, 1.0f)
        };

        public VertexArray quadVertexArray;
        public VertexBuffer quadVertexBuffer;
        public ShaderLibrary shaderLibrary;
        public Texture2D whiteTexture;

        public int quadIndexCount;
        public List<QuadVertex> quadVertexBase;
        public TextureSlots textureSlots;

        public RendererStatistics statistics = new RendererStatistics();

        public float[] getQuadVertexBaseData() {
            float[] temp = new float[quadVertexBase.size() * QuadVertex.getSize()];

            for (int i = 0; i < quadVertexBase.size(); i++) {
                quadVertexBase.get(i).getData(temp, i * QuadVertex.getSize());
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
