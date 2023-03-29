package org.lime.platform.opengl.renderer.buffers;

import org.lime.core.renderer.buffers.FrameBuffer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Log.LM_CORE_WARN;
import static org.lwjgl.opengl.GL46.*;


public class OpenGLFrameBuffer implements FrameBuffer {
    private static int MAX_FRAME_BUFFER_SIZE = 8192;
    private int rendererId = 0;
    private int colorAttachment = 0;
    private int depthAttachment = 0;
    private final Specification specification;

    public OpenGLFrameBuffer(Specification specification) {
        this.specification = specification;
        invalidate();
    }

    @Override
    public Specification getSpecification() {
        return specification;
    }

    @Override
    public int getColorAttachment() {
        return colorAttachment;
    }

    @Override
    public void shutdown() {
        glDeleteFramebuffers(rendererId);
        glDeleteTextures(colorAttachment);
        glDeleteTextures(depthAttachment);
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, rendererId);
        glViewport(0, 0, specification.width, specification.height);
    }

    @Override
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void invalidate() {
        if (rendererId != 0) {
            shutdown();
        }

        rendererId = glCreateFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, rendererId);

        colorAttachment = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, colorAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, specification.width, specification.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);

        depthAttachment = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, depthAttachment);
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_DEPTH24_STENCIL8, specification.width, specification.height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthAttachment, 0);

        LM_CORE_ASSERT(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE, "Framebuffer is incomplete!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0 || width > MAX_FRAME_BUFFER_SIZE || height > MAX_FRAME_BUFFER_SIZE) {
            LM_CORE_WARN(String.format("Attempted to resize framebuffer to %d, %d", width, height));
            return;
        }

        specification.width = width;
        specification.height = height;
        invalidate();
    }
}
