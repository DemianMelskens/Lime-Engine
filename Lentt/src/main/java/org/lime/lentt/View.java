package org.lime.lentt;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class View<T> implements Iterable<Integer> {
    private final Set<Integer> entities;
    private final Registry registry;

    private final Class<T> scope;

    public View(Class<T> scope, Set<Integer> entities, Registry registry) {
        this.entities = entities;
        this.registry = registry;
        this.scope = scope;
    }

    public T get(int entity) {
        return registry.get(entity, scope);
    }

    @Override
    public Iterator<Integer> iterator() {
        return entities.iterator();
    }

    public void forEach(BiConsumer<Integer, T> action) {
        entities.forEach(entity -> action.accept(entity, get(entity)));
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return entities.spliterator();
    }

    public Stream<Integer> stream() {
        return entities.stream();
    }
}
