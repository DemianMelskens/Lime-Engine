package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.buffers.vertices.QuadVertex;
import org.lime.platform.opengl.renderer.buffers.OpenGLVertexBuffer;

public abstract class VertexBuffer {

    public static VertexBuffer create(int size) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(size);
        };
    }

    public static VertexBuffer create(float[] vertices) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(vertices);
        };
    }

    public abstract void shutdown();

    public abstract void bind();

    public abstract void unbind();

    public abstract void setLayout(BufferLayout layout);

    public abstract void setData(float[] data);

    public abstract BufferLayout getLayout();
}
