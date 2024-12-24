package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day24 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private enum Op {
        AND,
        OR,
        XOR
    }

    private record Statement(String arg1, Op op, String arg2, String target) {}

    private final Map<String, Integer> VARIABLES = new HashMap<>();
    private final List<Statement> STATEMENTS = new ArrayList<>();

    private void solve2() {

    }

    private int calculate(int arg1, Op op, int arg2) {
        switch (op) {
            case OR -> {
                return arg1 | arg2;
            }
            case AND -> {
                return arg1 & arg2;
            }
            case XOR -> {
                return arg1 ^ arg2;
            }
            default -> throw new RuntimeException();
        }
    }

    private void solve1() {
        init();
        while (!STATEMENTS.isEmpty()) {
            var it = STATEMENTS.iterator();
            while (it.hasNext()) {
                var statement = it.next();
                if (!VARIABLES.containsKey(statement.arg1) || (!VARIABLES.containsKey(statement.arg2))) continue;

                var arg1 = VARIABLES.get(statement.arg1);
                var arg2 = VARIABLES.get(statement.arg2);
                VARIABLES.put(statement.target, calculate(arg1, statement.op, arg2));
                it.remove();
            }
        }

        log.warn(VARIABLES);
        log.warn(STATEMENTS);

        String binary = VARIABLES.entrySet().stream()
                .filter(e -> e.getKey().startsWith("z"))
                .sorted(Map.Entry.<String, Integer>comparingByKey().reversed())
                .map(Map.Entry::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining());

        log.success(binary);
        log.success(Long.valueOf(binary, 2));
    }

    private void init() {
        VARIABLES.clear();
        STATEMENTS.clear();

        Map<Boolean, List<String>> input = getInput().stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.partitioningBy(line -> line.contains("->")));

        var variables = input.get(false);
        var statements = input.get(true);

        for (var variable : variables) {
            String[] parts = variable.split(": ");
            VARIABLES.put(parts[0], Integer.parseInt(parts[1]));
        }

        log.error(VARIABLES);

        for (var statement : statements) {
            String[] parts = statement.split(" ");
            STATEMENTS.add(new Statement(parts[0], Op.valueOf(parts[1]), parts[2], parts[4]));
        }

        log.error(STATEMENTS);
    }

    public static void main(String[] ignoredArgs) {
        Day24 puzzle = new Day24();
        puzzle.run();
    }
}
