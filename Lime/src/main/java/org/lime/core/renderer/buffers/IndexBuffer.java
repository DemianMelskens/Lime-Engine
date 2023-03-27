package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.buffers.OpenGLIndexBuffer;

public interface IndexBuffer {

    static IndexBuffer create(int[] indices) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLIndexBuffer(indices);
        };
    }

    void shutdown();

    int getCount();

    void bind();

    void unbind();
}
