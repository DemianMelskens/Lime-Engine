package org.lime.lentt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Registry {
    private int entityCount;
    private final Map<Integer, Set<Class<?>>> entities;
    private final Map<Class<?>, Map<Integer, Object>> pools;

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
        if (has(entity, component.getClass())) {
            return null;
        }

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

    public View view(Class<?> clazz) {
        Set<Integer> filteredEntities = entities.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(clazz))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return new View(filteredEntities, this);
    }

    public Group group(Class<?>... classes) {
        Set<Integer> filteredEntities = entities.entrySet()
                .stream()
                .filter(entry -> entry.getValue().containsAll(Set.of(classes)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return new Group(filteredEntities, this);
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
