package org.lime.core.scene.components;

import org.joml.Matrix4f;
import org.lime.core.renderer.camera.Camera;

public class CameraComponent {
    public Camera camera;
    public boolean isPrimary = true; // TODO think about moving to scene

    public CameraComponent(Matrix4f projection) {
        this.camera = new Camera(projection);
    }
}
