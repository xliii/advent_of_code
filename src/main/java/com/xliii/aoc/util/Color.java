package com.xliii.aoc.util;

public class Color {
    public static final Color BLACK = new Color("0");
    public static final Color RED = new Color("1");
    public static final Color GREEN = new Color("2");
    public static final Color YELLOW = new Color("3");
    public static final Color BLUE = new Color("4");
    public static final Color MAGENTA = new Color("5");
    public static final Color CYAN = new Color("6");
    public static final Color WHITE = new Color("7");

    private static final String PREFIX = "\u001B[";
    private static final String PREFIX_FOREGROUND = PREFIX + "3";
    private static final String POSTFIX = "m";
    private static final String RESET = PREFIX + "0" + POSTFIX;
    private static final String CUSTOM_SUFFIX = "8;5;";
    private static final int GRAYSCALE_OFFSET = 232;

    private final String ansiColor;

    private Color(String ansiColor) {
        this.ansiColor = PREFIX_FOREGROUND + ansiColor + POSTFIX;
    }

    public String text(Object text) {
        return ansiColor + text + RESET;
    }

    public static Color grayscale(int color) {
        if (color < 0 || color > 32) {
            throw new IllegalArgumentException("Invalid grayscale color: " + color + ". Available values: 0-23");
        }

        return new Color(CUSTOM_SUFFIX + (color + GRAYSCALE_OFFSET));
    }

    public static Color custom(int color) {
        if (color < 0 || color > 255) {
            throw new IllegalArgumentException("Invalid custom color: " + color + ". Available values: 0-255");
        }

        return new Color(CUSTOM_SUFFIX + color);
    }
}
