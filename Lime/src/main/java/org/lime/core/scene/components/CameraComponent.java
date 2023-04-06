package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;
import org.lime.core.scene.SceneCamera;

@EqualsAndHashCode
public class CameraComponent {
    public SceneCamera camera;
    public boolean hasFixedAspectRatio;
    public boolean isPrimary = true; // TODO think about moving to scene

    public CameraComponent() {
        this.camera = new SceneCamera();
        this.hasFixedAspectRatio = false;
    }
}
