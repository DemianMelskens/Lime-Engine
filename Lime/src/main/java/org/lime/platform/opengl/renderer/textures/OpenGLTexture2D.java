package org.lime.platform.opengl.renderer.textures;

import org.lime.core.Image;
import org.lime.core.renderer.textures.Texture2D;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL46.*;

public class OpenGLTexture2D implements Texture2D {
    private int rendererId;
    private String path;
    private int width;
    private int height;

    public OpenGLTexture2D(String path) {
        Image image = new Image(path);

        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();

        rendererId = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(rendererId, 1, GL_RGB8, width, height);

        glTextureParameteri(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTextureParameteri(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, image.getData());

        image.free();
    }

    @Override
    public void bind() {
        bind(0);
    }

    @Override
    public void bind(int slot) {
        glBindTextureUnit(slot, rendererId);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
