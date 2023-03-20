package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.buffers.OpenGLIndexBuffer;

public abstract class IndexBuffer {

    public static IndexBuffer create(int[] indices) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLIndexBuffer(indices);
        };
    }

    public IndexBuffer(int[] indices) {
        init(indices);
    }

    protected abstract void init(int[] indices);

    public abstract void shutdown();

    public abstract int getCount();

    public abstract void bind();

    public abstract void unbind();
}
