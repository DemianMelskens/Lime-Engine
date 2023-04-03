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
    Set<Integer> entities;
    Map<Class<?>, Map<Integer, Object>> pools;

    public Registry() {
        this.pools = new HashMap<>();
        this.entities = new HashSet<>();
        this.entityCount = 0;
    }

    public int create() {
        int entity = entityCount++;
        entities.add(entity);
        return entity;
    }

    public <T> T emplace(int entity, T component) {
        Class<?> clazz = component.getClass();
        Map<Integer, Object> pool = getPool(clazz);
        pool.put(entity, component);
        pools.put(clazz, pool);
        return component;
    }

    public <T> T get(int entity, Class<T> clazz) {
        return clazz.cast(getPool(clazz).get(entity));
    }

    public boolean has(int entity, Class<?> clazz) {
        return getPool(clazz).containsKey(entity);
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
        entities.remove(entity);
        getPool(clazz).remove(entity);
    }

    private Map<Integer, Object> getPool(Class<?> clazz) {
        return pools.getOrDefault(clazz, new HashMap<>());
    }
}
