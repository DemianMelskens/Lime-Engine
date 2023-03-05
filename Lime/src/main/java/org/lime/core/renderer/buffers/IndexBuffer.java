package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.buffers.OpenGLIndexBuffer;

public abstract class IndexBuffer {

    public IndexBuffer(int[] indices) {
        init(indices);
    }

    protected abstract void init(int[] indices);

    public abstract int getCount();

    public abstract void Bind();

    public abstract void Unbind();

    public static IndexBuffer create(int[] indices) {
        return switch (Renderer.getRendererAPI()) {
            case Open_GL -> new OpenGLIndexBuffer(indices);
        };
    }
}
