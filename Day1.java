import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<Integer> measures = Files.readAllLines(Path.of("inputs/day1.txt")).stream().map(Integer::parseInt).toList();
        var groupedBy3 = new ArrayList<Integer>();
        for (int i = 0; i < measures.size() - 2; i++) {
            groupedBy3.add(measures.get(i) + measures.get(i + 1) + measures.get( i + 2));
        }
        System.out.println(countIncreases(measures));
        System.out.println(countIncreases(groupedBy3));
    }

    private static int countIncreases(List<Integer> measures) {
        int count = 0;
        var previous = measures.get(0);
        int current;
        for (int i = 1; i < measures.size(); i++) {
            current = measures.get(i);
            if (current > previous) {
                count++;
            }
            previous = current;
        }
        return count;
    }
}
