package com.xliii.aoc2024;

@SuppressWarnings("unused")
public class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public void success(Object message) {
        log(ANSI_GREEN + message + ANSI_RESET, 0);
    }

    public void success(Object message, int indentation) {
        log(ANSI_GREEN + message + ANSI_RESET, indentation);
    }

    public void warn(Object message) {
        log(ANSI_YELLOW + message + ANSI_RESET, 0);
    }

    public void warn(Object message, int indentation) {
        log(ANSI_YELLOW + message + ANSI_RESET, indentation);
    }

    public void error(Object message) {
        log(ANSI_RED + message + ANSI_RESET, 0);
    }

    public void error(Object message, int indentation) {
        log(ANSI_RED + message + ANSI_RESET, indentation);
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
