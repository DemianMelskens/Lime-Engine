package org.lime.core.renderer.buffers;

import lombok.Getter;
import lombok.Setter;
import org.lime.core.renderer.shader.ShaderDataType;

@Getter
public class BufferElement {

    private final String name;
    private final ShaderDataType type;
    @Setter
    private int offset;
    @Setter
    private int size;
    private final boolean normalized;

    public static BufferElement of(ShaderDataType type, String name) {
        return BufferElement.of(type, name, false);
    }

    public static BufferElement of(ShaderDataType type, String name, boolean normalized) {
        return new BufferElement(type, name, normalized);
    }

    private BufferElement(ShaderDataType type, String name, boolean normalized) {
        this.name = name;
        this.type = type;
        this.size = type.getSize();
        this.normalized = normalized;
    }

    public int getComponentCount() {
        return switch (type) {
            case Bool -> 1;
            case Int -> 1;
            case Int2 -> 2;
            case Int3 -> 3;
            case Int4 -> 4;
            case Float -> 1;
            case Float2 -> 2;
            case Float3 -> 3;
            case Float4 -> 4;
            case Mat3 -> 3 * 3;
            case Mat4 -> 4 * 4;
        };
    }
}
