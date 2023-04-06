package org.lime.core.scene.components;

import org.lime.core.scripting.Scriptable;

import java.lang.reflect.InvocationTargetException;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class NativeScriptComponent {
    public Scriptable instance;
    public Runnable instantiateFunction;

    public <T extends Scriptable> void bind(Class<T> clazz) {
        instantiateFunction = () -> this.instance = getInstance(clazz);
    }

    private <T> T getInstance(Class<T> clazz) {
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
