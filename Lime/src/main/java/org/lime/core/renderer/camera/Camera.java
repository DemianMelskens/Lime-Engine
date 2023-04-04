package org.lime.core.renderer.camera;

import lombok.Getter;
import org.joml.Matrix4f;

public class Camera {
    @Getter
    private Matrix4f projection;

    public Camera(Matrix4f projection) {
        this.projection = projection;
    }
}
