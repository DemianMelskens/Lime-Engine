package org.lime.sandbox;

import org.lime.core.Application;
import org.lime.debug.Profiler;

public class Sandbox extends Application {

    public Sandbox() {
        super();
        pushLayer(new ExampleLayer());
    }

    public static void main(String[] args) {
        Profiler.beginSession("runtime", "profiler/runtime.json");

        Application app = new Sandbox();
        app.run();

        Profiler.endSession();
    }
}
