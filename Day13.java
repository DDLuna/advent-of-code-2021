import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {
    Set<Point> points = new HashSet<>();
    List<Point> foldBy = new ArrayList<>();

    Day13() {
        try (var br = new BufferedReader(new FileReader("inputs/day13.txt"))) {
            String line = br.readLine();
            while (!line.isEmpty()) {
                String[] numbersStr = line.split(",");
                var p = new Point(Integer.parseInt(numbersStr[0]), Integer.parseInt(numbersStr[1]));
                points.add(p);
                line = br.readLine();
            }
            line = br.readLine();
            while (line != null) {
                String fold = line.split(" ")[2];
                if (fold.charAt(0) == 'x') {
                    foldBy.add(new Point(Integer.parseInt(fold.substring(2)), -1));
                } else {
                    foldBy.add(new Point(-1, Integer.parseInt(fold.substring(2))));
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Day13 day13 = new Day13();
        System.out.println(day13.fold());
        day13.printPoints();
    }

    int fold() {
        for (Point fold : foldBy) {
            var pointsToAdd = new ArrayList<Point>();
            if (fold.y != -1) {
                for (Point p : points) {
                    if (p.y > fold.y) {
                        int d = p.y - fold.y;
                        pointsToAdd.add(new Point(p.x, p.y - 2 * d));
                    }
                }
            } else {
                for (Point p : points) {
                    if (p.x > fold.x) {
                        int d = p.x - fold.x;
                        pointsToAdd.add(new Point(p.x - 2 * d, p.y));
                    }
                }
            }
            points.addAll(pointsToAdd);
            points = points.stream().filter(p -> fold.x != -1 ? p.x < fold.x : p.y < fold.y).collect(Collectors.toSet());
        }
        return points.size();
    }

    void printPoints() {
        int xMax = -1;
        int yMax = -1;
        for (var p : points) {
            xMax = Math.max(xMax, p.x);
            yMax = Math.max(yMax, p.y);
        }
        for (int row = 0; row <= yMax; row++) {
            for (int col = 0; col <= xMax; col++) {
                if (points.contains(new Point(col, row))) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }

    @Override
    public String toString() {
        return "Day13{" +
                "points=" + points +
                ", foldBy=" + foldBy +
                '}';
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
