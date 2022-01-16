import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10 {

    Map<Character, Character> pairs = Map.of('(', ')', '[', ']', '{', '}', '<', '>');
    Map<Character, Integer> scoreMap = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);
    Map<Character, Integer> scoreMap2 = Map.of(')', 1, ']', 2, '}', 3, '>', 4);

    public static void main(String[] args) {
        Day10 day10 = new Day10();
        List<String> lines = day10.readInput();
        long result = day10.syntaxScore(lines);
        System.out.println("Syntax score: " + result);
    }

    long syntaxScore(List<String> lines) {
        long score = 0;
        var incompleteLinesScore = new ArrayList<Long>();
        for (String line : lines) {
            Stack<Character> s = new Stack<>();
            boolean incomplete = true;
            for (char current : line.toCharArray()) {
                if(pairs.containsKey(current)) {
                    s.add(current);
                } else {
                    char match = s.pop();
                    if (pairs.get(match) != current) {
                        // score += scoreMap.get(current);
                        incomplete = false;
                        break;
                    }
                }
            }
            if (incomplete) {
                score = 0;
                while (!s.isEmpty()) {
                    char opening = s.pop();
                    char closing = pairs.get(opening);
                    score = score * 5 + scoreMap2.get(closing);
                }
                incompleteLinesScore.add(score);
            }
        }
        incompleteLinesScore.sort(Comparator.naturalOrder());
        return incompleteLinesScore.get(incompleteLinesScore.size() / 2);
    }

    List<String> readInput() {
        try {
            return Files.readAllLines(Path.of("inputs/day10.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
