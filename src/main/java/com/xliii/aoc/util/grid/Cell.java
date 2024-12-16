package com.xliii.aoc.util.grid;

import com.xliii.aoc.util.Direction;

import java.util.Optional;

public record Cell<T>(int x, int y, T value) {
    public Vector2D pos() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ":" + y + "|" + value + ")";
    }

    public Optional<Direction> directionTo(Cell<T> other) {
        Vector2D diff = other.pos().subtract(pos());
        return Direction.byVector(diff);
    }
}
