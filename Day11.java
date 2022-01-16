import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day11 {
    public static void main(String[] args) {
        int[][] octopuses = readInput();
        long flashes = 0;
        int step = 0;
        while (true) {
            step++;
            // increase energy by 1
            Queue<Integer[]> flashQ = new LinkedList<>();
            for (int j = 0; j < octopuses.length; j++) {
                for (int k = 0; k < octopuses[0].length; k++) {
                    octopuses[j][k]++;
                    if (octopuses[j][k] > 9) {
                        flashQ.add(new Integer[]{j, k});
                    }
                }
            }
            // Flash octopuses
            boolean[][] flashed = new boolean[octopuses.length][octopuses[0].length];
            while (!flashQ.isEmpty()) {
                Integer[] flashPair = flashQ.poll();
                int row = flashPair[0];
                int col = flashPair[1];
                if (flashed[row][col] || octopuses[row][col] <= 9) {
                    continue;
                }
                flashed[row][col] = true;
                flashes++;
                int[] sidesX = new int[]{-1,-1, 0, 1,1,1,0,-1};
                int[] sidesY = new int[]{ 0,-1,-1,-1,0,1,1, 1};
                for (int j = 0; j < 8; j++) {
                    int newRow = row + sidesX[j];
                    int newCol = col + sidesY[j];
                    if (newRow >= 0 && newCol >= 0 && newRow < octopuses.length && newCol < octopuses[0].length) {
                        octopuses[newRow][newCol]++;
                        flashQ.add(new Integer[]{row + sidesX[j], col + sidesY[j]});
                    }
                }
            }
            boolean allFlashed = true;
            for (int j = 0; j < flashed.length; j++) {
                for (int k = 0; k < flashed[0].length; k++) {
                    if (!flashed[j][k]) {
                        allFlashed = false;
                        break;
                    }
                }
            }
            if (allFlashed) {
                System.out.println("Flashed at step: " + step);
                return;
            }

            // Reset flashes
            for (int j = 0; j < octopuses.length; j++) {
                for (int k = 0; k < octopuses[0].length; k++) {
                    if (octopuses[j][k] > 9) {
                        octopuses[j][k] = 0;
                    }
                }
            }
        }
    }

    static int[][] readInput() {
        try {
            List<String> lines = Files.readAllLines(Path.of("inputs/day11.txt"));
            int[][] matrix = new int[lines.size()][];
            for (int row = 0; row < lines.size(); row++) {
                String line = lines.get(row);
                int[] rowArr = new int[line.length()];
                for (int i = 0; i < line.length(); i++) {
                    rowArr[i] = Integer.parseInt(line.substring(i, i + 1));
                }
                matrix[row] = rowArr;
            }
            return matrix;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
