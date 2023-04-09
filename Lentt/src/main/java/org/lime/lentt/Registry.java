package org.lime.lentt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.lime.lentt.utils.Assert.LNTT_CORE_EXCEPTION;

/**
 * The registry is responsible for managing the entities and components.
 */
public class Registry implements Iterable<Integer> {
    private int entityCount;
    private final Map<Integer, Set<Class<?>>> entities;
    private final Map<Class<?>, Map<Integer, Object>> pools;

    /**
     * Registry default constructor.
     */
    public Registry() {
        this.pools = new HashMap<>();
        this.entities = new HashMap<>();
        this.entityCount = 0;
    }

    /**
     * Creates a new entity.
     *
     * @return the entity id.
     */
    public int create() {
        int entity = entityCount++;
        entities.put(entity, new HashSet<>());
        return entity;
    }

    /**
     * Deletes the entity and all its components
     *
     * @param entity entity id.
     */
    public void delete(int entity) {
        entities.get(entity)
            .forEach(clazz -> pools.get(clazz).remove(entity));
        entities.remove(entity);
    }

    /**
     * Checks if the given entity exists in the registry.
     *
     * @param entity entity id.
     * @return True if the entity is found otherwise false.
     */
    public boolean has(int entity) {
        return entities.containsKey(entity);
    }


    /**
     * <p>
     * Inserts a component instance into the registry.<br/>
     * If the entity doesn't have this type of component already.
     * </p>
     *
     * @param entity    entity id.
     * @param component component instance.
     * @param <T>       component class type.
     * @return The component or null when the entity already has this component.
     */
    public <T> T insert(int entity, T component) {
        if (has(entity, component.getClass())) {
            return null;
        }

        Class<?> clazz = component.getClass();
        getPool(clazz).put(entity, component);
        entities.get(entity).add(clazz);
        return component;
    }

    /**
     * <p>
     * Creates an instance of the component and inserts it into the registry.<br/>
     * If the entity doesn't have this type of component already.
     * </p>
     *
     * @param entity entity id.
     * @param clazz  class type.
     * @param args   arguments for the class constructor.
     * @param <T>    component class type.
     * @return The created component or null when the entity already has this component.
     */
    public <T> T emplace(int entity, Class<T> clazz, Object... args) {
        try {
            if (has(entity, clazz)) {
                return null;
            }

            T component = getConstructor(clazz, args).newInstance(args);
            getPool(clazz).put(entity, component);
            entities.get(entity).add(clazz);
            return component;
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 IllegalArgumentException ex) {
            throw LNTT_CORE_EXCEPTION(String.format("Tried to construct class %s but failed!", clazz.getName()));
        }
    }

    /**
     * <p>
     * Retrieves the component instance for the entity.<br/>
     * If not found returns null.
     * </p>
     *
     * @param entity entity id.
     * @param clazz  class type.
     * @param <T>    component class type.
     * @return The component for the entity or null of the component is not found.
     */
    public <T> T get(int entity, Class<T> clazz) {
        return clazz.cast(getPool(clazz).get(entity));
    }

    /**
     * Checks if the given entity has a component instance.
     *
     * @param entity entity id.
     * @param clazz  class type.
     * @return True if the entity has the component otherwise false.
     */
    public boolean has(int entity, Class<?> clazz) {
        return entities.get(entity).contains(clazz);
    }

    /**
     * Removes component instance for entity
     *
     * @param entity entity id.
     * @param clazz  class type.
     */
    public void remove(int entity, Class<?> clazz) {
        getPool(clazz).remove(entity);
        entities.get(entity).remove(clazz);
    }

    /**
     * <p>
     * Creates a smaller view for the registry.<br/>
     * Checks which entities have the provided component.<br/>
     * Provides a more optimize way to loop through the entities.<br/>
     * For multiple component views use {@link #group(Class[])} as it is more optimized.
     * </p>
     *
     * @param clazz class type for requested component.
     * @param <T>   component class type.
     * @return The new view.
     * @see View
     */
    public <T> View<T> view(Class<T> clazz) {
        Set<Integer> filteredEntities = entities.entrySet()
            .stream()
            .filter(entry -> entry.getValue().contains(clazz))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        return new View<>(clazz, filteredEntities, this);
    }

    /**
     * <p>
     * Creates a smaller group for the registry.<br/>
     * Checks which entities have the provided components.<br/>
     * Provides a more optimize way to loop through the entities.<br/>
     * For single component group use {@link #view(Class)} as it is more optimized.
     * </p>
     *
     * @param classes class types for requested components.
     * @return The new group.
     * @see Group
     */
    public Group group(Class<?>... classes) {
        Set<Integer> filteredEntities = entities.entrySet()
            .stream()
            .filter(entry -> entry.getValue().containsAll(Set.of(classes)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());

        return new Group(filteredEntities, this);
    }

    private Map<Integer, Object> getPool(Class<?> clazz) {
        if (pools.containsKey(clazz)) {
            return pools.get(clazz);
        }

        HashMap<Integer, Object> pool = new HashMap<>();
        pools.put(clazz, pool);
        return pool;
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz, Object... args) {
        Class<?>[] ctorArgs = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);

        try {
            return clazz.getConstructor(ctorArgs);
        } catch (NoSuchMethodException | IllegalArgumentException ex) {
            throw LNTT_CORE_EXCEPTION(String.format("Tried to get construct class for %s with args %s!", clazz.getName(), Arrays.toString(ctorArgs)));
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return entities.keySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        entities.keySet().forEach(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return entities.keySet().spliterator();
    }

    public Stream<Integer> stream() {
        return entities.keySet().stream();
    }
}
