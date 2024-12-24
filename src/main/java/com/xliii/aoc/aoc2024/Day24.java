package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day24 extends Puzzle {
    
    
    //0 simple case
    //for i = 1; i < 45; i++
    //xN XOR yN -> partial sum
    //xN AND yN -> partial carry
    //partial sum AND carry out (i-1) -> carry's carry
    //partial sum XOR carry out (i-1) -> sum(i)
    //partial carry OR carry's carry -> carry out (i)
    
    /*
    x00 XOR y00 -> z00 (0 sum)
    x00 AND y00 -> gct (carry out 0)
    
    x01 XOR y01 -> wnt (1 partial sum)
    x01 AND y01 -> gcq (1 partial carry)
    wnt AND gct -> dnc (1 carry's carry)
    wnt XOR gct -> z01 (1 sum)
    gcq OR dnc -> qcs (carry out 1)

    x02 XOR y02 -> gwm (2 partial sum)
    x02 AND y02 -> vhv (2 partial carry)
    gwm AND qcs -> vhs (2 carry's carry)
    gwm XOR qcs -> z02 (2 sum)
    vhv OR vhs -> ktn (carry out 2) 


    Solve2 goes through chain of computation,
    throwing exception in case expected statement is not found
    Then frame in question can be inspected manually
    and replacement added to REPLACE_FIX map
     */

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

    private record Statement(String arg1, Op op, String arg2, String target) {
        @Override
        public String toString() {
            return arg1 + " " + op + " " + arg2 + " -> " + target;
        }
    }

    private final Map<String, Integer> VARIABLES = new HashMap<>();
    private final List<Statement> STATEMENTS = new ArrayList<>();
    
    private final int SIZE = 45;
    
    private final String[] PARTIAL_SUMS = new String[SIZE];
    private final String[] PARTIAL_CARRIES = new String[SIZE];
    private final String[] CARRY_CARRIES = new String[SIZE];
    private final String[] SUMS = new String[SIZE];
    private final String[] CARRIES_OUT = new String[SIZE];

    private Statement find(String arg1, String arg2, Op op) {
        return STATEMENTS.stream()
                .filter(s -> s.op.equals(op))
                .filter(s -> (s.arg1.equals(arg1) && s.arg2.equals(arg2))
                        || (s.arg1.equals(arg2) && s.arg2.equals(arg1)))
                .findAny().orElseThrow(() -> new RuntimeException("Couldn't find " + arg1 + " " + op + " " + arg2));
    }

    private void solve2() {
        init();

        var firstSum = find("x00", "y00", Op.XOR);
        var firstCarryOut = find("x00", "y00", Op.AND);

        SUMS[0] = firstSum.target;
        CARRIES_OUT[0] = firstCarryOut.target;

        for (int i = 1; i < SIZE; i++) {
            String xName = "x" + String.format("%02d", i);
            String yName = "y" + String.format("%02d", i);
            var partialSum = find(xName, yName, Op.XOR);
            var partialCarry = find(xName, yName, Op.AND);

            log.warn(partialSum);
            log.warn(partialCarry);
            PARTIAL_CARRIES[i] = partialCarry.target;
            PARTIAL_SUMS[i] = partialSum.target;

            var carriesCarry = find(PARTIAL_SUMS[i], CARRIES_OUT[i - 1], Op.AND);
            var sum = find(PARTIAL_SUMS[i], CARRIES_OUT[i - 1], Op.XOR);

            log.warn(carriesCarry);
            log.warn(sum);
            CARRY_CARRIES[i] = carriesCarry.target;
            SUMS[i] = sum.target;

            var carryOut = find(PARTIAL_CARRIES[i], CARRY_CARRIES[i], Op.OR);
            log.warn(carryOut);
            CARRIES_OUT[i] = carryOut.target;
        }

        var answer = REPLACE_FIX.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.joining(","));
        log.success(answer);
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
                //log.error(statement);
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

    private final Map<String, String> REPLACE_FIX = Map.of(
            "z12", "qdg",
            "qdg", "z12",
            "z19", "vvf",
            "vvf", "z19",
            "fgn", "dck",
            "dck", "fgn",
            "z37", "nvh",
            "nvh", "z37"
    );

    private String fix(String input) {
        return REPLACE_FIX.getOrDefault(input, input);
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

        //log.error(VARIABLES);

        for (var statement : statements) {
            String[] parts = statement.split(" ");
            STATEMENTS.add(new Statement(parts[0], Op.valueOf(parts[1]), parts[2], fix(parts[4])));
        }

        //log.error(STATEMENTS);
    }

    public static void main(String[] ignoredArgs) {
        Day24 puzzle = new Day24();
        puzzle.run();
    }
}
