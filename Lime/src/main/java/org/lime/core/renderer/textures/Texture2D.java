package org.lime.core.renderer.textures;

import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.textures.OpenGLTexture2D;

public interface Texture2D extends Texture {

    static Texture2D create(String path) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLTexture2D(path);
        };
    }
}
