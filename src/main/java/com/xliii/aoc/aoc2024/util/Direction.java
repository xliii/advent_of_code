package com.xliii.aoc.aoc2024.util;

import java.util.Set;

public enum Direction {

    NORTH(0,-1),

    NORTH_EAST(1,-1),

    EAST(1,0),

    SOUTH_EAST(1,1),

    SOUTH(0,1),

    SOUTH_WEST(-1,1),

    WEST(-1,0),

    NORTH_WEST(-1,-1);

    public final static Set<Direction> ORTHOGONAL = Set.of(NORTH, EAST, SOUTH, WEST);
    public final static Set<Direction> DIAGONAL = Set.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    public final static Set<Direction> ALL = Sets.union(ORTHOGONAL, DIAGONAL);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}