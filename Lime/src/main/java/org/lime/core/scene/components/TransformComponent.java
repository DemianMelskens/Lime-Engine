package org.lime.core.scene.components;

import org.joml.Matrix4f;

public class TransformComponent {

    public Matrix4f transform;

    public TransformComponent() {
        this.transform = new Matrix4f();
    }

    public TransformComponent(Matrix4f transform) {
        this.transform = transform;
    }
}
