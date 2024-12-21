package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.*;

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

    @Override
    protected boolean useExample() {
        return true;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private final Map<Path, List<String>> KEYPAD_MOVEMENTS = new HashMap<>();
    private final Map<Path, List<String>> DIRECTIONAL_MOVEMENTS = new HashMap<>();

    private void solve2() {
    }

    private record Path(Character from, Character to) {
    }

    private List<String> keypadSequence(Vector2D from, Vector2D to) {
        //TODO: Find all possible paths
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        String horizontalFirst = String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                'A';


        String verticalFirst = String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                'A';

        return List.of(horizontalFirst, verticalFirst);
    }

    private List<String> directionalSequence(Vector2D from, Vector2D to) {
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        String verticalFirst = String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                'A';

        String horizontalFirst = String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)) +
                String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)) +
                String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))) +
                String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))) +
                'A';

        return List.of(verticalFirst, horizontalFirst);
    }

    private void fillKeypadMap() {
        var grid = Grid.create(List.of("789", "456", "123", " 0A"));

        for (var from : grid) {
            if (from.value().equals(EMPTY)) continue;
            for (var to : grid) {
                if (to.value().equals(EMPTY)) continue;
                var paths = keypadSequence(from.pos(), to.pos());
                KEYPAD_MOVEMENTS.put(new Path(from.value(), to.value()), paths);
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
                DIRECTIONAL_MOVEMENTS.put(new Path(from.value(), to.value()), paths);
                //log.info(from.value() + " -> " + to.value() + " - " + path);
            }
        }
    }

    private void solve1() {
        fillDirectionalMap();
        fillKeypadMap();

        int total = 0;

        for (var line : getInput()) {
            log.info(line);
            var first = unwrapSequence("A" + line, KEYPAD_MOVEMENTS);
            log.error(first);
            var second = first.stream().flatMap(sequence -> unwrapSequence("A" + sequence, DIRECTIONAL_MOVEMENTS).stream()).toList();
            log.warn(second);
            var third = second.stream().flatMap(sequence -> unwrapSequence("A" + sequence, DIRECTIONAL_MOVEMENTS).stream()).toList();
            log.success(third);

            var shortestThird = third.stream().min(Comparator.comparingInt(String::length)).orElseThrow();
            log.success(shortestThird);
            log.success(shortestThird.length());



//            var second = unwrapSequence(first, DIRECTIONAL_MOVEMENTS);
//            log.warn(second);
//            var third = unwrapSequence(second, DIRECTIONAL_MOVEMENTS);
//            log.success(third);

//            int number = Integer.parseInt(line.substring(0, line.length() - 1));
//            int length = third.length();
//            total += number * length;
//            log.error("Complexity: " + length + " * " + number + " = " + number * length);
        }

        log.success(total);
    }

    private List<String> unwrapSequence(String sequence, Map<Path, List<String>> lookup) {
        if (sequence.length() < 2) {
            return List.of("", "");
        }

        List<String> result = new ArrayList<>();
        var from = sequence.charAt(0);
        var to = sequence.charAt(1);
        List<String> firstOptions = lookup.get(new Path(from, to));
        var sequences = unwrapSequence(sequence.substring(1), lookup);
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
