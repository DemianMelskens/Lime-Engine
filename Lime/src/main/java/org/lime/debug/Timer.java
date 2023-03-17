package org.lime.debug;

import java.util.*;
import java.util.function.Consumer;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class Timer implements Iterable<ProfileResult> {

    private static Timer instance;
    private final Map<String, Long> profiles;
    private final Map<String, ProfileResult> results;

    static Timer results() {
        return get();
    }

    static void start(String name) {
        get().profiles.put(name, currentMicroTime());
    }

    static void stop(String name) {
        Timer timer = get();
        if (!timer.profiles.containsKey(name)) {
            throw LM_CORE_EXCEPTION(String.format("No profile with name: %s is running!", name));
        }

        long startTimePoint = timer.profiles.get(name);
        long endTimePoint = currentMicroTime();

        ProfileResult result = new ProfileResult(name, startTimePoint, endTimePoint);
        timer.results.put(name, result);

        if (Profiler.output) {
            Instrumentor.get().writeProfile(result);
        }
    }

    static void clear() {
        Timer timer = get();
        timer.results.clear();
    }

    static Timer get() {
        if (instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    private Timer() {
        this.profiles = new HashMap<>();
        this.results = new LinkedHashMap<>();
    }

    @Override
    public Iterator<ProfileResult> iterator() {
        return results.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super ProfileResult> action) {
        results.values().forEach(action);
    }

    @Override
    public Spliterator<ProfileResult> spliterator() {
        return results.values().spliterator();
    }

    private static long currentMicroTime() {
        return System.nanoTime();
    }
}
