package com.xliii.aoc.util.grid;

import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

public class Grid<T> implements Iterable<Cell<T>> {

    public final T[][] data;

    private final int width;
    private final int height;
    private final Map<T, Color> colorMap;

    private Grid(T[][] data, Map<T, Color> colorMap) {
        this.data = data;
        this.width = data[0].length;
        this.height = data.length;
        this.colorMap = colorMap;
    }

    public int count(T value) {
        int total = 0;
        for (Cell<T> cell : this) {
            if (cell.value().equals(value)) {
                total++;
            }
        }
        return total;
    }

    public Grid<T> copy() {
        @SuppressWarnings("unchecked")
        T[][] dataCopy = (T[][]) Arrays.stream(data)
            .map(row -> row.clone())
            .toArray(Object[][]::new);
        return create(dataCopy, colorMap);
    }

    public T put(int x, int y, T value) {
        if (outOfBounds(x, y)) {
            throw new GridException(MessageFormat.format("Out of bounds: ({0},{1})", x, y));
        }

        T prev = data[y][x];
        data[y][x] = value;
        return prev;
    }

    public T get(Vector2D pos) {
        return get(pos.x(), pos.y());
    }

    public T get(int x, int y) {
        if (outOfBounds(x, y)) {
            throw new GridException(MessageFormat.format("Out of bounds: ({0},{1})", x, y));
        }

        return data[y][x];
    }

    public List<Cell<T>> neighbors(int x, int y, List<Direction> directions) {
        return directions.stream()
                .map(d -> neighborSafe(x, y, d))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Optional<Cell<T>> neighborSafe(int x, int y, Direction direction) {
        int nx = x + direction.x();
        int ny = y + direction.y();
        if (inBounds(nx, ny)) {
            return Optional.of(new Cell<>(nx, ny, get(nx, ny)));
        } else {
            return Optional.empty();
        }
    }

    public Cell<T> neighbor(int x, int y, Direction direction) {
        int nx = x + direction.x();
        int ny = y + direction.y();
        return new Cell<>(nx, ny, get(nx, ny));
    }

    public boolean isEdge(int x, int y) {
        return (x == 0 || x == width - 1) || (y == 0 || y == height - 1);
    }

    public boolean inBounds(Vector2D pos) {
        return inBounds(pos.x(), pos.y());
    }

    public boolean outOfBounds(Vector2D pos) {
        return outOfBounds(pos.x(), pos.y());
    }

    public boolean inBounds(int x, int y) {
        return !outOfBounds(x, y);
    }

    public boolean outOfBounds(int x, int y) {
        if (x < 0 || x >= width) return true;

        if (y < 0 || y >= height) return true;

        return false;
    }

    public Set<T> distinctValues() {
        Set<T> result = new HashSet<>();
        for (Cell<T> cell : this) {
            result.add(cell.value());
        }
        return result;
    }

    public List<Cell<T>> findAll(T value) {
        List<Cell<T>> result = new ArrayList<>();
        for (Cell<T> cell : this) {
            if (cell.value().equals(value)) {
                result.add(cell);
            }
        }
        return result;
    }

    public void fill(T value) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = value;
            }
        }
    }

    public Optional<Cell<T>> findOne(T value) {
        for (Cell<T> cell : this) {
            if (cell.value().equals(value)) {
                return Optional.of(cell);
            }
        }

        return Optional.empty();
    }

    public static Grid<Character> create(List<String> data) {
        return create(data, new HashMap<>());
    }

    public static Grid<Character> create(List<String> data, Map<Character, Color> colorMap) {
        Character[][] grid = data.stream()
                .map(
                    s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                ).toArray(Character[][]::new);
        return Grid.create(grid, colorMap);
    }

    public static <T> Grid<T> create(T[][] data) {
       return create(data, new HashMap<>());
    }

    public static <T> Grid<T> create(T[][] data, Map<T, Color> colorMap) {
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

        return new Grid<>(data, colorMap);
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
                T value = data[y][x];
                if (colorMap.containsKey(value)) {
                    sb.append(colorMap.get(value).text(value));
                } else {
                    sb.append(value);
                }

            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Cell<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < width * height;
            }

            @Override
            public Cell<T> next() {
                int x = index % width;
                int y = index / width;
                T value = get(x, y);
                index++;
                return new Cell<>(x, y, value);
            }
        };
    }
}
