import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day18 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new FileReader("inputs/day18.txt"));
        ArrayList<SnailfishNumber> numbers = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            numbers.add(new SnailfishNumber(line));
        }
//        int mag = numbers.stream().reduce(SnailfishNumber.zero(), SnailfishNumber::sum).magnitude();
//        System.out.println(mag);
        int largestSum = findLargestSumMagnitude(numbers);
        System.out.println(largestSum);
    }

    private static int findLargestSumMagnitude(List<SnailfishNumber> numbers) {
        int max = -1;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                if (i != j) {
                    var left = new SnailfishNumber(numbers.get(i).toString());
                    var right = new SnailfishNumber(numbers.get(j).toString());
                    max = Math.max(max, left.sum(right).magnitude());
                }
            }
        }
        return max;
    }
}

class SnailfishNumber {
    private SnailfishNumber left;
    private SnailfishNumber right;
    private SnailfishNumber parent;
    private int leftRegular;
    private int rightRegular;
    private boolean isNullElement;

    SnailfishNumber(String numberStr) {
        this(numberStr, null);
    }

    SnailfishNumber(String numberStr, SnailfishNumber parent) {
        this.parent = parent;
        Stack<Character> matches = new Stack<>();
        for (int i = 0; i < numberStr.length(); i++) {
            switch (numberStr.charAt(i)) {
                case '[' -> matches.add('[');
                case ']' -> matches.pop();
                case ',' -> {
                    if (matches.size() == 1) {
                        try {
                            leftRegular = Integer.parseInt(numberStr.substring(1, i));
                        } catch (NumberFormatException e) {
                            left = new SnailfishNumber(numberStr.substring(1, i), this);
                        }
                        String rightString = numberStr.substring(i + 1, numberStr.length() - 1);
                        try {
                            rightRegular = Integer.parseInt(rightString);
                        } catch (NumberFormatException e) {
                            right = new SnailfishNumber(rightString, this);
                        }
                        return;
                    }
                }
            }
        }
    }

    SnailfishNumber(SnailfishNumber left, SnailfishNumber right) {
        this.left = left;
        this.right = right;
    }

    SnailfishNumber(int leftRegular, int rightRegular, SnailfishNumber parent) {
        this.leftRegular = leftRegular;
        this.rightRegular = rightRegular;
        this.parent = parent;
    }

    public static SnailfishNumber zero() {
        var nullSnailfishNumber = new SnailfishNumber(0, 0, null);
        nullSnailfishNumber.isNullElement = true;
        return nullSnailfishNumber;
    }

    public SnailfishNumber sum(SnailfishNumber right) {
        if (isNullElement) {
            return right;
        }
        var sum = new SnailfishNumber(this, right);
        this.parent = sum;
        right.parent = sum;
        sum.reduce();
        return sum;
    }

    public int magnitude() {
        int leftMag = left == null ? leftRegular : left.magnitude();
        int rightMag = right == null ? rightRegular : right.magnitude();
        return 3 * leftMag + 2 * rightMag;
    }

    private void reduce() {
        boolean shouldContinue = true;
        while (shouldContinue) {
            shouldContinue = explode();
            if (!shouldContinue) {
                shouldContinue = split();
            }
        }
    }

    private boolean explode() {
       return explode(0);
    }

    private boolean explode(int depth) {
        if (left == null && right == null) {
            if (depth < 4) {
                return false;
            }
            explodeLeft(this);
            explodeRight(this);
            if (this.parent.left == this) {
                this.parent.leftRegular = 0;
                this.parent.left = null;
            } else {
                this.parent.rightRegular = 0;
                this.parent.right = null;
            }
            return true;
        }
        if (left != null && left.explode(depth + 1)) {
            return true;
        }
        return right != null && right.explode(depth + 1);
    }

    private void explodeRight(SnailfishNumber element) {
        int number = element.rightRegular;

        while (!element.isRoot() && element.parent.right == element) {
            element = element.parent;
        }
        if (element.isRoot()) {
            return;
        }
        // Find leftmost value
        element = element.parent;
        if (element.right == null) {
            element.rightRegular += number;
            return;
        }
        element = element.right;
        while (element.left != null) {
            element = element.left;
        }
        element.leftRegular += number;
    }

    private void explodeLeft(SnailfishNumber element) {
        int number = element.leftRegular;

        while (!element.isRoot() && element.parent.left == element) {
            element = element.parent;
        }
        if (element.isRoot()) {
            return;
        }
        // Find rightmost value.
        element = element.parent;
        if (element.left == null) {
            element.leftRegular += number;
            return;
        }
        element = element.left;
        while (element.right != null) {
            element = element.right;
        }
        element.rightRegular += number;
    }

    private boolean split() {
        if (left == null) {
            if (leftRegular >= 10) {
                left = splitNumber(leftRegular, this);
                leftRegular = 0;
                return true;
            }
        } else {
            if (left.split()) {
                return true;
            }
        }
        if (right == null) {
            if (rightRegular >= 10) {
                right = splitNumber(rightRegular, this);
                rightRegular = 0;
                return true;
            }
        } else {
            return right.split();
        }
        return false;
    }

    private SnailfishNumber splitNumber(int number, SnailfishNumber parent) {
        int div = number / 2;
        int remainder = number % 2;
        return new SnailfishNumber(div, div + remainder, parent);
    }

    private boolean isRoot() {
        return parent == null;
    }

    @Override
    public String toString() {
        return "[" + (this.left == null ? leftRegular : left) +
                ',' +
                (this.right == null ? rightRegular : right) +
                ']';
    }
}
