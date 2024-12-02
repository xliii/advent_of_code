package com.xliii.aoc2024;

import java.util.*;

public class Day2 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private static boolean safe(List<Integer> report) {
        Integer last = null;
        Boolean rising = null;
        for (Integer entry : report) {
            if (last == null) {
                last = entry;
                continue;
            }

            int diff = entry - last;
            last = entry;
            if (diff == 0 || Math.abs(diff) > 3) {
                return false;
            }

            if (rising == null) {
                rising = diff > 0;
            } else if (rising != (diff > 0)) {
                return false;
            }


        }
        return true;
    }

    private static boolean safeWithToleration(List<Integer> report) {
        System.out.println();
        return safeWithToleration(report, 0);
    }

    private static List<Integer> removeAt(List<Integer> report, int index, int errors) {
        ArrayList<Integer> updated = new ArrayList<>(report);
        updated.remove(index);
        System.out.println("  ".repeat(errors) + report + " > " + updated);
        return updated;
    }

    private static boolean safeWithToleration(List<Integer> report, int errors) {
        //TODO: Handle case where initial rising was incorrect

        System.out.println("  ".repeat(errors) + "Processing " + report + (errors > 0 ? " with toleration" : ""));
        if (errors > 1) {
            System.out.println("  ".repeat(errors) + "Unsafe: " + report + " - too deep: " + errors);
            return false;
        }
        Integer last = null;
        Boolean rising = null;
        for (int i = 0; i < report.size(); i++) {
            Integer entry = report.get(i);

            if (last == null) {
                last = entry;
                continue;
            }

            int diff = entry - last;
            last = entry;
            if (diff == 0 || Math.abs(diff) > 3) {
                boolean result1 = safeWithToleration(removeAt(report, i - 1, errors), errors + 1);
                boolean result2 = safeWithToleration(removeAt(report, i, errors), errors + 1);
                if (result1 || result2) {
                    System.out.println("  ".repeat(errors) + "Safe with toleration: " + report);
                } else {
                    System.out.println("  ".repeat(errors) + "Unsafe: " + report);
                }

                return result1 || result2;
            }

            if (rising == null) {
                rising = diff > 0;
            } else if (rising != (diff > 0)) {
                boolean result0 = safeWithToleration(removeAt(report, 0, errors), errors + 1);
                boolean result1 = safeWithToleration(removeAt(report, i - 1, errors), errors + 1);
                boolean result2 = safeWithToleration(removeAt(report, i, errors), errors + 1);
                if (result0 || result1 || result2) {
                    System.out.println("  ".repeat(errors) + "Safe with toleration: " + report);
                } else {
                    System.out.println("  ".repeat(errors) + "Unsafe: " + report);
                }

                return result0 || result1 || result2;
            }
        }

        if (errors > 0) {
            System.out.println("  ".repeat(errors) + "Safe with toleration: " + report);
        } else {
            System.out.println("  ".repeat(errors) + "Safe: " + report);
        }

        return true;
    }

    public static void main(String[] args) {
        Day2 day2 = new Day2();
        long safeReportsTotal = day2.getInput().stream()
                .map(entry -> List.of(entry.split("\\s+")))
                .map(entry -> entry.stream().map(Integer::valueOf).toList())
                .map(Day2::safe)
                .filter(Boolean::booleanValue)
                .count();

        System.out.println(safeReportsTotal);

        long safeReportsWithTolerationTotal = day2.getInput().stream()
                .map(entry -> List.of(entry.split("\\s+")))
                .map(entry -> entry.stream().map(Integer::valueOf).toList())
                .map(Day2::safeWithToleration)
                .filter(Boolean::booleanValue)
                .count();


        System.out.println(safeReportsWithTolerationTotal);
    }

}
