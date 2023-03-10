package org.lime.core;

import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.imgui.ImGuiLayer;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.RendererAPI;
import org.lime.core.time.Time;
import org.lime.core.time.TimeStep;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private LayerStack layerStack;
    private Window window;
    private ImGuiLayer imGuiLayer;
    private float lastFrameTime;


    public static Application getInstance() {
        return instance;
    }

    protected Application() {
        RendererAPI.setType(RendererAPI.Type.Open_GL);
        LM_CORE_ASSERT(instance == null, "There can only be one application instance");
        instance = this;
        this.isRunning = false;
        this.layerStack = new LayerStack();
        this.window = Window.create();
        this.window.setEventCallback(this::onEvent);

        Renderer.init();

        this.imGuiLayer = new ImGuiLayer();
        pushOverlay(imGuiLayer);
    }

    public Window getWindow() {
        return window;
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            float time = Time.getTime();
            TimeStep timestep = new TimeStep(time - lastFrameTime);
            lastFrameTime = time;

            for (Layer layer : layerStack)
                layer.onUpdate(timestep);

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

        for (Layer layer : layerStack) {
            layer.onEvent(event);
            if (event.isHandled())
                break;
        }
    }

    private boolean onWindowClose(WindowCloseEvent event) {
        this.isRunning = false;
        this.layerStack.forEach(Layer::onDetach);
        return true;
    }
}
