package org.lime.platform.opengl.renderer;

import org.lime.core.renderer.ShaderDataType;
import org.lime.core.renderer.VertexArray;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;

import java.util.ArrayList;
import java.util.List;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class OpenGLVertexArray extends VertexArray {
    private int rendererId;
    private List<VertexBuffer> vertexBuffers;
    private IndexBuffer indexBuffer;

    public OpenGLVertexArray() {
        rendererId = glGenVertexArrays();
        vertexBuffers = new ArrayList<>();
    }

    @Override
    public void bind() {
        glBindVertexArray(rendererId);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public void addVertexBuffer(VertexBuffer vertexBuffer) {
        glBindVertexArray(rendererId);
        vertexBuffer.bind();

        LM_CORE_ASSERT(!vertexBuffer.getLayout().getElements().isEmpty(), "Vertex Buffer has no layout!");

        int index = 0;
        for (BufferElement element : vertexBuffer.getLayout()) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(
                    index,
                    element.getComponentCount(),
                    ShaderDataType.toOpenGLBaseType(element.getType()),
                    element.isNormalized(),
                    vertexBuffer.getLayout().getStride(),
                    element.getOffset()
            );
            index++;
        }

        vertexBuffers.add(vertexBuffer);
    }

    @Override
    public List<VertexBuffer> getVertexBuffers() {
        return vertexBuffers;
    }

    @Override
    public void setIndexBuffer(IndexBuffer indexBuffer) {
        glBindVertexArray(rendererId);
        indexBuffer.Bind();

        this.indexBuffer = indexBuffer;
    }

    @Override
    public IndexBuffer getIndexBuffer() {
        return indexBuffer;
    }
}
