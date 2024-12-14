package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Statistics;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day14 extends Puzzle {

    private static final Pattern pattern = Pattern.compile("p=([0-9]+),([0-9]+) v=(-*[0-9]+),(-*[0-9]+)");

    @Override
    protected boolean useExample() {
        return false;
    }

    private final static int DURATION = 100000;
    
    private int WIDTH() {
        return useExample() ? 11 : 101;
    }
    private int HEIGHT() {
        return useExample() ? 7 : 103;
    }

    @Override
    protected void run() {
        solve1();
        //solve2();
        solve2variance();
    }

    private record Robot(Vector2D pos, Vector2D velocity) {}

    private List<Robot> getRobotInput() {
        return getInput().stream().map(
            line -> {
                Matcher matcher = pattern.matcher(line);
                boolean found = matcher.find();
                assert found;
                return new Robot(
                        new Vector2D(parseInt(matcher.group(1)), parseInt(matcher.group(2))),
                        new Vector2D(parseInt(matcher.group(3)), parseInt(matcher.group(4)))
                );
            }
        ).toList();
    }

    private void solve2variance() {
        List<Robot> robots = getRobotInput();

        Character[][] data = new Character[HEIGHT()][WIDTH()];
        Grid<Character> grid = Grid.create(data);

        double minVariance = Double.MAX_VALUE;

        int duration = 100;

        while (true) {
            grid.fill('.');
            duration++;

            List<Integer> xData = new ArrayList<>();
            List<Integer> yData = new ArrayList<>();

            for (var robot : robots) {
                int x = (((robot.pos.x() + robot.velocity.x() * duration) % WIDTH()) + WIDTH()) % WIDTH();
                int y = (((robot.pos.y() + robot.velocity.y() * duration) % HEIGHT()) + HEIGHT()) % HEIGHT();

                xData.add(x);
                yData.add(y);

                Character cell = grid.get(x, y);
                if (cell == '.') {
                    grid.put(x,y, '1');
                } else {
                    grid.put(x, y, (char) (cell + 1));
                }
            }

            var xVariance = Statistics.variance(xData);
            var yVariance = Statistics.variance(yData);
            var varianceSum = xVariance + yVariance;
            if (varianceSum < minVariance) {
                minVariance = varianceSum;
                log.info("New minimum variance: " + minVariance + " @ " + duration);
            }
        }
    }

    private void solve2() {
        List<Robot> robots = getRobotInput();

        Character[][] data = new Character[HEIGHT()][WIDTH()];
        Grid<Character> grid = Grid.create(data);

        int duration = 100;
        boolean overlap;
        do {
            grid.fill('.');
            overlap = false;
            duration++;
            if (duration % 100 == 0) {
                log.info("Processing: " + duration);
            }
            for (var robot : robots) {
                int x = (((robot.pos.x() + robot.velocity.x() * duration) % WIDTH()) + WIDTH()) % WIDTH();
                int y = (((robot.pos.y() + robot.velocity.y() * duration) % HEIGHT()) + HEIGHT()) % HEIGHT();

                Character cell = grid.get(x, y);
                if (cell == '.') {
                    grid.put(x,y, '1');
                } else {
                    grid.put(x, y, (char) (cell + 1));
                    overlap = true;
                    break;
                }
            }
        } while (overlap);

        log.info(duration);
        log.info(grid);
    }

    private void solve1() {
        List<Robot> robots = getRobotInput();

        int[] quadrants = new int[4];

        int halfWidth = WIDTH() / 2;
        int halfHeight = HEIGHT() / 2;

        Character[][] data = new Character[HEIGHT()][WIDTH()];
        Grid<Character> grid = Grid.create(data);
        grid.fill('.');

        for (var robot : robots) {
            int x = (((robot.pos.x() + robot.velocity.x() * DURATION) % WIDTH()) + WIDTH()) % WIDTH();
            int y = (((robot.pos.y() + robot.velocity.y() * DURATION) % HEIGHT()) + HEIGHT()) % HEIGHT();

            Character cell = grid.get(x, y);
            if (cell == '.') {
                grid.put(x,y, '1');
            } else {
                grid.put(x, y, (char) (cell + 1));
            }

            if (x == halfWidth || y == halfHeight) continue;

            var i = ((x < halfWidth) ? 1 : 0) * 2 + (y < halfHeight ? 1 : 0);
            quadrants[i]++;
        }

        log.info(Arrays.toString(quadrants));
        log.info(Arrays.stream(quadrants).reduce(1, (a,b) -> a*b));

        log.info(grid);
    }

    public static void main(String[] ignoredArgs) {
        Day14 puzzle = new Day14();
        puzzle.run();
    }
}
