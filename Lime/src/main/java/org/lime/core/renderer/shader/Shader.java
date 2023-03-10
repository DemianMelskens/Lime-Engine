package org.lime.core.renderer.shader;

import lombok.Getter;
import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.OpenGLShader;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public abstract class Shader {

    @Getter
    private String name;

    public static Shader create(String filePath) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLShader(filePath);
        };
    }

    public static Shader create(String name, String vertexSource, String fragmentSource) {
        return switch (Renderer.getAPI()) {
            case Open_GL -> new OpenGLShader(name, vertexSource, fragmentSource);
        };
    }

    protected Shader(String name) {
        if (isValidPath(name)) {
            this.name = new File(name).getName().split("\\.")[0];
            return;
        }
        this.name = name;
    }

    public abstract void bind();

    public abstract void unbind();

    public abstract void tearDown();

    private boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}
