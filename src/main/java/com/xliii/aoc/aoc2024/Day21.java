package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<Path, String> KEYPAD_MOVEMENTS = new HashMap<>();
    private final Map<Path, String> DIRECTIONAL_MOVEMENTS = new HashMap<>();

    private void solve2() {
    }

    private record Path (Character from, Character to) {}

    private String keypadSequence(Vector2D from, Vector2D to) {
        //TODO: Support multiple paths
        StringBuilder sb = new StringBuilder();
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        sb.append(String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)));
        sb.append(String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))));
        sb.append(String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))));
        sb.append(String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)));
        sb.append('A');
        return sb.toString();
    }

    private String directionalSequence(Vector2D from, Vector2D to) {
        StringBuilder sb = new StringBuilder();
        var x = to.x() - from.x();
        var y = to.y() - from.y();

        sb.append(String.valueOf(Direction.EAST.sign()).repeat(Math.max(0, x)));
        sb.append(String.valueOf(Direction.SOUTH.sign()).repeat(Math.max(0, y)));
        sb.append(String.valueOf(Direction.WEST.sign()).repeat(Math.abs(Math.min(0, x))));
        sb.append(String.valueOf(Direction.NORTH.sign()).repeat(Math.abs(Math.min(0, y))));
        sb.append('A');
        return sb.toString();
    }

    private void fillKeypadMap() {
        var grid = Grid.create(List.of("789", "456", "123", " 0A"));

        for (var from : grid) {
            if (from.value().equals(EMPTY)) continue;
            for (var to : grid) {
                if (to.value().equals(EMPTY)) continue;
                String path = keypadSequence(from.pos(), to.pos());
                KEYPAD_MOVEMENTS.put(new Path(from.value(), to.value()), path);
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
                String path = directionalSequence(from.pos(), to.pos());
                DIRECTIONAL_MOVEMENTS.put(new Path(from.value(), to.value()), path);
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
            var first = unwrapSequence(line, KEYPAD_MOVEMENTS);
            log.error(first);
            var second = unwrapSequence(first, DIRECTIONAL_MOVEMENTS);
            log.warn(second);
            var third = unwrapSequence(second, DIRECTIONAL_MOVEMENTS);
            log.success(third);

            int number = Integer.parseInt(line.substring(0, line.length() - 1));
            int length = third.length();
            total += number * length;
            log.error("Complexity: " + length + " * " + number + " = " + number * length);
        }

        log.success(total);

        String unwrap1 = unwrapSequence("^A<<^^A>>AvvvA", DIRECTIONAL_MOVEMENTS);
        System.out.println(unwrap1);
        System.out.println(unwrapSequence(unwrap1, DIRECTIONAL_MOVEMENTS));
    }

    private String unwrapSequence(String sequence, Map<Path, String> lookup) {
        sequence = "A" + sequence;
        var sb = new StringBuilder();
        for (int i = 0; i < sequence.length() - 1; i++) {
            var from = sequence.charAt(i);
            var to = sequence.charAt(i + 1);
            String path = lookup.get(new Path(from, to));
            sb.append(path);
        }
        return sb.toString();
    }

    public static void main(String[] ignoredArgs) {
        Day21 puzzle = new Day21();
        puzzle.run();
    }
}
