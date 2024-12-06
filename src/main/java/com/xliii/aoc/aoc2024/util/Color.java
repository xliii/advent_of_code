package com.xliii.aoc.aoc2024.util;

public enum Color {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLACK("\u001b[30m"),
    BLUE("\u001b[34m"),
    MAGENTA("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m");


    private static final String RESET = "\u001B[0m";
    private final String ansiColor;

    Color(String ansiColor) {
        this.ansiColor = ansiColor;
    }

    public String text(Object text) {
        return ansiColor + text + RESET;
    }
}
