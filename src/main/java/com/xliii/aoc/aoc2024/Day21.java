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

    private void solve(int robots) {
        int total = 0;

        for (var line : getInput()) {
            log.info(line);
            var first = unwrapSequence("A" + line, KEYPAD_MOVEMENTS);
            log.error(first);
            var next = first;
            for (int i = 0; i < robots; i++) {
                next = next.stream().flatMap(sequence -> unwrapSequence("A" + sequence, DIRECTIONAL_MOVEMENTS).stream()).toList();
                log.warn(next);
            }
            var shortest = next.stream().min(Comparator.comparingInt(String::length)).orElseThrow();
            log.success(shortest);
            var length = shortest.length();

            int number = Integer.parseInt(line.substring(0, line.length() - 1));
            total += number * length;
            log.error("Complexity: " + length + " * " + number + " = " + number * length);
        }

        log.success(total);
    }

    private void solve1() {
        solve(2);
    }

    private Map<String, List<String>> UNWRAP_CACHE = new ConcurrentHashMap<>();

    private List<String> memoizedUnwrapSequence(String sequence, Map<Path, List<String>> lookup) {
        if (!UNWRAP_CACHE.containsKey(sequence)) {
            UNWRAP_CACHE.put(sequence, unwrapSequence(sequence, lookup));
        }

        return UNWRAP_CACHE.get(sequence);
    }

    private List<String> unwrapSequence(String sequence, Map<Path, List<String>> lookup) {
        //TODO: somehow prune tree? or calculate length without actually going through all of options
        if (sequence.length() < 2) {
            return List.of("", "");
        }

        List<String> result = new ArrayList<>();
        var from = sequence.charAt(0);
        var to = sequence.charAt(1);
        List<String> firstOptions = lookup.get(new Path(from, to));
        var sequences = memoizedUnwrapSequence(sequence.substring(1), lookup);
        for (var first : firstOptions) {
            result.addAll(sequences.stream().map(s -> first + s).toList());
        }

        return result.stream().distinct().toList();
    }

    public static void main(String[] ignoredArgs) {
        Day21 puzzle = new Day21();
        puzzle.run();
    }
}
