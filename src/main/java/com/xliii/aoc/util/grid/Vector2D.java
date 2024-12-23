package com.xliii.aoc.util.grid;

import com.xliii.aoc.util.Direction;

import java.util.List;

public record Vector2D(int x, int y) {

    public static Vector2D ZERO = new Vector2D(0, 0);

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

    public static Vector2D fromString(String pos) {
        String[] parts = pos.split(",");
        return new Vector2D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public List<Vector2D> neighbors(List<Direction> directions) {
        return directions.stream()
                .map(this::move)
                .toList();
    }
}
