package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Color;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.Vector2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day14 extends Puzzle {

    private static final Pattern pattern = Pattern.compile("p=([0-9]+),([0-9]+) v=(-*[0-9]+),(-*[0-9]+)");

    @Override
    protected boolean useExample() {
        return false;
    }

    private final int DURATION = 100;
    
    private int WIDTH() {
        return useExample() ? 11 : 101;
    }
    private int HEIGHT() {
        return useExample() ? 7 : 103;
    }

    @Override
    protected void run() {
        solve1();
        solve1Old();
        solve2();
    }

    private void solve2() {

    }

    private record Robot(Vector2D pos, Vector2D velocity) {}

    private List<Robot> getRobotInput() {
        return getInput().stream().map(
            line -> {
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                return new Robot(
                        new Vector2D(parseInt(matcher.group(1)), parseInt(matcher.group(2))),
                        new Vector2D(parseInt(matcher.group(3)), parseInt(matcher.group(4)))
                );
            }
        ).toList();
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

    private void solve1Old() {
        List<Robot> robots = getRobotInput();
        for (int i = 0; i < DURATION; i++) {
            robots = move(robots);
            //printRobots(robots, false, false);

        }
        printRobots(robots, false, false);
        log.error(score(robots));
    }

    private Direction quadrant(int x, int y) {
        //middle filtered out already
        if (x < WIDTH() / 2) {
            if (y < HEIGHT() / 2) {
                return Direction.NORTH_WEST;
            } else {
                return Direction.SOUTH_WEST;
            }
        } else {
            if (y < HEIGHT() / 2) {
                return Direction.NORTH_EAST;
            } else {
                return Direction.SOUTH_EAST;
            }
        }
    }

    private int score(List<Robot> robots) {
        Map<Direction, Integer> score = new HashMap<>();

        for (int y = 0; y < HEIGHT(); y++) {
            for (int x = 0; x < WIDTH(); x++) {
                if (x == WIDTH() / 2 || y == HEIGHT() / 2) {
                    continue;
                }
                Direction quadrant = quadrant(x, y);
                int localScore = atPos(robots, new Vector2D(x, y));
                int currentScore = score.getOrDefault(quadrant, 0);
                score.put(quadrant, currentScore + localScore);
            }
        }
        log.info(score);
        return score.values().stream().reduce(1, (a,b) -> a * b);
    }

    private int atPos(List<Robot> robots, Vector2D pos) {
        return robots.stream().filter(robot -> robot.pos.equals(pos)).toList().size();
    }

    private void printRobots(List<Robot> robots, boolean skipMiddle, boolean showNext) {
        var next = move(robots);
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < HEIGHT(); y++) {
            for (int x = 0; x < WIDTH(); x++) {
                if (skipMiddle && (x == WIDTH() / 2 || y == HEIGHT() / 2)) {
                    sb.append(Color.BLACK.text("."));
                    continue;
                }

                int count = atPos(robots, new Vector2D(x, y));
                if (count == 0) {
                    int nextCount = atPos(next, new Vector2D(x, y));
                    if (nextCount == 0 || !showNext) {
                        sb.append(Color.YELLOW.text("."));
                    } else {
                        sb.append(Color.GREEN.text(nextCount));
                    }
                } else {
                    sb.append(Color.RED.text(count));
                }
            }
            sb.append("\n");
        }
        log.info(sb);
    }

    private List<Robot> move(List<Robot> robots) {
        return robots.stream().map(this::move).toList();
    }

    private Robot move(Robot robot) {
        return new Robot(new Vector2D(
                (robot.pos.x() + robot.velocity.x() + WIDTH()) % WIDTH(),
                (robot.pos.y() + robot.velocity.y() + HEIGHT()) % HEIGHT()
                ), robot.velocity
        );
    }

    public static void main(String[] ignoredArgs) {
        Day14 puzzle = new Day14();
        puzzle.run();
    }
}
