package org.lime.core.renderer.textures;

public interface Texture {

    void bind();

    void bind(int slot);

    int getWidth();

    int getHeight();
}
