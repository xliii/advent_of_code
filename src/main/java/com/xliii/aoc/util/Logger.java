package com.xliii.aoc.util;

@SuppressWarnings("unused")
public class Logger {

    public void success(Object message) {
        success(message, 0);
    }

    public void success(Object message, int indentation) {
        log(Color.GREEN.text(message), indentation);
    }

    public void warn(Object message) {
        warn(message, 0);
    }

    public void warn(Object message, int indentation) {
        log(Color.YELLOW.text(message), indentation);
    }

    public void error(Object message) {
        error(message, 0);
    }

    public void error(Object message, int indentation) {
        log(Color.RED.text(message), indentation);
    }

    public void info(Object message) {
        log(message, 0);
    }

    public void info() {
        log("", 0);
    }

    public void info(Object message, int indentation) {
        log(message, indentation);
    }

    private void log(Object message, int indentation) {
        System.out.println("  ".repeat(indentation) + message);
    }
}
