package com.expensetracker.util;

import org.slf4j.LoggerFactory;

/**
 * Utility class for structured logging with different severity levels.
 * Wraps SLF4J logger for consistent logging across the application.
 */
public class Logger {
    private final org.slf4j.Logger logger;
    private final String className;

    public Logger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.className = clazz.getSimpleName();
    }

    /**
     * Log an informational message.
     */
    public void info(String message) {
        logger.info("[{}] {}", className, message);
    }

    /**
     * Log an informational message with parameters.
     */
    public void info(String message, Object... args) {
        logger.info("[{}] " + message, prependClassName(args));
    }

    /**
     * Log a warning message.
     */
    public void warn(String message) {
        logger.warn("[{}] {}", className, message);
    }

    /**
     * Log a warning message with parameters.
     */
    public void warn(String message, Object... args) {
        logger.warn("[{}] " + message, prependClassName(args));
    }

    /**
     * Log an error message.
     */
    public void error(String message) {
        logger.error("[{}] {}", className, message);
    }

    /**
     * Log an error message with exception.
     */
    public void error(String message, Throwable throwable) {
        logger.error("[{}] {}", className, message, throwable);
    }

    /**
     * Log an error message with parameters.
     */
    public void error(String message, Object... args) {
        logger.error("[{}] " + message, prependClassName(args));
    }

    /**
     * Log a debug message.
     */
    public void debug(String message) {
        logger.debug("[{}] {}", className, message);
    }

    /**
     * Log a debug message with parameters.
     */
    public void debug(String message, Object... args) {
        logger.debug("[{}] " + message, prependClassName(args));
    }

    /**
     * Helper method to prepend class name to arguments array.
     */
    private Object[] prependClassName(Object... args) {
        Object[] result = new Object[args.length + 1];
        result[0] = className;
        System.arraycopy(args, 0, result, 1, args.length);
        return result;
    }
}
