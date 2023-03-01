package org.lime.sandbox;

import org.lime.core.Application;

public class Sandbox extends Application {

    public Sandbox() {
        super();
        pushLayer(new ExampleLayer());
    }

    public static void main(String[] args) {
        Application app = new Sandbox();
        app.run();
    }
}
