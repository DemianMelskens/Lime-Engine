package org.lime.debug;

import static org.lime.core.utils.Log.LM_CORE_INFO;

public interface Profiler {

    static void startOutput() {
        LM_CORE_INFO("Profiler output started!");
        Instrumentor.get().beginSession();
    }

    static void startOutput(String filePath) {
        LM_CORE_INFO("Profiler output started!");
        Instrumentor.get().beginSession(filePath);
    }

    static void startProfile(String name) {
        Timer.start(name);
    }

    static void stopProfile(String name) {
        Timer.stop(name);
    }

    static void clearProfiles() {
        Timer.clear();
    }

    static Timer results() {
        return Timer.results();
    }

    static void stopOutput() {
        Instrumentor.get().endSession();
    }
}
