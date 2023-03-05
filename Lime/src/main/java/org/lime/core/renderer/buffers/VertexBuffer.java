package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.buffers.OpenGLVertexBuffer;

public abstract class VertexBuffer {

    public VertexBuffer(float[] vertices) {
        init(vertices);
    }

    protected abstract void init(float[] vertices);

    public abstract void Bind();

    public abstract void Unbind();

    public static VertexBuffer create(float[] vertices) {
        return switch (Renderer.getRendererAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(vertices);
        };
    }
}
