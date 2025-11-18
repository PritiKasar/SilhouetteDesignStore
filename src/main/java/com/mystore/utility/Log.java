package com.mystore.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log Utility (Java 21 + Log4j2 Compatible)
 * -----------------------------------------
 * Provides clean logging APIs for test reporting.
 * Uses Log4j2 (modern, fast, thread-safe) instead of deprecated Log4j 1.x.
 */
public class Log {

    private static final Logger logger = LogManager.getLogger(Log.class);

    /** Marks the start of a test case */
    public static void startTestCase(String testName) {
        logger.info("========== STARTING TEST: {} ==========", testName);
    }

    /** Marks the end of a test case */
    public static void endTestCase(String testName) {
        logger.info("========== ENDING TEST: {} ==========", testName);
    }

    /** Log an INFO message */
    public static void info(String message) {
        logger.info(message);
    }

    /** Log a WARNING */
    public static void warn(String message) {
        logger.warn(message);
    }

    /** Log an ERROR */
    public static void error(String message) {
        logger.error(message);
    }

    /** Log a FATAL error */
    public static void fatal(String message) {
        logger.fatal(message);
    }

    /** Log a DEBUG message */
    public static void debug(String message) {
        logger.debug(message);
    }
}
