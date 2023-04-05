package org.lime.core.renderer.camera;

import lombok.Getter;
import org.joml.Matrix4f;

public class Camera {
    @Getter
    protected Matrix4f projection;

    public Camera() {
        this.projection = new Matrix4f();
    }

    public Camera(Matrix4f projection) {
        this.projection = projection;
    }
}
