package org.lime.lentt;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class View implements Iterable<Integer> {
    private final Set<Integer> entities;
    private final Registry registry;

    public View(Set<Integer> entities, Registry registry) {
        this.entities = entities;
        this.registry = registry;
    }

    public <T> T get(int entity, Class<T> clazz) {
        return registry.get(entity, clazz);
    }

    @Override
    public Iterator<Integer> iterator() {
        return entities.iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        entities.forEach(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return entities.spliterator();
    }
}
