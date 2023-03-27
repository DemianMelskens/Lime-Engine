package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.buffers.OpenGLVertexBuffer;

public interface VertexBuffer {

    static VertexBuffer create(int size) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(size);
        };
    }

    static VertexBuffer create(float[] vertices) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexBuffer(vertices);
        };
    }

    void shutdown();

    void bind();

    void unbind();

    void setLayout(BufferLayout layout);

    void setData(float[] data);

    BufferLayout getLayout();
}
