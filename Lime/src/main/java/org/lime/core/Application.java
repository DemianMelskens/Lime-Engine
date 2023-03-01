package org.lime.core;

import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.imgui.ImGuiLayer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.opengl.GL11C.*;

public class Application {
    private static Application instance;
    private boolean isRunning;
    private LayerStack layerStack;
    private Window window;
    private ImGuiLayer imGuiLayer;

    public static Application getInstance() {
        return instance;
    }

    public Application() {
        LM_CORE_ASSERT(instance == null, "There can only be one application instance");
        instance = this;
        this.isRunning = false;
        this.layerStack = new LayerStack();
        this.window = Window.create();
        this.window.setEventCallback(this::onEvent);
        this.imGuiLayer = new ImGuiLayer();
        pushOverlay(imGuiLayer);
    }

    public Window getWindow() {
        return window;
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            glClearColor(0, 1, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT);

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
