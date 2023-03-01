package org.lime.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private static Logger logger = LoggerFactory.getLogger(Log.class);

    public static void LM_CORE_TRACE(String message) {
        logger.trace(String.format("Lime Engine Core: %s", message));
    }

    public static void LM_TRACE(String message) {
        logger.trace(String.format("Lime Engine: %s", message));
    }

    public static void LM_CORE_INFO(String message) {
        logger.info(String.format("Lime Engine Core: %s", message));
    }

    public static void LM_INFO(String message) {
        logger.info(String.format("Lime Engine: %s", message));
    }

    public static void LM_CORE_WARN(String message) {
        logger.warn(String.format("Lime Engine Core: %s", message));
    }

    public static void LM_WARN(String message) {
        logger.warn(String.format("Lime Engine: %s", message));
    }

    public static void LM_CORE_ERROR(String message) {
        logger.error(String.format("Lime Engine Core: %s", message));
    }

    public static void LM_ERROR(String message) {
        logger.error(String.format("Lime Engine: %s", message));
    }
}
