package org.lime.core;

import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.imgui.ImGuiLayer;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.Shader;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lwjgl.opengl.GL11;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.opengl.GL46.*;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private int vertexArray;
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
        Renderer.setRendererAPI(Renderer.API.Open_GL);
        LM_CORE_ASSERT(instance == null, "There can only be one application instance");
        instance = this;
        this.isRunning = false;
        this.layerStack = new LayerStack();
        this.window = Window.create();
        this.window.setEventCallback(this::onEvent);

        this.imGuiLayer = new ImGuiLayer();
        pushOverlay(imGuiLayer);

        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f, 0.5f, 0.0f
        };
        vertexBuffer = VertexBuffer.create(vertices);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0L);

        int[] indices = {0, 1, 2};
        indexBuffer = IndexBuffer.create(indices);

        String vertexSource = """
                #version 330 core
                                
                layout(location = 0) in vec3 a_Position;
                                
                out vec3 v_Position;
                                
                void main(){
                    v_Position = a_Position;
                    gl_Position = vec4(a_Position, 1.0);    
                }
                """;

        String fragmentSource = """
                #version 330 core
                                
                layout(location = 0) out vec4 color;

                in vec3 v_Position;
                                
                void main(){
                    color = vec4(v_Position + 0.5, 1.0);    
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
            GL11.glClearColor(0.1f, 0.1f, 0.1f, 1f);
            GL11.glClear(GL_COLOR_BUFFER_BIT);

            shader.bind();
            glBindVertexArray(vertexArray);
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
