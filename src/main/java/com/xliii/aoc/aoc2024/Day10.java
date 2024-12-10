package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;

import java.util.HashSet;
import java.util.Set;

public class Day10 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private Grid<Character> grid;
    private Grid<Integer> scoreMap;

    private void solve2() {
        grid = Grid.create(getInput());
        scoreMap = Grid.create(new Integer[grid.getHeight()][grid.getWidth()]);
        for (var cell : scoreMap) {
            scoreMap.put(cell.x(), cell.y(), 0);
        }

        log.info(grid);

        var peaks = grid.findAll('9');
        for (var peak : peaks) {
            walkPeak(peak);
        }

        int total = 0;

        var starts = grid.findAll('0');
        for (var start : starts) {
            Integer rating = scoreMap.get(start.pos());
            log.warn(start + " -> " + rating);
            total += rating;

        }

        log.success(total);
    }

    private void solve1() {
        grid = Grid.create(getInput());
        scoreMap = Grid.create(new Integer[grid.getHeight()][grid.getWidth()]);
        for (var cell : scoreMap) {
            scoreMap.put(cell.x(), cell.y(), 0);
        }

        log.info(grid);

        int total = 0;

        var starts = grid.findAll('0');
        for (var start : starts) {
            var peaks = walkStart(start);
            log.warn(start + " -> " + peaks.size());
            total += peaks.size();
        }

        log.success(total);
    }

    private Set<Cell<Character>> walkStart(Cell<Character> peak) {
        scoreMap.put(peak.x(), peak.y(), 1);
        var peaks = new HashSet<Cell<Character>>();
        for (var neighbor : grid.neighbors(peak.x(), peak.y(), Direction.ORTHOGONAL)) {
            peaks.addAll(findEnd(neighbor, '1'));
        }
        return peaks;
    }

    private Set<Cell<Character>> findEnd(Cell<Character> cell, char height) {
        if (!cell.value().equals(height)) {
            return Set.of();
        }

        if (height == '9') {
            return Set.of(cell);
        }

        var peaks = new HashSet<Cell<Character>>();

        for (var neighbor : grid.neighbors(cell.x(), cell.y(), Direction.ORTHOGONAL)) {
            peaks.addAll(findEnd(neighbor, (char) (height + 1)));
        }
        return peaks;
    }

    private void walkPeak(Cell<Character> peak) {
        scoreMap.put(peak.x(), peak.y(), 1);
        for (var neighbor : grid.neighbors(peak.x(), peak.y(), Direction.ORTHOGONAL)) {
            walk(neighbor, '8');
        }
    }

    private void walk(Cell<Character> cell, char height) {
        if (height < '0') return;

        if (cell.value().equals(height)) {
            int currentScore = scoreMap.get(cell.x(), cell.y()) + 1;
            scoreMap.put(cell.x(), cell.y(), currentScore);
            for (var neighbor : grid.neighbors(cell.x(), cell.y(), Direction.ORTHOGONAL)) {
                walk(neighbor, (char) (height - 1));
            }
        }
    }

    public static void main(String[] ignoredArgs) {
        Day10 puzzle = new Day10();
        puzzle.run();
    }
}
