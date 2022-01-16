import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day8 {
    public static void main(String[] args) {
        var day8 = new Day8();
        int res = day8.readDigits();
        System.out.println("Number of unique segment digits " + res);
    }

    public int readDigits() {
        List<String> lines = readInput();
        int count = 0;
        for (String line : lines) {
            String[] split = line.split(" \\| ");
            var a = new SegmentDisplay(new ArrayList<>(List.of(split[0].split(" "))));
            count += a.readDigits(split[1].split(" "));
        }
        return count;
    }

    private List<String> readInput() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of("inputs/day8.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}

class SegmentDisplay {
    HashMap<String, Integer> segmentMap = new HashMap<>();

    SegmentDisplay(ArrayList<String> digits) {
        // sorted
        for (int i = 0; i < digits.size(); i++) {
            char[] charArr = digits.get(i).toCharArray();
            Arrays.sort(charArr);
            digits.set(i, String.valueOf(charArr));
        }
        String one = "";
        String seven = "";
        String four = "";
        for (String digit : digits) {
            if (digit.length() == 2) { // 1
                one = digit;
                segmentMap.put(digit, 1);
            }
            if (digit.length() == 4) {
                segmentMap.put(digit, 4);
                four = digit;
            }
            if (digit.length() == 3) {
                seven = digit;
                segmentMap.put(digit, 7);
            }
            if (digit.length() == 7) {
                segmentMap.put(digit, 8);
            }
        }
        String top = "";
        for (var c : seven.toCharArray()) {
            if (!one.contains(String.valueOf(c))) {
                top = String.valueOf(c);
            }
        }
        String six = "";
        for (String digit : digits) {
            if (digit.length() == 6) {
                String topRightSide = one.substring(0, 1);
                String botRightSide = one.substring(1, 2);
                if (!digit.contains(topRightSide) || !digit.contains(botRightSide)) {
                    segmentMap.put(digit, 6);
                    six = digit;
                    digits.remove(digit);
                    break;
                }
            }
        }
        String topRightSide = one.substring(0, 1);
        String botRightSide = one.substring(1, 2);
        String uniqueMiddle = "";
        for (char c : four.toCharArray()) {
            String cStr = String.valueOf(c);
            if (!topRightSide.equals(cStr) && !botRightSide.equals(cStr)) {
                uniqueMiddle += cStr;
            }
        }
        String middleOrLeft = uniqueMiddle.substring(0, 1);
        String middleOfLeft2 = uniqueMiddle.substring(1, 2);
        for (String digit : digits) {
            if (digit.length() == 6) {
                if (!digit.contains(middleOrLeft) || !digit.contains(middleOfLeft2)) {
                    segmentMap.put(digit, 0);
                    digits.remove(digit);
                    break;
                }
            }
        }
        // 9 segment is only of length 6 left
        for (String digit : digits) {
            if (digit.length() == 6) {
                segmentMap.put(digit, 9);
                break;
            }
        }
        for (String digit : digits) {
            if (digit.length() == 5) {
                if (digit.contains(topRightSide) && digit.contains(botRightSide)) {
                    segmentMap.put(digit, 3);
                    digits.remove(digit);
                    break;
                }
            }
        }
        for (String digit : digits) {
            if (digit.length() == 5) {
                int countNotFound = 0;
                for (int i = 0; i < six.length(); i++) {
                    if (!digit.contains(six.substring(i, i +1))) {
                        countNotFound++;
                    }
                }
                if (countNotFound == 1) {
                    segmentMap.put(digit, 5);

                } else {
                    segmentMap.put(digit, 2);
                }
            }
        }
    }

    public int readDigits(String[] s) {
        for (int i = 0; i < s.length; i++) {
            char[] charArr = s[i].toCharArray();
            Arrays.sort(charArr);
            s[i] = String.valueOf(charArr);
        }
        int result = 0;
        for (String value : s) {
            result = result * 10 + segmentMap.get(value);
        }
        return result;
    }
}
