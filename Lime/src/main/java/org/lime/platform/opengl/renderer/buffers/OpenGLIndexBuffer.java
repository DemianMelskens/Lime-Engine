package org.lime.platform.opengl.renderer.buffers;

import lombok.Getter;
import org.lime.core.renderer.buffers.IndexBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL45.glCreateBuffers;

public class OpenGLIndexBuffer extends IndexBuffer {
    private int rendererId;
    @Getter
    private int count;

    public OpenGLIndexBuffer(int[] indices) {
        super(indices);
    }

    @Override
    protected void init(int[] indices) {
        this.count = indices.length;
        this.rendererId = glCreateBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    @Override
    public void Bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
    }

    @Override
    public void Unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void tearDown() {
        glDeleteBuffers(rendererId);
    }
}
