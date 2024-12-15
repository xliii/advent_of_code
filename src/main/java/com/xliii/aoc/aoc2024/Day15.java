package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.Interactive;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        //solve1();
        solve2();
        //solveInteractive();
    }

    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';
    private static final char EMPTY = '.';
    private static final char BOX_LEFT = '[';
    private static final char BOX_RIGHT = ']';

    private static final Map<Character, Color> COLOR_MAP = Map.of(
        WALL, Color.BLUE,
        BOX, Color.GREEN, BOX_LEFT, Color.GREEN, BOX_RIGHT, Color.GREEN,
        ROBOT, Color.YELLOW,
        EMPTY, Color.BLACK
    );

    private Grid<Character> grid;
    private List<Direction> moves;
    private Cell<Character> robot;

    private void solveInteractive() {
        readInput2();
        robot = grid.findOne(ROBOT).orElseThrow();
        Interactive.run(input -> {
            var direction = Direction.byAlias(input);
            if (direction.isEmpty()) {
                log.error("Invalid direction: " + input);
                return;
            }

            robot = Day15.this.moveRobot2(robot, direction.get());
            log.info(direction);
            log.info(grid);
        });
    }

    private void solve2() {
        readInput2();
        Cell<Character> robot = grid.findOne(ROBOT).orElseThrow();
        int turn = 0;
        for (Direction move : moves) {
            robot = moveRobot2(robot, move);
            //log.info(++turn + ":" + move);
            //log.info(grid);
        }

        log.info(grid);
        log.info(gps2());
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

    private int gps2() {
        return grid.findAll(BOX_LEFT).stream().map(box -> box.y() * 100 + box.x()).reduce(Integer::sum).orElseThrow();
    }

    private int gps() {
        return grid.findAll(BOX).stream().map(box -> box.y() * 100 + box.x()).reduce(Integer::sum).orElseThrow();
    }

    private Cell<Character> moveRobot2(Cell<Character> robot, Direction direction) {
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
        if (direction == Direction.EAST || direction == Direction.WEST) {
            int depth = 1;
            var afterBox = grid.neighbor(next.x(), next.y(), direction);
            while (afterBox.value() != EMPTY) {
                if (afterBox.value() == WALL) {
                    return robot;
                }

                // another box
                afterBox = grid.neighbor(afterBox.x(), afterBox.y(), direction);
                depth++;
            }

            grid.put(robot.x(), robot.y(), EMPTY);
            grid.put(next.x(), next.y(), ROBOT);
            var movedBoxPos = next.pos().move(direction);
            for (int i = 0; i < depth; i++) {
                char box = (i % 2 == 0) ^ (direction == Direction.WEST) ? BOX_LEFT : BOX_RIGHT;
                grid.put(movedBoxPos.x(), movedBoxPos.y(), box);
                movedBoxPos = movedBoxPos.move(direction);
            }

            return new Cell<>(next.x(), next.y(), ROBOT);
        } else {
            int depth = 1;
            List<Cell<Character>> nextChecks = new ArrayList<>();
            List<Cell<Character>> toMove = new ArrayList<>();
            nextChecks.add(next);
            while (nextChecks.stream().anyMatch(check -> check.value() != EMPTY)) {
                if (nextChecks.stream().anyMatch(check -> check.value() == WALL)) {
                    return robot;
                }

                toMove.addAll(nextChecks.stream().filter(check -> check.value() != EMPTY).toList());

                nextChecks = nextChecks.stream().filter(check -> !check.value().equals(EMPTY)).flatMap(check ->
                    Stream.of(
                        grid.neighbor(check.x(), check.y(), direction),
                        grid.neighbor(check.x(), check.y(), direction.rotate45(
                                check.value() == BOX_LEFT ^ direction == Direction.SOUTH)
                        )
                    )).toList();

                // another box
                depth++;
            }

            //add other halves of boxes to move
            toMove = toMove.stream().flatMap(cell -> {
                if (cell.value() == BOX_LEFT) {
                    return Stream.of(cell, new Cell<>(cell.x() + 1, cell.y(), BOX_RIGHT));
                } else {
                    return Stream.of(cell, new Cell<>(cell.x() - 1, cell.y(), BOX_LEFT));
                }
            }).toList();

            //log.error("Can move " + direction + " - depth=" + depth);
            //log.error("Move: " + toMove);

            for (var box : toMove.reversed()) {
                grid.put(box.x(), box.y(), EMPTY);
                grid.put(box.x(), box.y() + direction.y(), box.value());
            }

            grid.put(robot.x(), robot.y(), EMPTY);
            grid.put(next.x(), next.y(), ROBOT);

            return new Cell<>(next.x(), next.y(), ROBOT);
        }
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

    private void readMoves(List<String> moves) {
        this.moves = moves.stream()
                .flatMap(
                        line -> line.chars().mapToObj(character -> Direction.bySign((char) character).orElseThrow())
                ).toList();

        log.info(this.moves);
    }

    private String widen(String line) {
        line = line.replaceAll("#", "##")
                .replaceAll("O", "[]")
                .replaceAll("\\.", "..")
                .replaceAll("@", "@.");
        return line;
    }

    private void readInput2() {
        var input = getInput().stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.partitioningBy(line -> line.startsWith(String.valueOf(WALL))));

        var wideGrid = input.get(true).stream().map(this::widen).toList();

        grid = Grid.create(wideGrid, COLOR_MAP);

        readMoves(input.get(false));

        log.info(grid);
    }

    private void readInput() {
        var input = getInput().stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.partitioningBy(line -> line.startsWith(String.valueOf(WALL))));

        grid = Grid.create(input.get(true), COLOR_MAP);

        readMoves(input.get(false));

        log.info(grid);
    }

    public static void main(String[] ignoredArgs) {
        Day15 puzzle = new Day15();
        puzzle.run();
    }
}
