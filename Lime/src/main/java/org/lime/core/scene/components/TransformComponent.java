package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@EqualsAndHashCode
public class TransformComponent {

    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);

    public TransformComponent() {
    }

    public TransformComponent(Vector3f position) {
        this.position = position;
    }

    public Matrix4f getTransform() {
        return new Matrix4f()
            .translate(position)
            .rotateXYZ(rotation)
            .scale(scale);
    }
}
