package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private long a;
    private long b;
    private long c;
    private int pointer = 0;
    private List<Long> program;
    private List<Long> output;

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private boolean test(long a, List<Long> expected) {
        init();

        this.a = a;

        while (pointer < program.size()) {
            long instruction = program.get(pointer);
            long operand = program.get(++pointer);
            pointer = operation(instruction, operand);
        }

        if (expected.equals(output)) {
            log.success(a + " -> " + output.stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            log.error(a + " -> " + output.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        return expected.equals(output);
    }

    private void solve2() {
        init();

        Set<Long> possibleAs = LongStream.range(0, 8).boxed().collect(Collectors.toSet());
        for (int out = 1; out <= program.size(); out++) {
            List<Long> expectedOutput = program.subList(program.size() - out, program.size());
            Set<Long> check = new HashSet<>();
            for (Long possibleA : possibleAs) {
                if (test(possibleA, expectedOutput)) {
                    for (int i = 0; i < 8; i++) {
                        check.add(possibleA * 8 + i);
                    }
                }
            }
            possibleAs = check;
            log.info(possibleAs);
        }


    }

    private void solve1() {
        init();
        while (pointer < program.size()) {
            long instruction = program.get(pointer);
            long operand = program.get(++pointer);
            pointer = operation(instruction, operand);
        }

        log.success(output.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private long combo(long operand) {
        return switch (operand) {
            case 0L, 1L, 2L, 3L -> operand;
            case 4L -> a;
            case 5L -> b;
            case 6L -> c;
            default -> throw new IllegalArgumentException("Reserved combo operand");
        };
    }

    private int operation(long instruction, long operand) {
        logState();
        return switch (instruction) {
            case 0L -> adv(operand);
            case 1L -> bxl(operand);
            case 2L -> bst(operand);
            case 3L -> jnz(operand);
            case 4L -> bxc(operand);
            case 5L -> out(operand);
            case 6L -> bdv(operand);
            case 7L -> cdv(operand);
            default -> throw new IllegalArgumentException("Unknown instruction: " + instruction);
        };
    }

    private void logState() {
        log.debug(MessageFormat.format("A = {0} B = {1} C = {2}", a, b, c));
    }

    private int bdv(long operand) {
        long divisor = (long) Math.pow(2, combo(operand));
        log.debug(MessageFormat.format("BDV: A / combo -> B ({0}/{1} -> {2})", a, divisor, a / divisor));
        b = a / divisor;
        return ++pointer;
    }

    private int out(long operand) {
        output.add(combo(operand) % 8);
        log.debug(MessageFormat.format("OUT: COMBO -> {0} ({1} -> {2} % 8)", combo(operand) % 8, operand, combo(operand)));
        return ++pointer;
    }

    private int bxc(long operand) {
        log.debug(MessageFormat.format("BXC: B xor C -> B ({0} xor {1} -> {2})", b, c, b ^ c));
        b = b ^ c;
        return ++pointer;
    }

    private int cdv(long operand) {
        long divisor = (long) Math.pow(2, combo(operand));
        log.debug(MessageFormat.format("BDV: A / combo -> C ({0}/{1} -> {2})", a, divisor, a / divisor));
        c = a / divisor;
        return ++pointer;
    }

    private int jnz(long operand) {
        if (a == 0) {
            log.debug("JNZ: NOJUMP");
            return ++pointer;
        }

        log.debug(MessageFormat.format("JNZ: JUMP -> {0}", operand));
        return (int) operand;
    }

    private int bst(long operand) {
        b = combo(operand) % 8;
        log.debug(MessageFormat.format("BST: COMBO % 8 -> B ({0} ({1}) % 8 -> {2}", combo(operand), operand, b));
        return ++pointer;
    }

    private int bxl(long operand) {
        log.debug(MessageFormat.format("BXC: B xor OP -> B ({0} xor {1} -> {2})", b, operand, b ^ operand));
        b = b ^ operand;
        return ++pointer;
    }

    private int adv(long operand) {
        int divisor = (int) Math.pow(2, combo(operand));
        log.debug(MessageFormat.format("BDV: A / combo -> A ({0}/{1} -> {2})", a, divisor, a / divisor));
        a = a / divisor;
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
            program.add(Long.parseLong(matcherProgram.group()));
        }

        log.debug("Register A: " + a);
        log.debug("Register B: " + b);
        log.debug("Register C: " + c);

        log.debug("Program: " + program);

        output = new ArrayList<>();
        pointer = 0;
    }

    public static void main(String[] ignoredArgs) {
        Day17 puzzle = new Day17();
        puzzle.run();
    }
}
