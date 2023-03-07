package org.lime.core.renderer;

public class RenderCommand {

    private static final RendererAPI rendererAPI = RendererAPI.get();

    private RenderCommand() {
    }

    public static void setClearColor(float red, float green, float blue, float alpha) {
        rendererAPI.setClearColor(red, green, blue, alpha);
    }

    public static void clear() {
        rendererAPI.clear();
    }

    public static void drawIndexed(VertexArray vertexArray) {
        rendererAPI.drawIndexed(vertexArray);
    }
}
