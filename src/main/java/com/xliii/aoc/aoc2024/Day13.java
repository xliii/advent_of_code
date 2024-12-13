package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.grid.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day13 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private final Pattern buttonPattern = Pattern.compile("Button .: X\\+([0-9]+), Y\\+([0-9]+)");
    private final Pattern prizePattern = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

    private final Map<State, Integer> cache = new ConcurrentHashMap<>();

    private final Integer INVALID = Integer.MAX_VALUE;

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {

    }

    private void solve1() {
        var tokens = getCaseInput().stream()
                .map(this::solve)
                .filter(cost -> !cost.equals(INVALID))
                .reduce(Integer::sum).orElseThrow();

        log.success(tokens);
    }

    private int memoizedSolve(State state) {
        if (!cache.containsKey(state)) {
            cache.put(state, solve(state));
        }

        return cache.get(state);
    }

    private int solve(State state) {
        if (state.prize.equals(Vector2D.ZERO)) {
            return state.cost();
        }

        if (state.prize.x() < 0 || state.prize.y() < 0) {
            return INVALID;
        }

        if (state.aPressed > 100 || state.bPressed > 100) {
            return INVALID;
        }

        return Math.min(memoizedSolve(new State(state.a, state.b, state.prize.subtract(state.a),
                        state.aPressed + 1, state.bPressed)),
                memoizedSolve(new State(state.a, state.b, state.prize.subtract(state.b),
                        state.aPressed, state.bPressed + 1)));

    }

    record State(Vector2D a, Vector2D b, Vector2D prize, int aPressed, int bPressed) {
        public int cost() {
            return aPressed * 3 + bPressed;
        }
    }

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

            Vector2D buttonA = new Vector2D(parseInt(a.group(1)), parseInt(a.group(2)));
            Vector2D buttonB = new Vector2D(parseInt(b.group(1)), parseInt(b.group(2)));
            Vector2D prize = new Vector2D(parseInt(p.group(1)), parseInt(p.group(2)));

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
