package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    private static final Integer EMPTY = -1;

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {
        String line = getInput().getFirst();
        log.info(line);

        List<File> files = parseFiles(line);
        log.error(files);

        defragment(files);

        log.warn(files);

        List<Integer> disk = toDisk(files);
        log.info(disk);

        log.success(checksum(disk));
    }

    private void solve1() {
        String line = getInput().getFirst();
        log.info(line);

        List<Integer> disk = parseDisk(line);
        log.error(disk);

        fragment(disk);

        log.warn(disk);

        log.success(checksum(disk));
    }

    private List<Integer> toDisk(List<File> files) {
        List<Integer> disk = new ArrayList<>();
        for (File file : files) {
            IntStream.range(0, file.size).forEach(_ -> disk.add(file.id));
        }
        return disk;
    }

    private long checksum(List<Integer> disk) {
        long checksum = 0;
        for (int i = 0; i < disk.size(); i++) {
            long block = disk.get(i);
            if (block == EMPTY) {
                continue;
            }

            long value = block * i;
            //log.info(checksum + "(+ " + value + " ) @ " + i);
            checksum += value;

        }

        return checksum;
    }

    private record File(int id, int size) {
        boolean isEmpty() {
            return id == EMPTY;
        }

        private String character() {
            return isEmpty() ? "." : "[" + id + "]";
        }

        @Override
        public String toString() {
            return character().repeat(size);
        }

        public static File empty(int size) {
            return new File(EMPTY, size);
        }
    }

    private void defragment(List<File> files) {
        for (int right = files.size() - 1; right >= 0; right--) {
            File rightFile = files.get(right);
            if (rightFile.isEmpty()) {
                continue;
            }

            //Find space
            for (int left = 0; left < right; left++) {
                File leftFile = files.get(left);
                if (!leftFile.isEmpty()) {
                    continue;
                }

                if (leftFile.size < rightFile.size) {
                    continue;
                }

                files.set(left, rightFile);

                int diff = leftFile.size - rightFile.size;
                int emptySpace = rightFile.size;

                if (right < files.size() - 1) {
                    File afterRight = files.get(right + 1);
                    if (afterRight.isEmpty()) {
                        emptySpace += afterRight.size;
                        files.remove(right + 1);
                    }
                }

                File beforeRight = files.get(right - 1);
                if (beforeRight.isEmpty()) {
                    emptySpace += beforeRight.size;
                    files.remove(right - 1);
                    right--;
                }

                files.set(right, File.empty(emptySpace));
                if (diff > 0) {
                    files.add(left + 1, File.empty(diff));
                    right++;
                }

                break;
            }
        }
    }

    private void fragment(List<Integer> disk) {
        int left = 0;
        int right = disk.size() - 1;
        while (left <= right) {
            if (!disk.get(left).equals(EMPTY)) {
                left++;
                continue;
            }

            while (disk.get(right).equals(EMPTY)) {
                right--;
            }
            if (right <= left) {
                break;
            }

            disk.set(left, disk.get(right));
            disk.set(right, EMPTY);

            right--;
            left++;
        }
    }

    private List<File> parseFiles(String line) {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            int value = Integer.parseInt(String.valueOf(line.charAt(i)));
            if (i % 2 == 0) {
                files.add(new File(i / 2, value));
            } else if (value > 0){
                files.add(File.empty(value));
            }

        }
        return files;
    }

    private List<Integer> parseDisk(String line) {
        List<Integer> disk = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            final int index = i;
            int value = Integer.parseInt(String.valueOf(line.charAt(i)));
            if (i % 2 == 0) {
                IntStream.range(0, value).forEach(_ ->
                    disk.add(index / 2)
                );
            } else {
                IntStream.range(0, value).forEach(_ ->
                    disk.add(EMPTY)
                );
            }

        }
        return disk;
    }

    public static void main(String[] ignoredArgs) {
        Day9 puzzle = new Day9();
        puzzle.run();
    }
}
