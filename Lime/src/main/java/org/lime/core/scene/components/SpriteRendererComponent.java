package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;
import org.lime.core.renderer.Color;

@EqualsAndHashCode
public class SpriteRendererComponent {
    public Color color;

    public SpriteRendererComponent() {
        this.color = Color.white();
    }

    public SpriteRendererComponent(Color color) {
        this.color = color;
    }
}
