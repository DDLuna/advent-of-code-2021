import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day4 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day4.txt"));
        var numbers = new ArrayList<Integer>();
        for (String numberStr : lines.get(0).split(",")) {
            numbers.add(Integer.valueOf(numberStr));
        }

        var bingos = new ArrayList<Bingo>();
        for (int i = 2; i < lines.size(); i += 6) {
            int[][] matrix = new int[5][5];
            for (int j = 0; j < 5; j++) {
                String[] rowStr = lines.get(i + j).trim().split(" +");
                int[] row = new int[5];
                for (int k = 0; k < 5; k++) {
                    row[k] = Integer.parseInt(rowStr[k]);
                }
                matrix[j] = row;
            }
            bingos.add(new Bingo(matrix));
        }

        for (int num : numbers) {
            for (Bingo bingo : bingos) {
                if (bingo.markNumber(num)) {
                    System.out.println("Winner! score: " + bingo.getScore() * num);
                    return;
                }
            }
        }
    }
}

class Bingo {
    private final int[][] board;
    private final boolean[][] marked;
    private int score;
    private boolean hasWon = false;

    public Bingo(int[][] board) {
        this.board = board;
        marked = new boolean[5][5];
        for (int[] row : board) {
            for (int elem : row) {
                score += elem;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public boolean markNumber(int number) {
        if (hasWon) {
            return true;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == number) {
                    marked[i][j] = true;
                    score -= number;
                }
            }
        }

        for (int row = 0; row < board.length; row++) {
            boolean won = true;
            for (int col = 0; col < board[0].length; col++) {
                if (!marked[row][col]) {
                    won = false;
                    break;
                }
            }
            if (won) {
                hasWon = true;
                return true;
            }
        }

        for (int row = 0; row < board.length; row++) {
            boolean won = true;
            for (int col = 0; col < board[0].length; col++) {
                if (!marked[col][row]) {
                    won = false;
                    break;
                }
            }
            if (won) {
                hasWon = true;
                return true;
            }
        }
        return false;
    }
}
