import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day6 {
    HashMap<FishPair, BigInteger> cache = new HashMap<>();

    public static void main(String[] args) {
        var day6 = new Day6();
        BigInteger res = day6.howManyLanternFish();
        System.out.println("After 256 days there are " + res + " lanternfish");
        System.out.println("Cache size: " +  day6.cache.size());
    }

    BigInteger howManyLanternFish() {
        ArrayList<Integer> fishes = readInput("inputs/day6.txt");
        BigInteger count = BigInteger.valueOf(fishes.size());
        for (int fish : fishes) {
            count = count.add(howManyFishAfterDays(256, fish));
        }
        return count;
    }

    private BigInteger howManyFishAfterDays(int daysRemaining, int timeToNextFish) {
        if (daysRemaining < timeToNextFish) {
            return BigInteger.ZERO;
        }
        var key = new FishPair(daysRemaining, timeToNextFish);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        BigInteger fishesSpawned =  BigInteger.ZERO;
        for (int i = timeToNextFish; i < daysRemaining; i += 7) {
            fishesSpawned = fishesSpawned.add(BigInteger.ONE);
            fishesSpawned = fishesSpawned.add(howManyFishAfterDays(daysRemaining - i, 9));
        }
        cache.put(new FishPair(daysRemaining, timeToNextFish), fishesSpawned);
        return fishesSpawned;
    }

    private ArrayList<Integer> readInput(String filepath) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(filepath));
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

class FishPair {
    int daysRemaining;
    int timeToNextFish;

    FishPair(int daysRemaining, int timeToNextFish) {
        this.daysRemaining = daysRemaining;
        this.timeToNextFish = timeToNextFish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FishPair fishPair = (FishPair) o;
        return timeToNextFish == fishPair.timeToNextFish && daysRemaining == fishPair.daysRemaining;
    }

    @Override
    public int hashCode() {
        int result = daysRemaining;
        result = 31 * result + timeToNextFish;
        return result;
    }
}
