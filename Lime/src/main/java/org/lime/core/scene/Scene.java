package org.lime.core.scene;

import org.lime.core.renderer.Renderer2D;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.time.TimeStep;
import org.lime.lentt.Group;
import org.lime.lentt.Registry;

public class Scene {

    Registry registry;

    public Scene() {
        this.registry = new Registry();
    }

    public void onUpdate(TimeStep timeStep) {
        Group group = registry.group(TransformComponent.class, SpriteRendererComponent.class);
        for (int entity : group) {
            TransformComponent transform = group.get(entity, TransformComponent.class);
            SpriteRendererComponent sprite = group.get(entity, SpriteRendererComponent.class);

            Renderer2D.drawQuad(transform.transform, sprite.color.getValue());
        }
    }

    public Entity createEntity() {
        return new Entity(registry.create(), this);
    }

    public void shutdown() {
    }
}
