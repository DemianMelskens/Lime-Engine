package org.lime.debug;

public abstract class Profiler {

    public static boolean output = false;
    public static boolean enabled = false;

    public static void beginSession(String name) {
        beginSession(name, "results.json");
    }

    public static void beginSession(String name, String filePath) {
        Instrumentor.get().beginSession(name, filePath);
    }

    public static void endSession() {
        Instrumentor.get().endSession();
    }

    public static synchronized void startProfile(String name) {
        Timer.start(name);
    }

    public static synchronized void stopProfile(String name) {
        Timer.stop(name);
    }

    public static void clearProfiles() {
        Timer.clear();
    }

    public static Timer results() {
        return Timer.results();
    }
}
