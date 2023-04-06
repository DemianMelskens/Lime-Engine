package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TagComponent {
    public String tag;

    public TagComponent(String tag) {
        this.tag = tag;
    }
}
