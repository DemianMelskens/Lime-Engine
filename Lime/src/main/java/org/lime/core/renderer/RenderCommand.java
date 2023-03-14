package org.lime.core.renderer;

public class RenderCommand {

    private RenderCommand() {
    }

    public static void init() {
        RendererAPI.get().init();
    }

    public static void setClearColor(float red, float green, float blue, float alpha) {
        RendererAPI.get().setClearColor(red, green, blue, alpha);
    }

    public static void setViewport(int x, int y, int width, int height) {
        RendererAPI.get().setViewport(x, y, width, height);
    }

    public static void clear() {
        RendererAPI.get().clear();
    }

    public static void drawIndexed(VertexArray vertexArray) {
        RendererAPI.get().drawIndexed(vertexArray);
    }
}
