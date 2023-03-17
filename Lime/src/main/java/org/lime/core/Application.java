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
import org.lime.debug.Profiler;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private boolean isMinimized;
    private LayerStack layerStack;
    private Window window;
    private ImGuiLayer imGuiLayer;
    private float lastFrameTime;


    public static Application getInstance() {
        return instance;
    }

    protected Application() {
        Profiler.beginSession("startUp", "profiler/startup.json");
        Profiler.startProfile("startUp");
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
        Profiler.stopProfile("startUp");
        Profiler.endSession();
    }

    public Window getWindow() {
        return window;
    }

    public void run() {
        Profiler.beginSession("runtime", "profiler/runtime.json");
        isRunning = true;

        while (isRunning) {
            Profiler.startProfile("onUpdate");
            float time = Time.getTime();
            TimeStep timestep = new TimeStep(time - lastFrameTime);
            lastFrameTime = time;

            if (!isMinimized) {
                for (Layer layer : layerStack)
                    layer.onUpdate(timestep);
            }

            imGuiLayer.begin();
            for (Layer layer : layerStack)
                layer.onImGuiRender();
            imGuiLayer.end();

            window.onUpdate();
            Profiler.stopProfile("onUpdate");
        }
        Profiler.endSession();
    }

    public void pushLayer(Layer layer) {
        layerStack.pushLayer(layer);
    }

    public void pushOverlay(Layer overlay) {
        layerStack.pushOverlay(overlay);
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(this::onWindowCloseEvent);
        dispatcher.dispatch(this::onWindowResizedEvent);

        for (Layer layer : layerStack) {
            layer.onEvent(event);
            if (event.isHandled())
                break;
        }
    }

    private boolean onWindowCloseEvent(WindowCloseEvent event) {
        this.isRunning = false;
        this.layerStack.forEach(Layer::onDetach);
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
}
