package org.lime.lentt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class View<T> implements Iterable<Integer> {
    Set<Integer> entities;
    Map<Integer, T> pool;

    public View(Set<Integer> entities, Map<Integer, T> pool) {
        this.entities = entities;
        this.pool = pool;
    }

    public T get(int entity) {
        return pool.get(entity);
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
