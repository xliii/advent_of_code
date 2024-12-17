package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day17 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private int a;
    private int b;
    private int c;
    private int pointer = 0;
    private List<Integer> program;
    private List<Integer> output;

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {

    }

    private void solve1() {
        init();
        while (pointer < program.size()) {
            int instruction = program.get(pointer);
            int operand = program.get(++pointer);
            pointer = operation(instruction, operand);
        }

        log.success(output.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private int combo(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalArgumentException("Reserved combo operand");
        };
    }

    private int operation(int instruction, int operand) {
        return switch (instruction) {
            case 0 -> adv(operand);
            case 1 -> bxl(operand);
            case 2 -> bst(operand);
            case 3 -> jnz(operand);
            case 4 -> bxc(operand);
            case 5 -> out(operand);
            case 6 -> bdv(operand);
            case 7 -> cdv(operand);
            default -> throw new IllegalArgumentException("Unknown instruction: " + instruction);
        };
    }

    private int bdv(int operand) {
        b = a / (int) Math.pow(2, combo(operand));
        return ++pointer;
    }

    private int out(int operand) {
        output.add(combo(operand) % 8);
        return ++pointer;
    }

    private int bxc(int operand) {
        b = b ^ c;
        return ++pointer;
    }

    private int cdv(int operand) {
        c = a / (int) Math.pow(2, combo(operand));
        return ++pointer;
    }

    private int jnz(int operand) {
        if (a == 0) {
            return ++pointer;
        }

        return operand;
    }

    private int bst(int operand) {
        b = combo(operand) % 8;
        return ++pointer;
    }

    private int bxl(int operand) {
        b = b ^ operand;
        return ++pointer;
    }

    private int adv(int operand) {
        a = a / (int) Math.pow(2, combo(operand));
        return ++pointer;
    }

    private void init() {
        List<String> input = getInput();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcherA = pattern.matcher(input.get(0));
        matcherA.find();
        a = Integer.parseInt(matcherA.group());

        Matcher matcherB = pattern.matcher(input.get(1));
        matcherB.find();
        b = Integer.parseInt(matcherB.group());

        Matcher matcherC = pattern.matcher(input.get(2));
        matcherC.find();
        c = Integer.parseInt(matcherC.group());

        program = new ArrayList<>();
        Matcher matcherProgram = pattern.matcher(input.get(4));
        while (matcherProgram.find()) {
            program.add(Integer.parseInt(matcherProgram.group()));
        }

        log.info("Register A: " + a);
        log.info("Register B: " + b);
        log.info("Register C: " + c);

        log.info("Program: " + program);

        output = new ArrayList<>();
    }

    public static void main(String[] ignoredArgs) {
        Day17 puzzle = new Day17();
        puzzle.run();
    }
}
