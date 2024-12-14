package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.grid.BigVector2D;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private final Pattern buttonPattern = Pattern.compile("Button .: X\\+([0-9]+), Y\\+([0-9]+)");
    private final Pattern prizePattern = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

    private final BigDecimal INVALID = BigDecimal.ZERO;
    private final BigDecimal THREE = new BigDecimal("3");

    private final BigDecimal PRIZE_OFFSET = new BigDecimal("10000000000000");

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {
        var tokens = getCaseInput().stream()
                .map(this::solve)
                .filter(cost -> !cost.equals(INVALID))
                .reduce(BigDecimal::add).orElseThrow();

        log.success(tokens);
    }

    private void solve1() {
        var tokens = getCaseInput().stream()
                .map(this::solve)
                .filter(cost -> !cost.equals(INVALID))
                .reduce(BigDecimal::add).orElseThrow();

        log.success(tokens);
    }

    private BigDecimal solve(State state) {
        //     b.x * p.y - b.y * p.x
        // a = ---------------------
        //     b.x * a.y - b.y * a.x
        BigDecimal aDividend = ((state.b.x().multiply(state.prize.y())).subtract(state.b.y().multiply(state.prize.x())));
        BigDecimal aDivisor = ((state.b.x().multiply(state.a.y())).subtract(state.b.y().multiply(state.a.x())));

        //     a.x * p.y - a.y * p.x
        // b = ---------------------
        //     a.x * b.y - a.y * b.x
        BigDecimal bDividend = ((state.a.x().multiply(state.prize.y())).subtract(state.a.y().multiply(state.prize.x())));
        BigDecimal bDivisor = ((state.a.x().multiply(state.b.y())).subtract(state.a.y().multiply(state.b.x())));

        //modulo check
        if (!aDividend.remainder(aDivisor).equals(BigDecimal.ZERO)) {
            return INVALID;
        }

        if (!bDividend.remainder(bDivisor).equals(BigDecimal.ZERO)) {
            return INVALID;
        }

        BigDecimal a = aDividend.divide(aDivisor, RoundingMode.UNNECESSARY);
        BigDecimal b = bDividend.divide(bDivisor, RoundingMode.UNNECESSARY);

        // cost = a * 3 + b
        return a.multiply(THREE).add(b);
    }

    record State(BigVector2D a, BigVector2D b, BigVector2D prize, int aPressed, int bPressed) {
        public int cost() {
            return aPressed * 3 + bPressed;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private List<State> getCaseInput() {
        List<State> states = new ArrayList<>();
        Iterator<String> inputIt = getInput().iterator();
        while (inputIt.hasNext()) {
            Matcher a = buttonPattern.matcher(inputIt.next());
            Matcher b = buttonPattern.matcher(inputIt.next());
            Matcher p = prizePattern.matcher(inputIt.next());
            a.find();
            b.find();
            p.find();

            BigVector2D buttonA = BigVector2D.of(a.group(1), a.group(2));
            BigVector2D buttonB = BigVector2D.of(b.group(1), b.group(2));
            BigVector2D prize = BigVector2D.of(p.group(1), p.group(2));
            prize = prize.add(new BigVector2D(PRIZE_OFFSET, PRIZE_OFFSET));

            states.add(new State(buttonA, buttonB, prize, 0, 0));

            if (inputIt.hasNext()) {
                //empty line
                inputIt.next();
            }

        }

        return states;
    }

    public static void main(String[] ignoredArgs) {
        Day13 puzzle = new Day13();
        puzzle.run();
    }
}
