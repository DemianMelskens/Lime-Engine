package org.lime.core.scripting;

import org.lime.core.scene.Entity;
import org.lime.core.time.TimeStep;

public abstract class Scriptable {
    public Entity entity;

    public void onCreate() {
    }

    public void onUpdate(TimeStep timeStep) {
    }

    public void onDestroy() {
    }

    public <T> T getComponent(Class<T> clazz) {
        return entity.getComponent(clazz);
    }
}
