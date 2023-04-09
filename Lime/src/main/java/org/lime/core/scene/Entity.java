package org.lime.core.scene;

import java.util.Objects;

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

    public Integer get() {
        return entityHandle;
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
        return entityHandle != null && scene.registry.hasEntity(entityHandle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;
        return Objects.equals(entityHandle, entity.entityHandle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityHandle, scene);
    }
}
