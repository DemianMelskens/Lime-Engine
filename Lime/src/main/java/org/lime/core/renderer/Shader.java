package org.lime.core.renderer;

import org.lime.platform.opengl.renderer.OpenGLShader;

public interface Shader {
    static Shader create(String vertexSource, String fragmentSource) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLShader(vertexSource, fragmentSource);
        };
    }

    static Shader create(String filePath) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLShader(filePath);
        };
    }

    void bind();

    void unbind();

    void tearDown();
}
