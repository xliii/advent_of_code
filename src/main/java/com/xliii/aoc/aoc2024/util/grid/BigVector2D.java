package com.xliii.aoc.aoc2024.util.grid;

import com.xliii.aoc.aoc2024.util.Direction;

import java.math.BigDecimal;
import java.util.List;

public record BigVector2D(BigDecimal x, BigDecimal y) {

    public static BigVector2D ZERO = new BigVector2D(BigDecimal.ZERO, BigDecimal.ZERO);

    @Override
    public String toString() {
        return "(" + x + ":" + y + ")";
    }

    public BigVector2D subtract(BigVector2D o) {
        return new BigVector2D(x.subtract(o.x), y.subtract(o.y));
    }

    public BigVector2D add(BigVector2D o) {
        return new BigVector2D(x.add(o.x), y.add(o.y));
    }

    public BigVector2D move(Direction direction) {
        return new BigVector2D(x.add(new BigDecimal(direction.x())), y.add(new BigDecimal(direction.y())));
    }

    public static BigVector2D of(String x, String y) {
        return new BigVector2D(new BigDecimal(x), new BigDecimal(y));
    }

    public boolean isNeighbor(BigVector2D other) {
        for (Direction direction : Direction.ORTHOGONAL) {
            if (move(direction).equals(other)) {
                return true;
            }
        }

        return false;
    }

    public List<BigVector2D> neighbors(List<Direction> directions) {
        return directions.stream()
                .map(this::move)
                .toList();
    }
}
