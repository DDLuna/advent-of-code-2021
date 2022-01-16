import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day7 {
    public static void main(String[] args) {
        var Day7 = new Day7();
        int res = Day7.leastAmountOfFuel();
        System.out.println("The least amount of fuel to spend is " + res);
    }


    private int leastAmountOfFuel() {
        ArrayList<Integer> positions = readInput();
//        positions.sort(Comparator.naturalOrder());
//        int median = positions.get(positions.size() / 2);

        int average = positions.stream().reduce(0, (subtotal, elem) -> subtotal += elem) / positions.size();
        int fuelCost = 0;
        for (int position : positions) {
            int distance = Math.abs(position - average);
            if (distance > 0) {
                distance = (distance * (1 + distance)) / 2;
            }
            fuelCost += distance;
        }
        return fuelCost;
    }
    private ArrayList<Integer> readInput() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of("inputs/day7.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Integer> res = new ArrayList<>();
        for (String numStr : lines.get(0).split(",")) {
            res.add(Integer.parseInt(numStr));
        }
        return res;
    }
}
