package com.xliii.aoc.util;

import com.xliii.aoc.util.grid.Vector2D;

import java.util.*;

public enum Direction {

    NORTH(0,-1, '^', Set.of("n", "north", "up", "u")),

    NORTH_EAST(1,-1, '↗', Set.of("ne")),

    EAST(1,0, '>', Set.of("e", "east", "right", "r")),

    SOUTH_EAST(1,1, '↘', Set.of("se")),

    SOUTH(0,1, 'v', Set.of("s", "south", "down", "d")),

    SOUTH_WEST(-1,1, '↙', Set.of("sw")),

    WEST(-1,0, '<', Set.of("w", "west", "left", "l")),

    NORTH_WEST(-1,-1, '↖', Set.of("nw"));

    public final static List<Direction> ORTHOGONAL = List.of(NORTH, EAST, SOUTH, WEST);
    public final static List<Direction> DIAGONAL = List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    public final static List<Direction> ALL = List.of(values());

    private final static Map<Character, Direction> bySign = new HashMap<>();
    private final static Map<String, Direction> byAlias = new HashMap<>();
    private final static Map<Vector2D, Direction> byVector = new HashMap<>();

    static {
        for (Direction direction : values()) {
            bySign.put(direction.sign, direction);
            byVector.put(new Vector2D(direction.x, direction.y), direction);

            for (var alias : direction.aliases) {
                byAlias.put(alias, direction);
            }

        }
    }

    private final int x;
    private final int y;
    private final char sign;
    private final Set<String> aliases;

    Direction(int x, int y, char sign, Set<String> aliases) {
        this.x = x;
        this.y = y;
        this.sign = sign;
        this.aliases = aliases;
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

    public static Optional<Direction> bySign(char sign) {
        return Optional.ofNullable(bySign.getOrDefault(sign, null));
    }

    public static Optional<Direction> byAlias(String alias) {
        return Optional.ofNullable(byAlias.getOrDefault(alias, null));
    }

    public static Optional<Direction> byVector(Vector2D vector) {
        return Optional.ofNullable(byVector.getOrDefault(vector, null));
    }
}