package org.lime.core.renderer.buffers.vertices;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lime.core.renderer.shader.ShaderDataType;

import java.util.List;

public class QuadVertex {
    public Vector3f position;
    public Vector4f color;
    public Vector2f textureCoordinate;
    public float textureIndex;
    public float textureTilingFactor;

    public QuadVertex(Vector4f position, Vector4f color, Vector2f textureCoordinate, float textureIndex, float textureTilingFactor) {
        this.position = new Vector3f(position.x, position.y, position.z);
        this.color = color;
        this.textureCoordinate = textureCoordinate;
        this.textureIndex = textureIndex;
        this.textureTilingFactor = textureTilingFactor;
    }

    private static List<ShaderDataType> structure() {
        return List.of(
            ShaderDataType.Float3,
            ShaderDataType.Float4,
            ShaderDataType.Float2,
            ShaderDataType.Float,
            ShaderDataType.Float
        );
    }

    public static int getBytes() {
        return structure().stream().map(ShaderDataType::getBytes).reduce(0, Integer::sum);
    }

    public static int getSize() {
        return structure().stream().map(ShaderDataType::getSize).reduce(0, Integer::sum);
    }

    public void getData(float[] target, int offset) {
        target[offset] = position.x;
        target[offset + 1] = position.y;
        target[offset + 2] = position.z;
        target[offset + 3] = color.x;
        target[offset + 4] = color.y;
        target[offset + 5] = color.z;
        target[offset + 6] = color.w;
        target[offset + 7] = textureCoordinate.x;
        target[offset + 8] = textureCoordinate.y;
        target[offset + 9] = textureIndex;
        target[offset + 10] = textureTilingFactor;
    }
}
