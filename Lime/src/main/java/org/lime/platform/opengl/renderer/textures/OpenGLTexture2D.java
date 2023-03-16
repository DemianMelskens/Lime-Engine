package org.lime.platform.opengl.renderer.textures;

import org.lime.core.Image;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.utils.Tuple;

import java.nio.ByteBuffer;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;
import static org.lwjgl.opengl.GL46.*;

public class OpenGLTexture2D implements Texture2D {
    private int rendererId;
    private String path;
    private final int width;
    private final int height;
    private int internalFormat;
    private int dataFormat;

    public OpenGLTexture2D(int width, int height) {
        this.width = width;
        this.height = height;
        determineFormats(4);
        init();
    }

    public OpenGLTexture2D(String path) {
        Image image = new Image(path);

        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();
        determineFormats(image.getChannels());
        init();

        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, image.getData());
        image.free();
    }

    private void init() {
        rendererId = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(rendererId, 1, internalFormat, width, height);

        glTextureParameteri(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTextureParameteri(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureParameteri(rendererId, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTextureParameteri(rendererId, GL_TEXTURE_WRAP_T, GL_REPEAT);
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
    public void setData(ByteBuffer data) {
        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private void determineFormats(int channels) {
        Tuple<Integer, Integer> formats = switch (channels) {
            case 3 -> Tuple.of(GL_RGB8, GL_RGB);
            case 4 -> Tuple.of(GL_RGBA8, GL_RGBA);
            default -> throw LM_CORE_EXCEPTION("Format not supported");
        };
        this.internalFormat = formats.getLeft();
        this.dataFormat = formats.getRight();
    }
}
