package org.lime.core.renderer.buffers;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.buffers.OpenGLFrameBuffer;

public interface FrameBuffer {

    static FrameBuffer create(Specification specification) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLFrameBuffer(specification);
        };
    }

    static Specification createSpec(int width, int height) {
        return new Specification(width, height);
    }

    Specification getSpecification();

    int getColorAttachment();

    void shutdown();

    void bind();

    void unbind();

    void invalidate();

    void resize(int width, int height);

    class Specification {
        public int width;
        public int height;
        public int samples = 1;
        public boolean swapChainTarget = false;

        public Specification(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
