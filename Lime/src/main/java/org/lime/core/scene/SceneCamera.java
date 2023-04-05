package org.lime.core.scene;

import lombok.Getter;
import org.joml.Matrix4f;
import org.lime.core.renderer.camera.Camera;

public class SceneCamera extends Camera {

    @Getter
    private float orthographicSize;
    private float orthographicNear;
    private float orthographicFar;
    private float aspectRatio;

    public SceneCamera() {
        super();
        this.orthographicSize = 10.0f;
        this.orthographicNear = -1.0f;
        this.orthographicFar = 1.0f;
        this.aspectRatio = 0.0f;
        reCalculateProjection();
    }

    public void setOrthographicSize(float size) {
        orthographicSize = size;
        reCalculateProjection();
    }

    public void setOrthographic(float size, float nearClip, float farClip) {
        orthographicSize = size;
        orthographicNear = nearClip;
        orthographicFar = farClip;
        reCalculateProjection();
    }

    public void setViewportSize(int width, int height) {
        aspectRatio = (float) width / (float) height;
        reCalculateProjection();
    }

    private void reCalculateProjection() {
        float orthographicLeft = -orthographicSize * aspectRatio * 0.5f;
        float orthographicRight = orthographicSize * aspectRatio * 0.5f;
        float orthographicBottom = -orthographicSize * 0.5f;
        float orthographicTop = orthographicSize * 0.5f;

        projection = new Matrix4f().ortho(orthographicLeft, orthographicRight,
                orthographicBottom, orthographicTop, orthographicNear, orthographicFar);
    }
}
