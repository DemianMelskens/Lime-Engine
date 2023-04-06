package org.lime.lentt.utils;

public class Assert {

    public static void LNTT_CORE_ASSERT(boolean condition, String message) {
        if (!condition)
            throw LNTT_CORE_EXCEPTION(message);
    }

    public static RuntimeException LNTT_CORE_EXCEPTION(String message) {
        return new RuntimeException(String.format("Lime Entity Component System assertion failed: %s", message));
    }
}
