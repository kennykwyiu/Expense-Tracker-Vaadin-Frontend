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

}
