package org.lime.core.scene;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class Entity {

    private Integer entityHandle;
    private Scene scene;

    public Entity() {
    }

    public Entity(int entityHandle, Scene scene) {
        this.entityHandle = entityHandle;
        this.scene = scene;
    }

    public Entity(Entity other) {
        this.entityHandle = other.entityHandle;
        this.scene = other.scene;
    }

    public boolean hasComponent(Class<?> clazz) {
        return scene.registry.has(entityHandle, clazz);
    }

    public <T> T getComponent(Class<T> clazz) {
        LM_CORE_ASSERT(hasComponent(clazz), "Entity does not have component!");
        return scene.registry.get(entityHandle, clazz);
    }

    public <T> T addComponent(Class<T> clazz, Object... args) {
        LM_CORE_ASSERT(!hasComponent(clazz), "Entity already has component!");
        return scene.registry.emplace(entityHandle, clazz, args);
    }

    public void removeComponent(Class<?> clazz) {
        scene.registry.remove(entityHandle, clazz);
    }

    public boolean isValid() {
        return entityHandle != null;
    }
}
