package com.xliii.aoc2024;

public class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private int indentation = 0;

    public void success(Object message) {
        log(ANSI_GREEN + message + ANSI_RESET);
    }

    public void warn(Object message) {
        log(ANSI_YELLOW + message + ANSI_RESET);
    }

    public void error(Object message) {
        log(ANSI_RED + message + ANSI_RESET);
    }

    public void info(Object message) {
        log(message);
    }

    private void log(Object message) {
        System.out.println("  ".repeat(indentation) + message);
    }
}
