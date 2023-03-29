package org.lime.core;

import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.events.application.WindowResizeEvent;
import org.lime.core.imgui.ImGuiLayer;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.RendererAPI;
import org.lime.core.time.Time;
import org.lime.core.time.TimeStep;

import java.util.ListIterator;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private boolean isMinimized;
    private LayerStack layerStack;
    private Window window;
    private ImGuiLayer imGuiLayer;
    private float lastFrameTime;


    public static Application get() {
        return instance;
    }

    protected Application() {
        this("Lime Engine");
    }

    protected Application(String name) {
        RendererAPI.setType(RendererAPI.Type.Open_GL);
        LM_CORE_ASSERT(instance == null, "There can only be one application instance");
        instance = this;
        this.isRunning = false;
        this.layerStack = new LayerStack();
        this.window = Window.create(name);
        this.window.setEventCallback(this::onEvent);

        Renderer.init();

        this.imGuiLayer = new ImGuiLayer();
        pushOverlay(imGuiLayer);
    }

    public static Window getWindow() {
        return get().window;
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            float time = Time.getTime();
            TimeStep timestep = new TimeStep(time - lastFrameTime);
            lastFrameTime = time;

            if (!isMinimized) {
                for (Layer layer : layerStack)
                    layer.onUpdate(timestep);

                imGuiLayer.begin();
                for (Layer layer : layerStack)
                    layer.onImGuiRender();
                imGuiLayer.end();
            }

            window.onUpdate();
        }
    }

    public void pushLayer(Layer layer) {
        layerStack.pushLayer(layer);
        layer.onAttach();
    }

    public void pushOverlay(Layer layer) {
        layerStack.pushOverlay(layer);
        layer.onAttach();
    }

    public ImGuiLayer getImGuiLayer() {
        return imGuiLayer;
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(this::onWindowCloseEvent);
        dispatcher.dispatch(this::onWindowResizedEvent);

        ListIterator<Layer> iterator = layerStack.reverseIterator();
        while (iterator.hasPrevious()) {
            Layer layer = iterator.previous();
            layer.onEvent(event);
            if (event.isHandled)
                break;
        }
    }

    private boolean onWindowCloseEvent(WindowCloseEvent event) {
        this.shutdown();
        return true;
    }

    private boolean onWindowResizedEvent(WindowResizeEvent event) {
        if (event.getWidth() == 0 || event.getHeight() == 0) {
            isMinimized = true;
            return false;
        }

        isMinimized = false;
        Renderer.onWindowResizedEvent(event);
        return false;
    }

    public void shutdown() {
        window.shutdown();
        layerStack.forEach(Layer::onDetach);
        Renderer.shutdown();
        isRunning = false;
    }
}
