package com.xliii.aoc.aoc2024.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

public class Grid<T> {

    private final T[][] data;

    private final int width;
    private final int height;


    private Grid(T[][] data) {
        this.data = data;
        this.width = data.length;
        this.height = data[0].length;
    }

    public T get(int x, int y) {
        if (outOfBounds(x, y)) {
            throw new GridException(MessageFormat.format("Out of bounds: ({0},{1})", x, y));
        }

        return data[y][x];
    }

    public boolean isEdge(int x, int y) {
        return (x == 0 || x == width - 1) && (y == 0 || y == height - 1);
    }

    public boolean inBounds(int x, int y) {
        return !outOfBounds(x, y);
    }

    public boolean outOfBounds(int x, int y) {
        if (x < 0 || x >= width) return true;
        if (y < 0 || y >= height) return true;

        return false;
    }

    public static <T> Grid<T> create(T[][] data) {
        if (data.length == 0) {
            throw new GridException("Grid height cannot be 0");
        }

        List<Integer> widths = Stream.of(data).map(row -> row.length).distinct().toList();
        if (widths.size() > 1) {
            throw new GridException("Grid width should be same, got " + widths);
        }

        Integer width = widths.getFirst();
        if (width == 0) {
            throw new GridException("Grid width cannot be 0");
        }

        return new Grid<>(data);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
