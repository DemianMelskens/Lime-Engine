package org.lime.core.scene;

public class Entity {

    private int handle;
    private Scene scene;

    public Entity(int handle, Scene scene) {
        this.handle = handle;
        this.scene = scene;
    }

    public Entity(Entity other) {
        this.handle = other.handle;
        this.scene = other.scene;
    }

    public boolean hasComponent(Class<?> clazz) {
        return scene.registry.has(handle, clazz);
    }

    public <T> T getComponent(Class<T> clazz) {
        return scene.registry.get(handle, clazz);
    }

    public <T> T addComponent(T component) {
        return scene.registry.emplace(handle, component);
    }
}
