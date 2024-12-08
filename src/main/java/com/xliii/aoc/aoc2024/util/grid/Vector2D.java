package com.xliii.aoc.aoc2024.util.grid;

public record Vector2D(int x, int y) {
    @Override
    public String toString() {
        return "(" + x + ":" + y + ")";
    }

    public Vector2D subtract(Vector2D o) {
        return new Vector2D(x - o.x, y - o.y);
    }

    public Vector2D add(Vector2D o) {
        return new Vector2D(x + o.x, y + o.y);
    }
}
