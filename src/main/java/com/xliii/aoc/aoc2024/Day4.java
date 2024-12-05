package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private final Pattern pattern = Pattern.compile("(?=XMAS|SAMX)");

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private boolean isXMAS(Grid<Character> grid, int x, int y) {
        if (grid.outOfBounds(x, y) || grid.isEdge(x, y)) return false;

        char nw = grid.neighbor(x, y, Direction.NORTH_WEST);
        char se = grid.neighbor(x, y, Direction.SOUTH_EAST);
        char sw = grid.neighbor(x, y, Direction.SOUTH_WEST);
        char ne = grid.neighbor(x, y, Direction.NORTH_EAST);

        return
            isMMSS(nw, ne, sw, se) ||
            isMMSS(nw, sw, ne, se) ||
            isMMSS(sw, se, nw, ne) ||
            isMMSS(ne, se, nw, sw);
    }

    private boolean isMMSS(char a, char b, char c, char d) {
        return a == 'M' && b == 'M' && c == 'S' && d == 'S';
    }

    private void solve2() {
        Grid<Character> grid = Grid.create(getInput());

        int total = 0;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                char current = grid.get(x, y);
                if (current != 'A') {
                    continue;
                }

                if (isXMAS(grid, x, y)) {
                    total++;
                }

            }
        }

        log.success(total);
    }

    private void solve1() {
        List<String> input = getInput();
        List<String> transposed = transpose(input);
        int total = 0;
        total += count(input);
        total += count(transposed);
        total += count(rotate45(input));
        total += count(rotate135(input));

        log.success(total);
    }

    private void print(List<String> table) {
        table.forEach(log::info);
    }

    private List<String> rotate135(List<String> data) {
        int N = data.size();
        int M = data.getFirst().length();
        int max = Math.max(N, M);
        char[][] result = new char[2*max][2*max];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result[i+j][N-1-i+j] = data.get(i).charAt(M - 1 - j);
            }
        }

        return Arrays.stream(result).map(String::new).map(s -> s.replaceAll("\\u0000", "")).toList();
    }

    private List<String> rotate45(List<String> data) {
        int N = data.size();
        int M = data.getFirst().length();
        int max = Math.max(N, M);
        char[][] result = new char[2*max][2*max];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result[i+j][N-1-i+j] = data.get(i).charAt(j);
            }
        }

        return Arrays.stream(result).map(String::new).map(s -> s.replaceAll("\\u0000", "")).toList();
    }

    private List<String> transpose(List<String> data) {
        List<String> transposed = new ArrayList<>();
        int size = data.getFirst().length();
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            for (String row : data) {
                sb.append(row.charAt(i));
            }

            transposed.add(sb.toString());
        }
        return transposed;
    }

    private int count(List<String> square) {
        log.info("Searching in: ");
        print(square);
        int total = 0;
        for (String line : square) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                log.info(matcher.group());
                total++;
            }
        }
        return total;
    }

    public static void main(String[] args) {
        Day4 puzzle = new Day4();
        puzzle.run();
    }
}
