package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;
import org.lime.core.scene.SceneCamera;

@EqualsAndHashCode
public class CameraComponent {
    public boolean isPrimary = true; // TODO think about moving to scene
    public boolean hasFixedAspectRatio;
    public SceneCamera camera;

    public CameraComponent() {
        this.camera = new SceneCamera();
        this.hasFixedAspectRatio = false;
    }
}
