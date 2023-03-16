package org.lime.core.renderer.shader;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.renderer.Renderer;
import org.lime.platform.opengl.renderer.OpenGLShader;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public abstract class Shader {

    @Getter
    protected String name;

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

    public abstract void setInt(String name, int value);
    public abstract void setFloat3(String name, Vector3f value);

    public abstract void setFloat4(String name, Vector4f value);

    public abstract void setMat4(String name, Matrix4f value);

    private boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}
