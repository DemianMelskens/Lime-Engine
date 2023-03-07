package org.lime.core.renderer;

import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.platform.opengl.renderer.OpenGLVertexArray;

import java.util.List;

public abstract class VertexArray {

    public static VertexArray create() {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLVertexArray();
        };
    }

    public abstract void tearDown();

    public abstract void bind();

    public abstract void unbind();

    public abstract void addVertexBuffer(VertexBuffer vertexBuffer);

    public abstract List<VertexBuffer> getVertexBuffers();

    public abstract void setIndexBuffer(IndexBuffer indexBuffer);

    public abstract IndexBuffer getIndexBuffer();
}
