package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day21 extends Puzzle {

    /*
    Keypad:
    789
    456
    123
     0A

    Directional pad:
     ^A
    <v>

    */

    private static final Character EMPTY = ' ';
    private static final Vector2D KEYPAD_EMPTY = new Vector2D(0,3);
    private static final Vector2D DIRECTIONAL_EMPTY = new Vector2D(0, 0);

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        fillDirectionalMap();
        fillKeypadMap();

        //solve1();
        solve2();
    }

    private final Map<Path, List<String>> KEYPAD_MOVEMENTS = new HashMap<>();
    private final Map<Path, List<String>> DIRECTIONAL_MOVEMENTS = new HashMap<>();

    private void solve2() {
        solve(25);
    }

    private record Path(Character from, Character to) {}

    private record PathWithDepth(Character from, Character to, long depth) {}

    private List<String> keypadSequence(Vector2D from, Vector2D to) {
        //TODO: Find all possible paths, while eliminating invalid (outside field)
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        String horizontalFirst = String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                'A';


        String verticalFirst = String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                'A';

        //remove path through empty
        if (new Vector2D(to.x(), from.y()).equals(KEYPAD_EMPTY)) {
            return List.of(verticalFirst);
        } else if (new Vector2D(from.x(), to.y()).equals(KEYPAD_EMPTY)) {
            return List.of(horizontalFirst);
        } else return List.of(horizontalFirst, verticalFirst);
    }

    private List<String> directionalSequence(Vector2D from, Vector2D to) {
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        String verticalFirst = String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                'A';

        String horizontalFirst = String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                'A';

        //remove path through empty
        if (new Vector2D(to.x(), from.y()).equals(DIRECTIONAL_EMPTY)) {
            return List.of(verticalFirst);
        } else if (new Vector2D(from.x(), to.y()).equals(DIRECTIONAL_EMPTY)) {
            return List.of(horizontalFirst);
        } else return List.of(horizontalFirst, verticalFirst);
    }

    private void fillKeypadMap() {
        var grid = Grid.create(List.of("789", "456", "123", " 0A"));

        for (var from : grid) {
            if (from.value().equals(EMPTY)) continue;
            for (var to : grid) {
                if (to.value().equals(EMPTY)) continue;
                var paths = keypadSequence(from.pos(), to.pos());
                KEYPAD_MOVEMENTS.put(new Path(from.value(), to.value()), paths.stream().distinct().toList());
                //log.info(from.value() + " -> " + to.value() + " - " + path);
            }
        }
    }

    private void fillDirectionalMap() {
        var grid = Grid.create(List.of(" ^A", "<v>"));

        for (var from : grid) {
            if (from.value().equals(EMPTY)) continue;
            for (var to : grid) {
                if (to.value().equals(EMPTY)) continue;
                var paths = directionalSequence(from.pos(), to.pos());
                DIRECTIONAL_MOVEMENTS.put(new Path(from.value(), to.value()), paths.stream().distinct().toList());
                //log.info(from.value() + " -> " + to.value() + " - " + path);
            }
        }
    }

    private void solve(long robots) {
        long total = 0;

        for (var line : getInput()) {
            log.info(line);
            long cost = codeCost(line, robots);

            long number = Long.parseLong(line.substring(0, line.length() - 1));
            total += number * cost;
            log.error("Complexity: " + cost + " * " + number + " = " + number * cost);
        }

        log.success(total);
    }

    private void solve1() {
        solve(2);
    }

    private final Map<PathWithDepth, Long> UNWRAP_CACHE = new ConcurrentHashMap<>();

    private long memoizedCost(Character from, Character to, long depth, long maxDepth) {
        var pathWithDepth = new PathWithDepth(from, to, depth);
        if (!UNWRAP_CACHE.containsKey(pathWithDepth)) {
            UNWRAP_CACHE.put(pathWithDepth, cost(from, to, depth, maxDepth));
        }

        return UNWRAP_CACHE.get(pathWithDepth);
    }

    private long codeCost(String line, long depth) {
        line = "A" + line;
        long cost = 0;
        for (int i = 0; i < line.length() - 1; i++) {
            var from = line.charAt(i);
            var to = line.charAt(i + 1);
            cost += cost(from, to, depth, depth);
        }
        return cost;
    }

    private long cost(Character from, Character to, long depth, long maxDepth) {
        var lookup = (depth == maxDepth) ? KEYPAD_MOVEMENTS : DIRECTIONAL_MOVEMENTS;
        if (depth == 0) {
            return lookup.get(new Path(from, to))
                    .stream().map(String::length)
                    .min(Comparator.naturalOrder()).orElseThrow();
        }

        var paths = lookup.get(new Path(from, to));
        var smallest_cost = Long.MAX_VALUE;

        for (var path : paths) {
            path = "A" + path;
            var cost = 0L;
            for (int i = 0; i < path.length() - 1; i++) {
                from = path.charAt(i);
                to = path.charAt(i + 1);
                cost += memoizedCost(from, to, depth - 1, maxDepth);
            }

            smallest_cost = Math.min(smallest_cost, cost);
        }

        return smallest_cost;
    }

    public static void main(String[] ignoredArgs) {
        Day21 puzzle = new Day21();
        puzzle.run();
    }
}
