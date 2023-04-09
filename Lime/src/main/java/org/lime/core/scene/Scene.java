package org.lime.core.scene;

import org.joml.Matrix4f;
import org.lime.core.renderer.Renderer2D;
import org.lime.core.renderer.camera.Camera;
import org.lime.core.scene.components.*;
import org.lime.core.time.TimeStep;
import org.lime.lentt.Group;
import org.lime.lentt.Registry;
import org.lime.lentt.View;

public class Scene {

    Registry registry;

    public Scene() {
        this.registry = new Registry();
    }

    public Registry getRegistry() {
        return registry;
    }

    public void onUpdate(TimeStep timeStep) {
        //TODO: move to onScene play
        registry.view(NativeScriptComponent.class)
                .forEach((entity, component) -> {
                    if (component.instance == null) {
                        component.instance = component.instantiate.get();
                        component.instance.entity = new Entity(entity, this);
                        component.instance.onCreate();
                    }

                    component.instance.onUpdate(timeStep);
                });

        Camera mainCamera = null;
        Matrix4f mainCameraTransform = null;

        Group group = registry.group(TransformComponent.class, CameraComponent.class);
        for (int entity : group) {
            TransformComponent transform = group.get(entity, TransformComponent.class);
            CameraComponent camera = group.get(entity, CameraComponent.class);

            if (camera.isPrimary) {
                mainCamera = camera.camera;
                mainCameraTransform = transform.getTransform();
                break;
            }
        }

        if (mainCamera != null) {
            Renderer2D.beginScene(mainCamera, mainCameraTransform);

            group = registry.group(TransformComponent.class, SpriteRendererComponent.class);
            for (int entity : group) {
                TransformComponent transform = group.get(entity, TransformComponent.class);
                SpriteRendererComponent sprite = group.get(entity, SpriteRendererComponent.class);

                Renderer2D.drawQuad(transform.getTransform(), sprite.color.getValue());
            }

            Renderer2D.endScene();
        }

    }

    public Entity createEntity() {
        return createEntity("Unnamed entity");
    }

    public Entity createEntity(String name) {
        Entity entity = new Entity(registry.create(), this);
        entity.addComponent(TagComponent.class, name);
        entity.addComponent(TransformComponent.class);
        return entity;
    }

    public void deleteEntity(Entity entity) {
        registry.delete(entity.get());
    }

    public void onViewportResize(int width, int height) {
        View<CameraComponent> view = registry.view(CameraComponent.class);
        for (int entity : view) {
            var cameraComponent = view.get(entity);
            if (!cameraComponent.hasFixedAspectRatio) {
                cameraComponent.camera.setViewportSize(width, height);
            }
        }
    }

    public void shutdown() {
    }
}
