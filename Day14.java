import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Day14 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day14.txt"));
        var it = lines.iterator();

        var chainStr = it.next();
        var pairCountMap = new HashMap<String, BigInteger>();
        for (int i = 0; i < chainStr.length() -1; i++) {
            String pairKey = chainStr.substring(i, i + 2);
            pairCountMap.put(pairKey, pairCountMap.getOrDefault(pairKey, BigInteger.ZERO).add(BigInteger.ONE));
        }
        var elementCount = new HashMap<Character, BigInteger>();
        for (char c : chainStr.toCharArray()) {
            elementCount.put(c, elementCount.getOrDefault(c, BigInteger.ZERO).add(BigInteger.ONE));
        }

        it.next();
        var pairMap = new HashMap<String, String[]>();
        while (it.hasNext()) {
            var pair = it.next().split(" -> ");
            pairMap.put(pair[0], new String[]{pair[0].charAt(0) + pair[1], pair[1] + pair[0].charAt(1)});
        }

        generatePolymer(pairCountMap, elementCount, pairMap);
    }

    private static void generatePolymer(HashMap<String, BigInteger> pairCountMap,
                                        HashMap<Character, BigInteger> elementCount,
                                        HashMap<String, String[]> pairMap) {

        for (int i = 0; i < 40; i++) {
            System.out.println("Pass number " + (i + 1) + "...");
            var newPairCount = new HashMap<String, BigInteger>();
            for (var pairKey : pairCountMap.keySet()) {
                String[] newPairs = pairMap.get(pairKey);
                BigInteger pairCount = pairCountMap.get(pairKey);
                char newElement = newPairs[0].charAt(1);

                elementCount.put(newElement, elementCount.getOrDefault(newElement, BigInteger.ZERO).add(pairCount));
                newPairCount.put(newPairs[0], newPairCount.getOrDefault(newPairs[0], BigInteger.ZERO).add(pairCount));
                newPairCount.put(newPairs[1], newPairCount.getOrDefault(newPairs[1], BigInteger.ZERO).add(pairCount));
            }
            pairCountMap = newPairCount;
        }

        BigInteger max = elementCount.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        BigInteger min = elementCount.values().stream().min(Comparator.naturalOrder()).orElseThrow();
        System.out.println(max.subtract(min));
    }
}
