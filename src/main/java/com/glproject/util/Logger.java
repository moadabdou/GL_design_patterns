package com.glproject.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    private static Level globalLevel = Level.INFO;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static PrintStream output = System.out;

    private static final String NAME = "App";

    private Logger() {
    }

    private static class Holder {
        private static final Logger INSTANCE = new Logger();
    }

    public static Logger getInstance() {
        return Holder.INSTANCE;
    }

    public static Logger getLogger(Class<?> clazz) {
        return Holder.INSTANCE;
    }

    public static void setLevel(Level level) {
        globalLevel = level;
    }

    public static void setOutput(PrintStream out) {
        output = out;
    }

    public boolean isDebugEnabled() {
        return globalLevel.ordinal() <= Level.DEBUG.ordinal();
    }

    public boolean isInfoEnabled() {
        return globalLevel.ordinal() <= Level.INFO.ordinal();
    }

    public boolean isWarnEnabled() {
        return globalLevel.ordinal() <= Level.WARN.ordinal();
    }

    public boolean isErrorEnabled() {
        return globalLevel.ordinal() <= Level.ERROR.ordinal();
    }

    public void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    public void debug(String message, Object... args) {
        log(Level.DEBUG, format(message, args), null);
    }

    public void debug(String message, Throwable t) {
        log(Level.DEBUG, message, t);
    }

    public void info(String message) {
        log(Level.INFO, message, null);
    }

    public void info(String message, Object... args) {
        log(Level.INFO, format(message, args), null);
    }

    public void info(String message, Throwable t) {
        log(Level.INFO, message, t);
    }

    public void warn(String message) {
        log(Level.WARN, message, null);
    }

    public void warn(String message, Object... args) {
        log(Level.WARN, format(message, args), null);
    }

    public void warn(String message, Throwable t) {
        log(Level.WARN, message, t);
    }

    public void error(String message) {
        log(Level.ERROR, message, null);
    }

    public void error(String message, Object... args) {
        log(Level.ERROR, format(message, args), null);
    }

    public void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    private void log(Level level, String message, Throwable t) {
        if (level.ordinal() < globalLevel.ordinal()) {
            return;
        }
        String timestamp = LocalDateTime.now().format(FORMATTER);
        output.printf("%s [%s] %s - %s%n", timestamp, level, NAME, message);
        if (t != null) {
            t.printStackTrace(output);
        }
    }

    private static String format(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '{' && i + 1 < message.length() && message.charAt(i + 1) == '}') {
                sb.append(argIndex < args.length ? args[argIndex] : "{}");
                argIndex++;
                i++;
            } else {
                sb.append(message.charAt(i));
            }
        }
        return sb.toString();
    }
}
