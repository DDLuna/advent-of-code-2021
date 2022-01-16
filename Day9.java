import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {
    public static void main(String[] args) {
        var day9 = new Day9();
        int res = day9.findLowestPointsSum(day9.readInput());
        System.out.println("Multiplication of 3 largest basins: " + res);
    }

    int findLowestPointsSum(int[][] matrix) {
        int count = 0; // Used only for first part.
        ArrayList<Integer[]> startingPoints = new ArrayList<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                boolean isLowPoint = (row == 0 || matrix[row - 1][col] > matrix[row][col]) &&
                        (col == 0 || matrix[row][col - 1] > matrix[row][col]) &&
                        (row >= matrix.length - 1 || matrix[row + 1][col] > matrix[row][col]) &&
                        (col >= matrix[0].length - 1 || matrix[row][col + 1] > matrix[row][col]);
                if (isLowPoint) {
                    count += matrix[row][col] + 1;
                    startingPoints.add(new Integer[]{row, col});
                }
            }
        }
        PriorityQueue<Integer> biggestBasins = new PriorityQueue<>();
        for (var startingPoint : startingPoints) {
            int size = calculateBasinSize(startingPoint, matrix);
            biggestBasins.add(size);
            if (biggestBasins.size() > 3) {
                biggestBasins.poll();
            }
        }
        return biggestBasins.stream().reduce(1, (mul, elem) -> mul *= elem);
    }

    private int calculateBasinSize(Integer[] startingPoint, int[][] matrix) {
        int size = 0;
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        Queue<Integer[]> next = new LinkedList<>();
        next.add(startingPoint);
        while (!next.isEmpty()) {
            Integer[] current = next.remove();
            int row = current[0];
            int col = current[1];
            if (visited[row][col] || matrix[row][col] == 9) {
                continue;
            }
            visited[row][col] = true;
            size++;
            if (row > 0) {
                next.add(new Integer[]{row - 1, col});
            }
            if (row < matrix.length - 1) {
                next.add(new Integer[]{row + 1, col});
            }
            if (col > 0) {
                next.add(new Integer[]{row, col - 1});
            }
            if (col < matrix[0].length - 1) {
                next.add(new Integer[]{row, col + 1});
            }
        }
        return size;
    }

    int[][] readInput() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of("inputs/day9.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int[][] matrix = new int[lines.size()][];
        for (int rowIndex = 0; rowIndex < lines.size(); rowIndex++) {
            String line = lines.get(rowIndex);
            int[] row = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                row[i] = Integer.parseInt(line.substring(i, i + 1));
            }
            matrix[rowIndex] = row;
        }
        return matrix;
    }
}
