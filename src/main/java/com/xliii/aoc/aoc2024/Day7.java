package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    record Calculation(Long result, List<Long> numbers) {}

    private void solve1() {
        List<Calculation> calculations = getInput().stream().map(line -> {
            String[] parts = line.split(": ");
            Long result = Long.valueOf(parts[0]);
            List<Long> numbers = Arrays.stream(parts[1].split(" ")).map(Long::valueOf).toList();
            return new Calculation(result, numbers);
        }).toList();

        log.info(calculations);

        Long result = calculations.stream()
                .filter(this::isCorrect)
                .map(Calculation::result)
                .reduce(0L, Long::sum);

        log.success(result);
    }

    private boolean isCorrect(Calculation c) {
        Long first = c.numbers.getFirst();
        if (c.numbers.size() == 1) {
            return first.equals(c.result);
        }

        Long second = c.numbers.get(1);
        List<Long> tail = c.numbers.subList(2, c.numbers.size());

        List<Long> sumCheck = new ArrayList<>();
        sumCheck.add(first + second);
        sumCheck.addAll(tail);

        List<Long> multCheck = new ArrayList<>();
        multCheck.add(first * second);
        multCheck.addAll(tail);


        return isCorrect(new Calculation(c.result, sumCheck)) || isCorrect(new Calculation(c.result, multCheck));
    }

    private boolean isCorrect2(Calculation c) {
        Long first = c.numbers.getFirst();
        if (c.numbers.size() == 1) {
            return first.equals(c.result);
        }

        Long second = c.numbers.get(1);
        List<Long> tail = c.numbers.subList(2, c.numbers.size());

        List<Long> sumCheck = new ArrayList<>();
        sumCheck.add(first + second);
        sumCheck.addAll(tail);

        List<Long> multCheck = new ArrayList<>();
        multCheck.add(first * second);
        multCheck.addAll(tail);

        List<Long> concatCheck = new ArrayList<>();
        concatCheck.add(concat(first, second));
        concatCheck.addAll(tail);

        return isCorrect2(new Calculation(c.result, sumCheck)) ||
               isCorrect2(new Calculation(c.result, multCheck)) ||
               isCorrect2(new Calculation(c.result, concatCheck));
    }

    private Long concat(Long a, Long b) {
        return Long.valueOf(a.toString() + b.toString());
    }

    private void solve2() {
        List<Calculation> calculations = getInput().stream().map(line -> {
            String[] parts = line.split(": ");
            Long result = Long.valueOf(parts[0]);
            List<Long> numbers = Arrays.stream(parts[1].split(" ")).map(Long::valueOf).toList();
            return new Calculation(result, numbers);
        }).toList();

        log.info(calculations);

        Long result = calculations.stream()
                .filter(this::isCorrect2)
                .map(Calculation::result)
                .reduce(0L, Long::sum);

        log.success(result);
    }

    public static void main(String[] args) {
        Day7 puzzle = new Day7();
        puzzle.run();
    }
}
