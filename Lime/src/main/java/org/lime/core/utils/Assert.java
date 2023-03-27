package org.lime.core.utils;

public class Assert {

    public static void LM_CORE_ASSERT(boolean condition, String message) {
        if (!condition)
            throw LM_CORE_EXCEPTION(message);
    }

    public static RuntimeException LM_CORE_EXCEPTION(String message) {
        return new RuntimeException(String.format("Lime Engine Core assertion failed: %s", message));
    }
}
