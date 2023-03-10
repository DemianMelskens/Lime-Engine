package org.lime.core.renderer.shader;

import lombok.Getter;

import static org.lwjgl.opengl.GL46.*;

@Getter
public enum ShaderDataType {
    Bool(1),
    Int(4),
    Int2(4 * 2),
    Int3(4 * 3),
    Int4(4 * 4),
    Float(4),
    Float2(4 * 2),
    Float3(4 * 3),
    Float4(4 * 4),
    Mat3(4 * 3 * 3),
    Mat4(4 * 4 * 4);

    private final int size;

    ShaderDataType(int size) {
        this.size = size;
    }

    public static int toOpenGLBaseType(ShaderDataType type) {
        return switch (type) {
            case Bool -> GL_BOOL;
            case Int -> GL_INT;
            case Int2 -> GL_INT;
            case Int3 -> GL_INT;
            case Int4 -> GL_INT;
            case Float -> GL_FLOAT;
            case Float2 -> GL_FLOAT;
            case Float3 -> GL_FLOAT;
            case Float4 -> GL_FLOAT;
            case Mat3 -> GL_FLOAT;
            case Mat4 -> GL_FLOAT;
        };
    }
}
