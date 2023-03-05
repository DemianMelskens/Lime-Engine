package org.lime.core.renderer;

public class Renderer {

    public enum API {
        Open_GL
    }

    private static API api;

    public static API getRendererAPI() {
        return api;
    }

    public static void setRendererAPI(API api) {
        Renderer.api = api;
    }
}
