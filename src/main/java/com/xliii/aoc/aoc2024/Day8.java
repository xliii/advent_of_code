package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Sets;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private void solve1() {
        Grid<Character> grid = Grid.create(getInput());
        log.info(grid);

        Set<Character> distinct = grid.distinctValues();
        distinct.remove('.');

        Set<Vector2D> result = new HashSet<>();

        for (Character frequency : distinct) {
            Set<Cell<Character>> antennas = grid.findAll(frequency);
            log.info(frequency + " -> " + antennas);

            Set<Set<Cell<Character>>> pairs = Sets.combinations(antennas, 2);
            for (Set<Cell<Character>> pair : pairs) {
                List<Cell<Character>> cells = new ArrayList<>(pair);
                Cell<Character> a = cells.getFirst();
                Cell<Character> b = cells.getLast();

                log.info("Processing " + a + " & " + b);

                antinodes(a.pos(), b.pos())
                        .stream().filter(grid::inBounds)
                        .forEach(result::add);
            }
        }
        log.info(result);
        log.success(result.size());
    }

    private Set<Vector2D> antinodes(Vector2D a, Vector2D b) {
        Set<Vector2D> antinodes = new HashSet<>();
        log.info(a);
        log.info(b);
        Vector2D diff = a.subtract(b);
        log.success(diff);
        Vector2D antinode1 = a.add(diff);
        Vector2D antinode2 = b.subtract(diff);

        log.error(antinode1);
        log.error(antinode2);
        antinodes.add(antinode1);
        antinodes.add(antinode2);

        return antinodes;
    }

    private Set<Vector2D> harmonics(Vector2D a, Vector2D b, Grid<Character> grid) {
        Set<Vector2D> harmonics = new HashSet<>();
        log.info(a);
        log.info(b);
        Vector2D diff = a.subtract(b);
        log.success(diff);

        //add self
        harmonics.add(a);
        harmonics.add(b);

        //positive harmonics
        Vector2D positiveHarmonic = a.add(diff);
        while (grid.inBounds(positiveHarmonic)) {
            harmonics.add(positiveHarmonic);
            positiveHarmonic = positiveHarmonic.add(diff);
        }

        //Negative harmonics
        Vector2D negativeHarmonic = b.subtract(diff);
        while (grid.inBounds(negativeHarmonic)) {
            harmonics.add(negativeHarmonic);
            negativeHarmonic = negativeHarmonic.subtract(diff);
        }

        return harmonics;
    }

    private void solve2() {
        Grid<Character> grid = Grid.create(getInput());
        log.info(grid);

        Set<Character> distinct = grid.distinctValues();
        distinct.remove('.');

        Set<Vector2D> result = new HashSet<>();

        for (Character frequency : distinct) {
            Set<Cell<Character>> antennas = grid.findAll(frequency);
            log.info(frequency + " -> " + antennas);

            Set<Set<Cell<Character>>> pairs = Sets.combinations(antennas, 2);
            for (Set<Cell<Character>> pair : pairs) {
                List<Cell<Character>> cells = new ArrayList<>(pair);
                Cell<Character> a = cells.getFirst();
                Cell<Character> b = cells.getLast();

                log.info("Processing " + a + " & " + b);

                result.addAll(harmonics(a.pos(), b.pos(), grid));
            }
        }
        log.info(result);
        log.success(result.size());
    }

    public static void main(String[] ignoredArgs) {
        Day8 puzzle = new Day8();
        puzzle.run();
    }
}
