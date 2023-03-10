package org.lime.sandbox;

import org.joml.Vector3f;
import org.lime.core.Input;
import org.lime.core.Layer;
import org.lime.core.events.Event;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.VertexArray;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.renderer.shader.ShaderDataType;
import org.lime.core.renderer.shader.ShaderLibrary;
import org.lime.core.renderer.textures.Texture;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.time.TimeStep;
import org.lime.platform.opengl.renderer.OpenGLShader;

import static org.lime.core.KeyCodes.*;

public class ExampleLayer extends Layer {

    private static final float MOVE_SPEED = 4.0f;
    private OrthographicCamera camera;
    private Vector3f cameraPosition;
    private VertexArray vertexArray;
    private ShaderLibrary shaderLibrary;
    private Texture texture, logoTexture;

    public ExampleLayer() {
        super("Example");
        this.shaderLibrary = new ShaderLibrary();
        this.camera = new OrthographicCamera(-1.6f, 1.6f, -0.9f, 0.9f);
        this.cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        vertexArray = VertexArray.create();

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
        };
        VertexBuffer vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float2, "a_TexCoord")
        );
        vertexBuffer.setLayout(layout);
        vertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2, 2, 3, 0};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        vertexArray.setIndexBuffer(indexBuffer);

        shaderLibrary.load("/shaders/Texture.glsl");
        texture = Texture2D.create("/textures/Checkerboard.png");
        logoTexture = Texture2D.create("/textures/Logo.png");
        shaderLibrary.get(OpenGLShader.class, "Texture").uploadUniformInt("u_Texture", 0);
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
        vertexArray.tearDown();
        shaderLibrary.tearDown();
    }

    @Override
    public void onUpdate(TimeStep timestep) {
        RenderCommand.setClearColor(0.1f, 0.1f, 0.1f, 1f);
        RenderCommand.clear();

        if (Input.isKeyPressed(LM_KEY_LEFT))
            cameraPosition.x -= (MOVE_SPEED * timestep.getSeconds());
        else if (Input.isKeyPressed(LM_KEY_RIGHT))
            cameraPosition.x += (MOVE_SPEED * timestep.getSeconds());
        if (Input.isKeyPressed(LM_KEY_UP))
            cameraPosition.y += (MOVE_SPEED * timestep.getSeconds());
        else if (Input.isKeyPressed(LM_KEY_DOWN))
            cameraPosition.y -= (MOVE_SPEED * timestep.getSeconds());

        camera.setPosition(cameraPosition);

        Renderer.beginScene(camera);

        texture.bind();
        Renderer.submit(shaderLibrary.get("Texture"), vertexArray);

        logoTexture.bind();
        Renderer.submit(shaderLibrary.get("Texture"), vertexArray);

        Renderer.endScene();
    }

    @Override
    public void onImGuiRender() {
    }

    @Override
    public void onEvent(Event event) {
    }
}
