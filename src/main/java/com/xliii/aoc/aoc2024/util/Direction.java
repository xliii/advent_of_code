package com.xliii.aoc.aoc2024.util;

import java.util.List;

public enum Direction {

    NORTH(0,-1, '^'),

    NORTH_EAST(1,-1, ' '),

    EAST(1,0, '>'),

    SOUTH_EAST(1,1, ' '),

    SOUTH(0,1, 'v'),

    SOUTH_WEST(-1,1, ' '),

    WEST(-1,0, '<'),

    NORTH_WEST(-1,-1, ' ');

    public final static List<Direction> ORTHOGONAL = List.of(NORTH, EAST, SOUTH, WEST);
    public final static List<Direction> DIAGONAL = List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    public final static List<Direction> ALL = List.of(values());

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

    public Direction rotate45() {
        //TODO: Implement
        throw new RuntimeException("Not implemented");
    }

    public Direction rotate90() {
        //TODO: Implement CCW
        if (ORTHOGONAL.contains(this)) {
            int index = ORTHOGONAL.indexOf(this);
            return ORTHOGONAL.get((index + 1) % ORTHOGONAL.size());
        } else {
            int index = DIAGONAL.indexOf(this);
            return DIAGONAL.get((index + 1) % DIAGONAL.size());
        }
    }

    public static boolean isDirection(char sign) {
        for (Direction value : values()) {
            if (value.sign == sign) {
                return true;
            }
        }
        return false;
    }

    public static Direction bySign(char sign) {
        //TODO sign to direction cache
        for (Direction value : values()) {
            if (value.sign == sign) {
                return value;
            }
        }

        throw new IllegalArgumentException("No direction for sign: " + sign);
    }
}