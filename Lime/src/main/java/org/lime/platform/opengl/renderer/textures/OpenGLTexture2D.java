package org.lime.platform.opengl.renderer.textures;

import org.lime.core.Image;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.utils.Tuple;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;
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

        Tuple<Integer, Integer> formats = getFormats(image.getChannels());

        rendererId = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(rendererId, 1, formats.getLeft(), width, height);

        glTextureParameteri(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTextureParameteri(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, formats.getRight(), GL_UNSIGNED_BYTE, image.getData());

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

    private Tuple<Integer, Integer> getFormats(int channels) {
        return switch (channels) {
            case 4 -> Tuple.of(GL_RGBA8, GL_RGBA);
            case 3 -> Tuple.of(GL_RGB8, GL_RGB);
            default -> throw LM_CORE_EXCEPTION("Format not supported");
        };
    }
}
