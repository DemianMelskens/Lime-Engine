package org.lime.platform.opengl.buffers;

import org.lime.core.renderer.buffers.VertexBuffer;

import static org.lwjgl.opengl.GL46.*;

public class OpenGLVertexBuffer extends VertexBuffer {

    private int rendererId;

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
    public void Bind() {
        glBindBuffer(GL_ARRAY_BUFFER, rendererId);
    }

    @Override
    public void Unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void tearDown() {
        glDeleteBuffers(rendererId);
    }
}
