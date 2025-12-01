package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;

import java.util.List;

public class Day1 extends Puzzle {

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private void solve1() {
        List<String> input = getInput();
        int index = 50;
        int password = 0;
        for (String line : input) {
            char c = line.charAt(0);
            int num = Integer.parseInt(line, 1, line.length(), 10);
            if (c == 'R') {
                index = (index + num + 100) % 100;
            } else {
                index = (index - num + 100) % 100;
            }

            System.out.println(index);

            if (index == 0) {
                password++;
            }
        }

        System.out.println("Password: " + password);
    }

    private void solve2() {
        List<String> input = getInput();
        int currentPos = 50;
        int nextPos;
        int password = 0;
        for (String line : input) {
            int prevPos = currentPos;
            char c = line.charAt(0);
            int num = Integer.parseInt(line, 1, line.length(), 10);
            if (c == 'R') {
                nextPos = currentPos + num;
                currentPos = nextPos % 100;
            } else {
                nextPos = currentPos - num;
                currentPos = Math.floorMod(nextPos, 100);
            }

            //check
            password += (num / 100);
            //overflow
            int remainder = num % 100;
            if (prevPos != 0 && c == 'R' && ((prevPos + remainder) >= 100)) {
                password++;
            }

            if (prevPos != 0 && c == 'L' && ((prevPos - remainder) <= 0)) {
                password++;
            }

            System.out.println(line + ": " + prevPos + " -> " + currentPos + "(" + password + ")");
        }

        System.out.println("Password: " + password);
    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        day1.run();
    }
}
