package org.lime.core.renderer.buffers.vertices;

import lombok.AllArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.renderer.shader.ShaderDataType;

import java.util.stream.Stream;

@AllArgsConstructor
public class QuadVertex {
    public Vector3f position;
    public Vector4f color;
    public Vector2f texCoord;
    public float texIndex;

    public static int getSize() {
        return ShaderDataType.Float3.getSize() +
                ShaderDataType.Float4.getSize() +
                ShaderDataType.Float2.getSize() +
                ShaderDataType.Float.getSize();
    }

    public void getData(float[] target, int offset) {
        target[offset] = position.x;
        target[offset + 1] = position.y;
        target[offset + 2] = position.z;
        target[offset + 3] = color.x;
        target[offset + 4] = color.y;
        target[offset + 5] = color.z;
        target[offset + 6] = color.w;
        target[offset + 7] = texCoord.x;
        target[offset + 8] = texCoord.y;
        target[offset + 9] = texIndex;
    }
}
