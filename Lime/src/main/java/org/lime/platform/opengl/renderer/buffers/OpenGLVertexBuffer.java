package org.lime.platform.opengl.renderer.buffers;

import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.VertexBuffer;

import static org.lwjgl.opengl.GL46.*;

public class OpenGLVertexBuffer extends VertexBuffer {

    private int rendererId;
    private BufferLayout layout;

    public OpenGLVertexBuffer(float[] vertices) {
        super(vertices);
    }

    @Override
    protected void init(float[] vertices) {
        rendererId = glCreateBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
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
    public BufferLayout getLayout() {
        return layout;
    }

    private void tearDown() {
        glDeleteBuffers(rendererId);
    }
}
