package com.xliii.aoc2024;

import java.util.*;

public class Day2 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private boolean safe(List<Integer> report) {
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

    private boolean safeWithToleration(List<Integer> report) {
        System.out.println();
        return safeWithToleration(report, 0);
    }

    private List<Integer> removeAt(List<Integer> report, int index) {
        ArrayList<Integer> updated = new ArrayList<>(report);
        updated.remove(index);
        log.info(report + " > " + updated);
        return updated;
    }

    private boolean safeWithToleration(List<Integer> report, int errors) {
        //TODO: Handle case where initial rising was incorrect
        log.info("Processing " + report + (errors > 0 ? " with toleration" : ""));
        if (errors > 1) {
            log.error("Unsafe: " + report + " - too deep: " + errors);
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
                boolean result1 = safeWithToleration(removeAt(report, i - 1), errors + 1);
                boolean result2 = safeWithToleration(removeAt(report, i), errors + 1);
                if (result1 || result2) {
                    log.success("Safe with toleration: " + report);
                } else {
                    log.error("Unsafe: " + report);
                }

                return result1 || result2;
            }

            if (rising == null) {
                rising = diff > 0;
            } else if (rising != (diff > 0)) {
                boolean result0 = safeWithToleration(removeAt(report, 0), errors + 1);
                boolean result1 = safeWithToleration(removeAt(report, i - 1), errors + 1);
                boolean result2 = safeWithToleration(removeAt(report, i), errors + 1);
                if (result0 || result1 || result2) {
                    log.success("Safe with toleration: " + report);
                } else {
                    log.error("Unsafe: " + report);
                }

                return result0 || result1 || result2;
            }
        }

        if (errors > 0) {
            log.success("Safe with toleration: " + report);
        } else {
            log.error("Safe: " + report);
        }

        return true;
    }

    public void processSafe() {
        long safeReportsTotal = getInput().stream()
                .map(entry -> List.of(entry.split("\\s+")))
                .map(entry -> entry.stream().map(Integer::valueOf).toList())
                .map(this::safe)
                .filter(Boolean::booleanValue)
                .count();

        log.info(safeReportsTotal);
    }

    private void processSafeWithToleration() {
        long safeReportsWithTolerationTotal = getInput().stream()
                .map(entry -> List.of(entry.split("\\s+")))
                .map(entry -> entry.stream().map(Integer::valueOf).toList())
                .map(this::safeWithToleration)
                .filter(Boolean::booleanValue)
                .count();


        log.info(safeReportsWithTolerationTotal);
    }

    @Override
    protected void run() {
        processSafe();
        processSafeWithToleration();
    }

    public static void main(String[] args) {
        Day2 day2 = new Day2();
        day2.run();
    }
}
