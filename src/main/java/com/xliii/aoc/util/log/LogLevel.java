package com.xliii.aoc.util.log;

public enum LogLevel {
    ERROR(0),
    WARN(1),
    SUCCESS(2),
    INFO(3),
    DEBUG(4);

    public final int level;

    LogLevel(int level) {
        this.level = level;
    }
}
