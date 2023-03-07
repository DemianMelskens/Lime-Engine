package org.lime.sandbox;

import org.joml.Vector3f;
import org.lime.core.Input;
import org.lime.core.Layer;
import org.lime.core.events.Event;
import org.lime.core.renderer.*;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.time.TimeStep;

import static org.lime.core.KeyCodes.*;

public class ExampleLayer extends Layer {

    private static final float MOVE_SPEED = 4.0f;
    private static final float ROTATION_SPEED = 90f;
    private OrthographicCamera camera;
    private Vector3f cameraPosition;
    private float cameraRotation;
    private VertexArray vertexArray;
    private Shader shader;

    public ExampleLayer() {
        super("Example");
        this.camera = new OrthographicCamera(-1.6f, 1.6f, -0.9f, 0.9f);
        this.cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        vertexArray = VertexArray.create();

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f
        };
        VertexBuffer vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float4, "a_Color")
        );
        vertexBuffer.setLayout(layout);
        vertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        vertexArray.setIndexBuffer(indexBuffer);

        String vertexSource = """
                #version 330 core
                                
                layout(location = 0) in vec3 a_Position;
                layout(location = 1) in vec4 a_Color;

                uniform mat4 u_ViewProjection;
                uniform mat4 u_Transform;
                                               
                out vec3 v_Position;
                out vec4 v_Color;
                                
                void main(){
                    v_Position = a_Position;
                    v_Color = a_Color;
                    gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);    
                }
                """;

        String fragmentSource = """
                #version 330 core
                                
                layout(location = 0) out vec4 color;

                in vec3 v_Position;
                in vec4 v_Color;
                                
                void main(){
                    color = vec4(v_Position + 0.5, 1.0);
                    color= v_Color;    
                }
                """;

        shader = new Shader(vertexSource, fragmentSource);
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
        vertexArray.tearDown();
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

        if (Input.isKeyPressed(LM_KEY_A))
            cameraRotation += (ROTATION_SPEED * timestep.getSeconds());
        if (Input.isKeyPressed(LM_KEY_D))
            cameraRotation -= (ROTATION_SPEED * timestep.getSeconds());

        camera.setPosition(cameraPosition);
        camera.setRotation(cameraRotation);

        Renderer.beginScene(camera);
        Renderer.submit(shader, vertexArray);
        Renderer.endScene();
    }

    @Override
    public void onImGuiRender() {
    }

    @Override
    public void onEvent(Event event) {
    }
}
