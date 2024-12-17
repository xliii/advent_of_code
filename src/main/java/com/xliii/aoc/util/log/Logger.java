package com.xliii.aoc.util.log;

import com.xliii.aoc.util.Color;

@SuppressWarnings("unused")
public class Logger {

    private LogLevel level;

    public Logger() {
        this(LogLevel.DEBUG);
    }

    public Logger(LogLevel level) {
        this.level = level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void success(Object message) {
        success(message, 0);
    }

    public void success(Object message, int indentation) {
        log(Color.GREEN.text(message), indentation, LogLevel.SUCCESS);
    }

    public void warn(Object message) {
        warn(message, 0);
    }

    public void warn(Object message, int indentation) {
        log(Color.YELLOW.text(message), indentation, LogLevel.WARN);
    }

    public void error(Object message) {
        error(message, 0);
    }

    public void error(Object message, int indentation) {
        log(Color.RED.text(message), indentation, LogLevel.ERROR);
    }

    public void info(Object message) {
        info(message, 0);
    }

    public void info() {
        info("", 0);
    }

    public void info(Object message, int indentation) {
        log(message, indentation, LogLevel.INFO);
    }

    public void debug(Object message) {
        debug(message, 0);
    }

    public void debug(Object message, int indentation) {
        log(Color.CYAN.text(message), indentation, LogLevel.DEBUG);
    }

    private void log(Object message, int indentation, LogLevel level) {
        if (shouldLog(level)) {
            System.out.println("  ".repeat(indentation) + message);
        }
    }

    private boolean shouldLog(LogLevel level) {
        return level.level <= this.level.level;
    }
}
