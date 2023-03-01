package org.lime.core.utils;

public class Assert {

    public static void LM_CORE_ASSERT(boolean condition, String message) {
        if (!condition)
            throw new RuntimeException(String.format("Lime Engine Core assertion failed: %s", message));
    }
}
