package com.xliii.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private final String enable = "do()";
    private final String disable = "don't()";

    private final Pattern patternWithFlags = Pattern.compile("(mul\\([0-9]+,[0-9]+\\)|do\\(\\)|don't\\(\\))");
    private final Pattern pattern = Pattern.compile("mul\\([0-9]+,[0-9]+\\)");
    private final Pattern number = Pattern.compile("[0-9]+");

    private long calculate(String mul) {
        Matcher matcher = number.matcher(mul);
        List<Long> numbers = new ArrayList<>();
        while (matcher.find()) {
            numbers.add(Long.valueOf(matcher.group()));
        }

        long result = numbers.stream().reduce(1L, (a, b) -> a * b);
        log.info(mul + " = " + result);
        return result;
    }

    @Override
    protected void run() {
        String input = String.join("", getInput());
        log.info(mulLine(input));
    }

    private long mulLine(String input) {
        Matcher matcher = patternWithFlags.matcher(input);
        List<String> matches = new ArrayList<>();
        boolean enabled = true;
        while (matcher.find()) {
            String token = matcher.group();
            if (token.equals(enable)) {
                log.success("Enable at " + matcher.start());
                enabled = true;
            } else if (token.equals(disable)) {
                log.error("Disable at " + matcher.start());
                enabled = false;
            } else if (enabled) {
                log.info("Added token at " + matcher.start() + ": " + token);
                matches.add(token);
            } else {
                log.info("Skipped token at " + matcher.start() + ": " + token);
            }
        }

        //log.info(matches);
        log.info(matches.size());

        Long sum = matches.stream().map(this::calculate).reduce(0L, Long::sum);
        log.info(sum);
        return sum;
    }

    public static void main(String[] args) {
        Day3 puzzle = new Day3();
        puzzle.run();
    }
}
