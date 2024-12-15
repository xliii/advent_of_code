package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day15 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private static char WALL = '#';
    private static char BOX = 'O';
    private static char ROBOT = '@';
    private static char EMPTY = '.';

    private static Map<Character, Color> COLOR_MAP = Map.of(
        WALL, Color.BLUE,
        BOX, Color.GREEN,
        ROBOT, Color.YELLOW,
        EMPTY, Color.BLACK
    );

    private Grid<Character> grid;
    private List<Direction> moves;

    private void solve2() {

    }

    private void solve1() {
        readInput();
        Cell<Character> robot = grid.findOne(ROBOT).orElseThrow();
        for (Direction move : moves) {
            robot = moveRobot(robot, move);
            //log.info(grid);
        }

        log.info(gps());
    }

    private int gps() {
        return grid.findAll(BOX).stream().map(box -> box.y() * 100 + box.x()).reduce(Integer::sum).orElseThrow();
    }

    private Cell<Character> moveRobot(Cell<Character> robot, Direction direction) {
        var next = grid.neighbor(robot.x(), robot.y(), direction);
        if (next.value() == WALL) {
            return robot;
        }

        if (next.value() == EMPTY) {
            grid.put(robot.x(), robot.y(), EMPTY);
            grid.put(next.x(), next.y(), ROBOT);
            return new Cell<>(next.x(), next.y(), ROBOT);
        }

        // try moving box
        int movedBoxes = 1;
        var afterBox = grid.neighbor(next.x(), next.y(), direction);
        while (afterBox.value() != EMPTY) {
            if (afterBox.value() == WALL) {
                return robot;
            }

            // another box
            afterBox = grid.neighbor(afterBox.x(), afterBox.y(), direction);
            movedBoxes++;
        }

        grid.put(robot.x(), robot.y(), EMPTY);
        grid.put(next.x(), next.y(), ROBOT);
        var movedBoxPos = next.pos().move(direction);
        for (int i = 0; i < movedBoxes; i++) {
            grid.put(movedBoxPos.x(), movedBoxPos.y(), BOX);
            movedBoxPos = movedBoxPos.move(direction);
        }

        return new Cell<>(next.x(), next.y(), ROBOT);
    }

    private void readInput() {
        var input = getInput().stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.partitioningBy(line -> line.startsWith(String.valueOf(WALL))));

        grid = Grid.create(input.get(true), COLOR_MAP);
        moves = input.get(false).stream()
                .flatMap(
                    line -> line.chars().mapToObj(character -> Direction.bySign((char) character))
                ).toList();

        log.info(grid);
        log.info(moves);
    }

    public static void main(String[] ignoredArgs) {
        Day15 puzzle = new Day15();
        puzzle.run();
    }
}
