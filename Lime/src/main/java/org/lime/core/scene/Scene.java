package org.lime.core.scene;

import org.joml.Matrix4f;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.camera.Camera;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TagComponent;
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
        Camera mainCamera = null;
        Matrix4f mainCameraTransform = null;

        Group group = registry.group(TransformComponent.class, CameraComponent.class);
        for (int entity : group) {
            TransformComponent transform = group.get(entity, TransformComponent.class);
            CameraComponent camera = group.get(entity, CameraComponent.class);

            if (camera.isPrimary) {
                mainCamera = camera.camera;
                mainCameraTransform = transform.transform;
                break;
            }
        }

        if (mainCamera != null) {
            Renderer2D.beginScene(mainCamera, mainCameraTransform);

            group = registry.group(TransformComponent.class, SpriteRendererComponent.class);
            for (int entity : group) {
                TransformComponent transform = group.get(entity, TransformComponent.class);
                SpriteRendererComponent sprite = group.get(entity, SpriteRendererComponent.class);

                Renderer2D.drawQuad(transform.transform, sprite.color.getValue());
            }

            Renderer2D.endScene();
        }

    }

    public Entity createEntity() {
        return createEntity("Entity");
    }

    public Entity createEntity(String name) {
        Entity entity = new Entity(registry.create(), this);
        entity.addComponent(new TagComponent(name));
        entity.addComponent(new TransformComponent());
        return entity;
    }

    public void shutdown() {
    }
}
