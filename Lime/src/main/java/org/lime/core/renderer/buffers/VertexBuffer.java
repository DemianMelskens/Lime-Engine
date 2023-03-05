package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.buffers.OpenGLVertexBuffer;

public abstract class VertexBuffer {

    public VertexBuffer(float[] vertices) {
        init(vertices);
    }

    protected abstract void init(float[] vertices);

    public abstract void bind();

    public abstract void unbind();

    public abstract void setLayout(BufferLayout layout);

    public abstract BufferLayout getLayout();

    public static VertexBuffer create(float[] vertices) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(vertices);
        };
    }
}
