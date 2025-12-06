package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private void solve1() {
        int size = Stream.of(getInput().getFirst().split("\\s+")).filter(s -> !s.isBlank()).toList().size();
        Calculator calc = new Calculator(size);
        for (String line : getInput()) {
            List<String> list = Stream.of(line.split("\\s+")).filter(s -> !s.isBlank()).toList();
            calc.addRow(list);
        }

        for (var calculation : calc.calculations) {
            System.out.println(calculation.result());
        }

        long result = calc.calculations.stream().map(Calculation::result).mapToLong(Long::longValue).sum();

        System.out.println("Result: " + result);
    }

    static class Calculator {
        public List<Calculation> calculations = new ArrayList<>();

        public Calculator(int size) {
            for (int i = 0; i < size; i++) {
                calculations.add(new Calculation());
            }
        }

        public void addRow(List<String> data) {
            for (int i = 0; i < data.size(); i++) {
                String value = data.get(i);
                var calculation = calculations.get(i);
                if (value.matches("\\d+")) {
                    calculation.addNumber(Long.valueOf(value));
                } else {
                    calculation.addOperation(value);
                }
            }
        }
    }

    enum Operation {
        ADDITION("+"),
        MULTIPLICATION("*");

        private String sign;

        private static Map<String, Operation> map = new HashMap<>();

        static {
            for (Operation op : values()) {
                map.put(op.sign, op);
            }
        }

        Operation(String sign) {
            this.sign = sign;
        }

        public static Operation create(String sign) {
            return map.get(sign);
        }

        public Long apply(List<Long> numbers) {
            if (this.equals(ADDITION)) {
                return numbers.stream().reduce(0L, Long::sum);
            } else {
                return numbers.stream().reduce(1L, (a, b) -> a * b);
            }
        }
    }

    static class Calculation {
        private final List<Long> numbers = new ArrayList<>();
        private Operation operation;

        public void addNumber(Long number) {
            numbers.add(number);
        }

        public void addOperation(String value) {
            operation = Operation.create(value);
        }

        public long result() {
            return operation.apply(numbers);
        }
    }


      @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day6 puzzle = new Day6();
        puzzle.run();
    }
}
