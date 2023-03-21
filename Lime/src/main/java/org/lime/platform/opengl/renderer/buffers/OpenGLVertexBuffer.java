package org.lime.platform.opengl.renderer.buffers;

import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.buffers.vertices.QuadVertex;

import static org.lwjgl.opengl.GL46.*;

public class OpenGLVertexBuffer extends VertexBuffer {

    private int rendererId;
    private BufferLayout layout;

    public OpenGLVertexBuffer(int size) {
        rendererId = glCreateBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
        glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
    }

    public OpenGLVertexBuffer(float[] data) {
        rendererId = glCreateBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void setLayout(BufferLayout layout) {
        this.layout = layout;
    }

    @Override
    public void setData(float[] data) {
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
    }

    @Override
    public BufferLayout getLayout() {
        return layout;
    }

    @Override
    public void shutdown() {
        glDeleteBuffers(rendererId);
    }
}
