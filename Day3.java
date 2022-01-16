import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> binaries = Files.readAllLines(Path.of("inputs/day3.txt"));

        int[] onesCount = countChar(binaries, '1');
        int[] zerosCount = countChar(binaries, '0');
        var gammaBuilder = new StringBuilder();
        var epsilonBuilder = new StringBuilder();
        for (int i = 0; i < onesCount.length; i++) {
            if (onesCount[i] > zerosCount[i]) {
                gammaBuilder.append('1');
                epsilonBuilder.append('0');
            } else {
                gammaBuilder.append('0');
                epsilonBuilder.append('1');
            }
        }
        int gammaRate = Integer.parseInt(gammaBuilder.toString(), 2);
        int epsilonRate = Integer.parseInt(epsilonBuilder.toString(), 2);
        System.out.println(gammaRate * epsilonRate);

        List<String> oxygenRatingList = new ArrayList<>(binaries);
        List<String> co2RatingList = new ArrayList<>(binaries);
        for (int i = 0; i < binaries.get(0).length(); i++) {
            if (oxygenRatingList.size() == 1) {
                break;
            }
            onesCount = countChar(oxygenRatingList, '1');
            zerosCount = countChar(oxygenRatingList, '0');
            char mostCommon = onesCount[i] >= zerosCount[i] ? '1' : '0';
            int index = i;
            oxygenRatingList = oxygenRatingList.stream().filter(n -> n.charAt(index) == mostCommon).toList();

        }
        for (int i = 0; i < binaries.get(0).length(); i++) {
            if (co2RatingList.size() == 1) {
                break;
            }
            onesCount = countChar(co2RatingList, '1');
            zerosCount = countChar(co2RatingList, '0');
            char leastCommon = zerosCount[i] <= onesCount[i] ? '0' : '1';
            int index = i;
            co2RatingList = co2RatingList.stream().filter(n -> n.charAt(index) == leastCommon).toList();
        }
        int oxygenRating = Integer.parseInt(oxygenRatingList.get(0), 2);
        int co2Rating = Integer.parseInt(co2RatingList.get(0), 2);
        System.out.println(oxygenRating * co2Rating);
    }

    private static int[] countChar(List<String> binaries, char element) {
        int[] counts = new int[binaries.get(0).length()];
        for (var binary : binaries) {
            for (int i = 0; i < binaries.get(0).length(); i++) {
                if (binary.charAt(i) == element) {
                    counts[i]++;
                }
            }
        }
        return counts;
    }
}
