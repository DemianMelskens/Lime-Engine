package org.lime.sandbox;

import imgui.ImGui;
import org.lime.core.Layer;
import org.lime.core.events.Event;

public class ExampleLayer extends Layer {

    public ExampleLayer() {
        super("Example");
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onImGuiRender() {
        ImGui.begin("test");
        ImGui.text("Hello World!");
        ImGui.end();
    }

    @Override
    public void onEvent(Event event) {

    }
}
