package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.Map;
import java.util.Optional;

//run with -Xss8m
public class Day16 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private static final char WALL = '#';
    private static final char EMPTY = '.';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char PATH = 'O';
    private static final char CURRENT = 'X';

    private static final Map<Character, Color> COLOR_MAP = Map.of(
            WALL, Color.BLUE,
            START, Color.GREEN,
            END, Color.YELLOW,
            EMPTY, Color.BLACK,
            CURRENT, Color.RED
    );

    private static final ScoreCell ZERO = new ScoreCell(0);

    private static final Map<ScoreCell, Color> SCORE_CELL_COLOR_MAP = Map.of(
            ZERO, Color.BLACK
    );

    private Grid<Character> grid;
    private Grid<ScoreCell> scoreMap;

    private record ScoreCell(int value) {
        @Override
        public String toString() {
            return String.format("[%05d]", value);
        }
    }

    private void solve2() {
        grid = Grid.create(getInput(), COLOR_MAP);

        var end = grid.findOne(END).orElseThrow();
        walkBack(end, score(end) + 1, 0);

        log.info(grid);

        log.info("Path: " + (grid.findAll(PATH).size() + 1)); // add START
    }

    private int score(Cell<Character> cell) {
        return scoreMap.get(cell.pos()).value;
    }

    private boolean isPath(int current, int score, int prevScore) {
        return (score - current == 1) || (score - current == 1001) || (prevScore - current == 2);
    }

    private void walkBack(Cell<Character> cell, int score, int prevScore) {
        var cellValue = grid.get(cell.pos());
        grid.put(cell.x(), cell.y(), CURRENT);
        //log.info(grid);
        if (!isPath(score(cell), score, prevScore)) {
            grid.put(cell.x(), cell.y(), cellValue);
            return;
        }

        grid.put(cell.x(), cell.y(), PATH);

        for (var neighbor : grid.neighbors(cell.x(), cell.y())) {
            if (neighbor.value() == EMPTY) {
                walkBack(neighbor, score(cell), score);
            }
        }
    }

    private void solve1() {
        grid = Grid.create(getInput(), COLOR_MAP);
        scoreMap = Grid.create(new ScoreCell[grid.getHeight()][grid.getWidth()], SCORE_CELL_COLOR_MAP);
        scoreMap.fill(new ScoreCell(Integer.MAX_VALUE));
        log.info(grid);

        for (var wall : grid.findAll(WALL)) {
            scoreMap.put(wall.x(), wall.y(), ZERO);
        }

        var start = grid.findOne(START).orElseThrow();
        scoreMap.put(start.x(), start.y(), new ScoreCell(-1));

        var end = grid.findOne(END).orElseThrow();

        for (var direction : Direction.ORTHOGONAL) {
            Optional<Cell<Character>> neighbor = grid.neighborSafe(start.x(), start.y(), direction);
            if (neighbor.isEmpty()) continue;

            var n = neighbor.get();
            if (n.value() == WALL) {
                continue;
            }

            walk(start, n, Direction.EAST, 0);
        }

        log.info(scoreMap);

        log.success(score(end));
    }

    private void walk(Cell<Character> prev, Cell<Character> current, Direction direction, int score) {
        var newDirection = prev.directionTo(current).orElseThrow();
        if (newDirection != direction) {
            score += 1000;
            direction = newDirection;
        }
        score++;

        int x = current.x();
        int y = current.y();
        var prevScore = score(current);
        if (prevScore <= score) return;

        scoreMap.put(x, y, new ScoreCell(score));

        if (!current.value().equals(END)) {
            grid.put(x, y, direction.sign());
        }

        //log.info(grid);

        if (current.value().equals(END)) {
            log.success("New best for END: " + score);
            return;
        }

        for (var neighbor : grid.neighbors(x, y)) {
            if (neighbor.equals(prev)) {
                continue;
            }

            if (neighbor.value() == WALL) {
                continue;
            }

            walk(current, neighbor, direction, score);
        }
    }

    public static void main(String[] ignoredArgs) {
        Day16 puzzle = new Day16();
        puzzle.run();
    }
}
