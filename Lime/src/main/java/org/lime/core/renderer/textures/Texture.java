package org.lime.core.renderer.textures;

import java.nio.ByteBuffer;

public interface Texture {

    void bind();

    void bind(int slot);

    void setData(ByteBuffer data);

    int getWidth();

    int getHeight();
}
