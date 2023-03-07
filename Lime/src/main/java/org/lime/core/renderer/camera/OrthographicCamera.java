package org.lime.core.renderer.camera;

import lombok.Getter;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Getter
public class OrthographicCamera {

    private Vector3f position;
    private float rotation;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f viewProjectionMatrix;

    public OrthographicCamera(float left, float right, float bottom, float top) {
        this.position = new Vector3f();
        this.rotation = 0.0f;
        this.projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        this.viewMatrix = new Matrix4f();
        this.viewProjectionMatrix = new Matrix4f();
        reCalculateViewMatrix();
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        reCalculateViewMatrix();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        reCalculateViewMatrix();
    }

    private void reCalculateViewMatrix() {
        Matrix4f transform = new Matrix4f()
                .identity()
                .translate(position)
                .mul(new Matrix4f()
                        .identity()
                        .rotate(Math.toRadians(rotation), new Vector3f(0.0f, 0.0f, 1.0f))
                );

        viewMatrix = transform.invert();
        projectionMatrix.mul(viewMatrix, viewProjectionMatrix);
    }
}
