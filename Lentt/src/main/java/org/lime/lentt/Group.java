package org.lime.lentt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Group implements Iterable<Integer> {
    Set<Integer> entities;
    Map<Class<?>, Map<Integer, Object>> pools;

    public Group(Set<Integer> entities, Map<Class<?>, Map<Integer, Object>> pools) {
        this.entities = entities;
        this.pools = pools;
    }

    public <T> T get(int entity, Class<T> clazz) {
        return clazz.cast(getPool(clazz).get(entity));
    }

    private Map<Integer, Object> getPool(Class<?> clazz) {
        return pools.get(clazz);
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
