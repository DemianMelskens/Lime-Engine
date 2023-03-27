package org.lime.platform.opengl.renderer.buffers;

import org.lime.core.renderer.buffers.FrameBuffer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.opengl.GL46.*;


public class OpenGLFrameBuffer implements FrameBuffer {
    private int rendererId;
    private int colorAttachment;
    private int depthAttachment;
    private final Specification specs;

    public OpenGLFrameBuffer(Specification specs) {
        this.specs = specs;
        this.resize();
    }

    @Override
    public Specification getSpecification() {
        return specs;
    }

    @Override
    public int getColorAttachment() {
        return colorAttachment;
    }

    @Override
    public void shutdown() {
        glDeleteFramebuffers(rendererId);
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, rendererId);
    }

    @Override
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void resize() {
        rendererId = glCreateFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, rendererId);

        colorAttachment = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, colorAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, specs.width, specs.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);

        depthAttachment = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, depthAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, specs.width, specs.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthAttachment, 0);

        LM_CORE_ASSERT(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE, "Framebuffer is incomplete!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
