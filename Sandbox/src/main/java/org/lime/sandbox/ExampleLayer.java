package org.lime.sandbox;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.Input;
import org.lime.core.Layer;
import org.lime.core.events.Event;
import org.lime.core.renderer.*;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.camera.OrthographicCamera;
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
    private Shader shader;
    private Texture texture;

    public ExampleLayer() {
        super("Example");
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

        String vertexSource = """
                #version 330 core
                                
                layout(location = 0) in vec3 a_Position;
                layout(location = 1) in vec2 a_TexCoord;

                uniform mat4 u_ViewProjection;
                uniform mat4 u_Transform;
                                               
                out vec2 v_TexCoord;
                                               
                void main(){
                    v_TexCoord = a_TexCoord;
                    gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);    
                }
                """;

        String fragmentSource = """
                #version 330 core
                                
                layout(location = 0) out vec4 color;
                                
                in vec2 v_TexCoord;
                                
                uniform sampler2D u_Texture;

                void main(){
                    color= texture(u_Texture, v_TexCoord);    
                }
                """;

        shader = Shader.create(vertexSource, fragmentSource);
        texture = Texture2D.create("C:\\Users\\Demian Melskens\\IdeaProjects\\Lime-Engine\\Sandbox\\src\\main\\resources\\assets\\textures\\Checkerboard.png");
        ((OpenGLShader) shader).uploadUniformInt("u_Texture", 0);
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
        vertexArray.tearDown();
        shader.tearDown();
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
