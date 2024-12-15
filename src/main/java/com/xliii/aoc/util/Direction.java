package com.xliii.aoc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Direction {

    NORTH(0,-1, '^'),

    NORTH_EAST(1,-1, '↗'),

    EAST(1,0, '>'),

    SOUTH_EAST(1,1, '↘'),

    SOUTH(0,1, 'v'),

    SOUTH_WEST(-1,1, '↙'),

    WEST(-1,0, '<'),

    NORTH_WEST(-1,-1, '↖');

    public final static List<Direction> ORTHOGONAL = List.of(NORTH, EAST, SOUTH, WEST);
    public final static List<Direction> DIAGONAL = List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    public final static List<Direction> ALL = List.of(values());
    private final static Map<Character, Direction> bySign = new HashMap<>();

    static {
        for (Direction dir : values()) {
            bySign.put(dir.sign, dir);
        }
    }

    private final int x;
    private final int y;
    private final char sign;

    Direction(int x, int y, char sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public char sign() {
        return sign;
    }

    public Direction rotate45(boolean clockwise) {
        int diff = clockwise ? 1 : -1;
        int index = ALL.indexOf(this);
        return ALL.get((ALL.size() + index + diff) % ALL.size());
    }

    public Direction rotate45() {
        return rotate45(true);
    }

    public Direction rotate90(boolean clockwise) {
        int diff = clockwise ? 1 : -1;
        if (ORTHOGONAL.contains(this)) {
            int index = ORTHOGONAL.indexOf(this);
            return ORTHOGONAL.get((ORTHOGONAL.size() + index + diff) % ORTHOGONAL.size());
        } else {
            int index = DIAGONAL.indexOf(this);
            return DIAGONAL.get((DIAGONAL.size() + index + diff) % DIAGONAL.size());
        }
    }

    public Direction rotate90() {
        return rotate90(true);
    }

    public static boolean isDirection(char sign) {
        return bySign.containsKey(sign);
    }

    public static Direction bySign(char sign) {
        if (!isDirection(sign)) {
            throw new IllegalArgumentException("No direction for sign: " + sign);
        }

        return bySign.get(sign);
    }
}