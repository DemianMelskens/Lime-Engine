package org.lime.core.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.Math;
import org.joml.Matrix4f;
import org.lime.core.renderer.camera.Camera;
import org.lime.core.renderer.camera.ProjectionType;

public class SceneCamera extends Camera {
    @Getter
    private ProjectionType projectionType;
    @Getter
    private float perspectiveFOV;
    @Getter
    private float perspectiveNearClip;
    @Getter
    private float perspectiveFarClip;
    @Getter
    private float orthographicSize;
    @Getter
    private float orthographicNearClip;
    @Getter
    private float orthographicFarClip;
    private float aspectRatio;

    public SceneCamera() {
        super();
        this.projectionType = ProjectionType.Orthographic;
        this.orthographicSize = 10.0f;
        this.orthographicNearClip = -1.0f;
        this.orthographicFarClip = 1.0f;
        this.perspectiveFOV = Math.toRadians(45.0f);
        this.perspectiveNearClip = 0.01f;
        this.perspectiveFarClip = 1_000.0f;
        this.aspectRatio = 0.0f;
        reCalculateProjection();
    }

    public void setProjectionType(ProjectionType type) {
        projectionType = type;
        reCalculateProjection();
    }

    public void setPerspective(float verticalFOV, float nearClip, float farClip) {
        projectionType = ProjectionType.Perspective;
        perspectiveFOV = verticalFOV;
        perspectiveNearClip = nearClip;
        perspectiveFarClip = farClip;
        reCalculateProjection();
    }

    public void setOrthographic(float size, float nearClip, float farClip) {
        projectionType = ProjectionType.Orthographic;
        orthographicSize = size;
        orthographicNearClip = nearClip;
        orthographicFarClip = farClip;
        reCalculateProjection();
    }

    public void setViewportSize(int width, int height) {
        aspectRatio = (float) width / (float) height;
        reCalculateProjection();
    }

    public void setPerspectiveFOV(float verticalFOV) {
        perspectiveFOV = verticalFOV;
        reCalculateProjection();
    }

    public void setPerspectiveNearClip(float nearClip) {
        perspectiveNearClip = nearClip;
        reCalculateProjection();
    }

    public void setPerspectiveFarClip(float farClip) {
        perspectiveFarClip = farClip;
        reCalculateProjection();
    }

    public void setOrthographicSize(float size) {
        orthographicSize = size;
        reCalculateProjection();
    }

    public void setOrthographicNearClip(float nearClip) {
        orthographicNearClip = nearClip;
        reCalculateProjection();
    }

    public void setOrthographicFarClip(float farClip) {
        orthographicFarClip = farClip;
        reCalculateProjection();
    }

    private void reCalculateProjection() {
        switch (projectionType) {
            case Perspective -> reCalculatePerspective();
            case Orthographic -> reCalculateOrthographic();
        }

    }

    private void reCalculatePerspective() {
        projection = new Matrix4f()
                .perspective(perspectiveFOV, aspectRatio, perspectiveNearClip, perspectiveFarClip);
    }

    private void reCalculateOrthographic() {
        float orthographicLeft = -orthographicSize * aspectRatio * 0.5f;
        float orthographicRight = orthographicSize * aspectRatio * 0.5f;
        float orthographicBottom = -orthographicSize * 0.5f;
        float orthographicTop = orthographicSize * 0.5f;

        projection = new Matrix4f().ortho(orthographicLeft, orthographicRight,
                orthographicBottom, orthographicTop, orthographicNearClip, orthographicFarClip);
    }
}
