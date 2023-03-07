package org.lime.platform.opengl.renderer;

import org.lime.core.renderer.RendererAPI;
import org.lime.core.renderer.VertexArray;

import static org.lwjgl.opengl.GL46.*;

public class OpenGLRendererAPI extends RendererAPI {
    @Override
    public void setClearColor(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void drawIndexed(VertexArray vertexArray) {
        glDrawElements(GL_TRIANGLES, vertexArray.getIndexBuffer().getCount(), GL_UNSIGNED_INT, 0L);
    }
}
