package org.lime.platform.opengl.renderer.buffers;

import lombok.Getter;
import org.lime.core.renderer.buffers.IndexBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL45.glCreateBuffers;

public class OpenGLIndexBuffer implements IndexBuffer {
    private final int rendererId;
    @Getter
    private final int count;

    public OpenGLIndexBuffer(int[] indices) {
        this.count = indices.length;
        this.rendererId = glCreateBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void shutdown() {
        glDeleteBuffers(rendererId);
    }
}
