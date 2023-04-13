package org.lime.core.scene.components;

import lombok.EqualsAndHashCode;
import org.lime.core.scripting.Scriptable;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

@EqualsAndHashCode
public class NativeScriptComponent<T extends Scriptable> {
    public Class<T> clazz;
    public Scriptable instance;
    public Supplier<Scriptable> instantiate;
    public Consumer<NativeScriptComponent> destroy;

    public NativeScriptComponent(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void bind() {
        instantiate = () -> getInstance(clazz);
        destroy = component -> component.instance = null;
    }

    private T getInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw LM_CORE_EXCEPTION(String.format("Could not instantiate script: %s", clazz.getName()));
        }
    }
}
