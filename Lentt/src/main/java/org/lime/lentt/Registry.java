package org.lime.lentt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Registry {
    int entityCount;
    Map<Integer, Set<Class<?>>> entities;
    Map<Class<?>, Map<Integer, Object>> pools;

    public Registry() {
        this.pools = new HashMap<>();
        this.entities = new HashMap<>();
        this.entityCount = 0;
    }

    public int create() {
        int entity = entityCount++;
        entities.put(entity, new HashSet<>());
        return entity;
    }

    public void delete(int entity) {
        entities.get(entity)
                .forEach(clazz -> pools.get(clazz).remove(entity));
        entities.remove(entity);
    }

    public <T> T emplace(int entity, T component) {
        Class<?> clazz = component.getClass();
        getPool(clazz).put(entity, component);
        entities.get(entity).add(clazz);
        return component;
    }

    public <T> T get(int entity, Class<T> clazz) {
        return clazz.cast(getPool(clazz).get(entity));
    }

    public boolean has(int entity, Class<?> clazz) {
        return entities.get(entity).contains(clazz);
    }

    public <T> View<T> view(Class<T> clazz) {
        return new View<>(entities, (Map<Integer, T>) getPool(clazz));
    }

    public Group group(Class<?>... classes) {
        Map<Class<?>, Map<Integer, Object>> groups = Stream.of(classes)
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::getPool
                ));
        return new Group(entities, groups);
    }

    public void remove(int entity, Class<?> clazz) {
        getPool(clazz).remove(entity);
        entities.get(entity).remove(clazz);
    }

    private Map<Integer, Object> getPool(Class<?> clazz) {
        if (pools.containsKey(clazz)) {
            return pools.get(clazz);
        }

        HashMap<Integer, Object> pool = new HashMap<>();
        pools.put(clazz, pool);
        return pool;
    }
}
