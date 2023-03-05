package org.lime.core.renderer;

public class Renderer {

    public enum API {
        Open_GL
    }

    private static API api;

    public static API getAPI() {
        return api;
    }

    public static void setAPI(API api) {
        Renderer.api = api;
    }
}
