package org.lime.sandbox2d;

import org.lime.core.Application;
import org.lime.debug.Profiler;

public class Sandbox2D extends Application {

    public Sandbox2D() {
        super();
        pushLayer(new ExampleLayer());
    }

    public static void main(String[] args) {
        Profiler.startOutput();
        Application app = new Sandbox2D();
        app.run();
        Profiler.stopOutput();
    }
}
