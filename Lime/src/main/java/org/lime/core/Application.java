package org.lime.core;

import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.imgui.ImGuiLayer;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.Shader;
import org.lime.core.renderer.ShaderDataType;
import org.lime.core.renderer.VertexArray;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.opengl.GL46.*;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private VertexArray vertexArray;
    private VertexBuffer vertexBuffer;
    private IndexBuffer indexBuffer;
    private Shader shader;
    private LayerStack layerStack;
    private Window window;
    private ImGuiLayer imGuiLayer;

    public static Application getInstance() {
        return instance;
    }

    public Application() {
        Renderer.setAPI(Renderer.API.Open_GL);
        LM_CORE_ASSERT(instance == null, "There can only be one application instance");
        instance = this;
        this.isRunning = false;
        this.layerStack = new LayerStack();
        this.window = Window.create();
        this.window.setEventCallback(this::onEvent);

        this.imGuiLayer = new ImGuiLayer();
        pushOverlay(imGuiLayer);

        vertexArray = VertexArray.create();

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f
        };
        vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = BufferLayout.create(
                BufferElement.of(ShaderDataType.Float3, "a_Position"),
                BufferElement.of(ShaderDataType.Float4, "a_Color")
        );
        vertexBuffer.setLayout(layout);
        vertexArray.addVertexBuffer(vertexBuffer);

        int[] indices = {0, 1, 2};
        indexBuffer = IndexBuffer.create(indices);
        vertexArray.setIndexBuffer(indexBuffer);

        String vertexSource = """
                #version 330 core
                                
                layout(location = 0) in vec3 a_Position;
                layout(location = 1) in vec4 a_Color;
                                
                out vec3 v_Position;
                out vec4 v_Color;
                                
                void main(){
                    v_Position = a_Position;
                    v_Color = a_Color;
                    gl_Position = vec4(a_Position, 1.0);    
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

    public Window getWindow() {
        return window;
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            glClearColor(0.1f, 0.1f, 0.1f, 1f);
            glClear(GL_COLOR_BUFFER_BIT);

            shader.bind();
            vertexArray.bind();
            glDrawElements(GL_TRIANGLES, indexBuffer.getCount(), GL_UNSIGNED_INT, 0L);

            imGuiLayer.begin();
            for (Layer layer : layerStack)
                layer.onImGuiRender();
            imGuiLayer.end();

            window.onUpdate();
        }
    }

    public void pushLayer(Layer layer) {
        layerStack.pushLayer(layer);
    }

    public void pushOverlay(Layer overlay) {
        layerStack.pushOverlay(overlay);
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(this::onWindowClose);

        for (Layer layer : this.layerStack) {
            layer.onEvent(event);
            if (event.isHandled())
                break;
        }
    }

    private boolean onWindowClose(WindowCloseEvent event) {
        this.isRunning = false;
        return true;
    }
}
