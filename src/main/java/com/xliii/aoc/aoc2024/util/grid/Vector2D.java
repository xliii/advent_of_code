package com.xliii.aoc.aoc2024.util.grid;

import com.xliii.aoc.aoc2024.util.Direction;

import java.util.List;

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

    public Vector2D move(Direction direction) {
        return new Vector2D(x + direction.x(), y + direction.y());
    }

    public boolean isNeighbor(Vector2D other) {
        for (Direction direction : Direction.ORTHOGONAL) {
            if (move(direction).equals(other)) {
                return true;
            }
        }

        return false;
    }

    public List<Vector2D> neighbors(List<Direction> directions) {
        return directions.stream()
                .map(this::move)
                .toList();
    }
}
