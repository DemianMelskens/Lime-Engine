package org.lime.core.renderer.buffers.vertices;

import lombok.AllArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.renderer.shader.ShaderDataType;

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

    public float[] getData() {
        return new float[]{
                position.x, position.y, position.z,
                color.x, color.y, color.z, color.w,
                texCoord.x, texCoord.y,
                texIndex
        };
    }
}
