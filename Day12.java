import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 {
    private final HashMap<String, Set<String>> caveMap;
    Day12() {
        try {
            List<String> lines = Files.readAllLines(Path.of(("inputs/day12.txt")));
            HashMap<String, Set<String>> caveMap = new HashMap<>();
            for (String line : lines) {
                String[] lineSplit = line.split("-");
                String caveA = lineSplit[0];
                String caveB = lineSplit[1];
                var caveAToB = caveMap.getOrDefault(caveA, new HashSet<>());
                var caveBtoA = caveMap.getOrDefault(caveB, new HashSet<>());
                caveAToB.add(caveB);
                caveBtoA.add(caveA);
                caveMap.put(caveA, caveAToB);
                caveMap.put(caveB, caveBtoA);
            }
            this.caveMap = caveMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    long differentPaths() {
        return differentPaths("start", new HashSet<>(), false);
    }

    long differentPaths(String currentCave, HashSet<String> visited, boolean doubleVisit) {
        if (visited.contains(currentCave)) {
            if (!doubleVisit) {
                doubleVisit = true;
            } else {
                return 0;
            }
        }
        if (currentCave.equals("end")) {
            return 1;
        }

        if (currentCave.matches("[a-z]+")) {
            visited.add(currentCave);
        }

        long count = 0;
        for (var nextCave : caveMap.get(currentCave)) {
            if (!nextCave.equals("start")) {
                count += differentPaths(nextCave, new HashSet<>(visited), doubleVisit);
            }
        }
        return count;
    }

    public static void main(String[] args) {
        var day12 = new Day12();
        long result = day12.differentPaths();
        System.out.println(result);
    }
}