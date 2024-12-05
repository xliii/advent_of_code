package com.xliii.aoc.aoc2024.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

public class Grid<T> {

    public final T[][] data;

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

    public T neighbor(int x, int y, Direction direction) {
        int nx = x + direction.getX();
        int ny = y + direction.getY();
        return get(nx, ny);
    }

    public boolean isEdge(int x, int y) {
        return (x == 0 || x == width - 1) || (y == 0 || y == height - 1);
    }

    public boolean inBounds(int x, int y) {
        return !outOfBounds(x, y);
    }

    public boolean outOfBounds(int x, int y) {
        if (x < 0 || x >= width) return true;

        if (y < 0 || y >= height) return true;

        return false;
    }

    public static Grid<Character> create(List<String> data) {
        Character[][] grid = data.stream()
                .map(
                    s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                ).toArray(Character[][]::new);
        return Grid.create(grid);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(data[y][x]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
