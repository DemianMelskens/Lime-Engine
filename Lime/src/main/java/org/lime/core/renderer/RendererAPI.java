package org.lime.core.renderer;

import org.lime.platform.opengl.renderer.OpenGLRendererAPI;

public abstract class RendererAPI {

    private static RendererAPI instance;
    private static Type type;

    public static RendererAPI get() {
        if (instance == null) {
            instance = switch (getType()) {
                case Open_GL -> new OpenGLRendererAPI();
            };
        }
        return instance;
    }

    public abstract void init();

    public static Type getType() {
        return type;
    }

    public static void setType(Type type) {
        RendererAPI.type = type;
    }

    public abstract void setClearColor(float red, float green, float blue, float alpha);

    public abstract void setViewport(int x, int y, int width, int height);

    public abstract void clear();

    public abstract void drawIndexed(VertexArray vertexArray);

    public enum Type {
        Open_GL
    }
}
