package org.lime.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final String CORE_TEMPLATE = "%sLime Engine Core: %s%s";
    private static final String TEMPLATE = "%sLime Engine: %s%s";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    private Log() {
    }

    public static void LM_CORE_TRACE(String message) {
        if (logger.isTraceEnabled())
            logger.trace(String.format(CORE_TEMPLATE, ANSI_CYAN, message, ANSI_RESET));
    }

    public static void LM_TRACE(String message) {
        if (logger.isTraceEnabled())
            logger.trace(String.format(TEMPLATE, ANSI_CYAN, message, ANSI_RESET));
    }

    public static void LM_CORE_DEBUG(String message) {
        if (logger.isDebugEnabled())
            logger.debug(String.format(CORE_TEMPLATE, ANSI_PURPLE, message, ANSI_RESET));
    }

    public static void LM_DEBUG(String message) {
        if (logger.isDebugEnabled())
            logger.debug(String.format(TEMPLATE, ANSI_PURPLE, message, ANSI_RESET));
    }

    public static void LM_CORE_INFO(String message) {
        if (logger.isInfoEnabled())
            logger.info(String.format(CORE_TEMPLATE, ANSI_WHITE, message, ANSI_RESET));
    }

    public static void LM_INFO(String message) {
        if (logger.isInfoEnabled())
            logger.info(String.format(TEMPLATE, ANSI_WHITE, message, ANSI_RESET));
    }

    public static void LM_CORE_WARN(String message) {
        if (logger.isWarnEnabled())
            logger.warn(String.format(CORE_TEMPLATE, ANSI_YELLOW, message, ANSI_RESET));
    }

    public static void LM_WARN(String message) {
        if (logger.isWarnEnabled())
            logger.warn(String.format(TEMPLATE, ANSI_YELLOW, message, ANSI_RESET));
    }

    public static void LM_CORE_ERROR(String message) {
        if (logger.isErrorEnabled())
            logger.error(String.format(CORE_TEMPLATE, ANSI_RED, message, ANSI_RESET));
    }

    public static void LM_ERROR(String message) {
        if (logger.isErrorEnabled())
            logger.error(String.format(TEMPLATE, ANSI_RED, message, ANSI_RESET));
    }
}
