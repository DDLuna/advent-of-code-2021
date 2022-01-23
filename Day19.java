import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day19 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("inputs/day19.txt"));
        Queue<Scanner> scanners = new LinkedList<>();
        String line;
        while (reader.readLine() != null) {
            var beacons = new HashSet<Point>();
            line = reader.readLine();
            while (line != null && !line.equals("")) {
                beacons.add(new Point(line));
                line = reader.readLine();
            }
            scanners.add(new Scanner(beacons));
        }
        if (scanners.isEmpty()) {
            throw new RuntimeException("Invalid input");
        }

        var scannersPositions = new ArrayList<Point>();
        var base = scanners.poll();
        while (!scanners.isEmpty()) {
            var currScanner = scanners.poll();
            Point scannerPosition = base.tryToAddScanner(currScanner);
            if (scannerPosition == null) {
                scanners.add(currScanner);
            } else {
                scannersPositions.add(scannerPosition);
            }
            System.out.println("Queue size: " + scanners.size());
        }
        System.out.println(base.getBeacons().size());
        System.out.println(calculateMaxManhattanDistance(scannersPositions));
    }

    private static int calculateMaxManhattanDistance(ArrayList<Point> points) {
        int max = 0;
        for (var point1 : points) {
            for (var point2 : points) {
                max = Math.max(max,
                        Math.abs(point1.getX() - point2.getX()) +
                                Math.abs(point1.getY() - point2.getY()) +
                                Math.abs(point1.getZ() - point2.getZ()));
            }
        }
        return max;
    }
}

class Point {
    private final int x;
    private final int y;
    private final int z;

    public static final int[][][] ROTATIONS = {
            // Front
            {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}},
            {{1, 0, 0}, {0, 0, -1}, {0, 1, 0}},
            {{1, 0, 0}, {0, -1, 0}, {0, 0, -1}},
            {{1, 0, 0}, {0, 0, 1}, {0, -1, 0}},

            // Up
            {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}},
            {{0, 0, 1},  {1, 0, 0}, {0, 1, 0}},
            {{0, 1, 0},  {1, 0, 0}, {0, 0, -1}},
            {{0, 0, -1}, {1, 0, 0}, {0, -1, 0}},

            // Back
            {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}},
            {{-1, 0, 0}, {0, 0, -1}, {0, -1, 0}},
            {{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}},
            {{-1, 0, 0}, {0, 0, 1}, {0, 1, 0}},

            // Down
            {{0, 1, 0},  {-1, 0, 0}, {0, 0, 1}},
            {{0, 0, 1},  {-1, 0, 0}, {0, -1, 0}},
            {{0, -1, 0}, {-1, 0, 0}, {0, 0, -1}},
            {{0, 0, -1}, {-1, 0, 0}, {0, 1, 0}},

            // Right
            {{0, 0, -1}, {0, 1, 0}, {1, 0, 0}},
            {{0, 1, 0}, {0, 0, 1}, {1, 0, 0}},
            {{0, 0, 1}, {0, -1, 0}, {1, 0, 0}},
            {{0, -1, 0}, {0, 0, -1}, {1, 0, 0}},

            // Left
            {{0, 0, -1}, {0, -1, 0}, {-1, 0, 0}},
            {{0, -1, 0}, {0, 0, 1}, {-1, 0, 0}},
            {{0, 0, 1}, {0, 1, 0}, {-1, 0, 0}},
            {{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}},
    };

    Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Point(String commaSeparatedPoint) {
        this(commaSeparatedPoint.split(","));
    }

    private Point(String[] numbers) {
        this(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Point minus(Point other) {
        return new Point(x - other.x, y - other.y, z - other.z);
    }

    public Point rotate(int[][] matrix) {
        int newX = matrix[0][0] * x + matrix[0][1] * y + matrix[0][2] * z;
        int newY = matrix[1][0] * x + matrix[1][1] * y + matrix[1][2] * z;
        int newZ = matrix[2][0] * x + matrix[2][1] * y + matrix[2][2] * z;
        return new Point(newX, newY, newZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;
        return z == point.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

class Scanner {
    private final static int OVERLAP_NUMBER = 12;

    private final HashSet<Point> beacons = new HashSet<>();

    Scanner(HashSet<Point> beacons) {
        this.beacons.addAll(beacons);
    }

    Scanner(Scanner other, int[][] rotation, Point translation) {
        for (var beacon : other.getBeacons()) {
            beacons.add(beacon.rotate(rotation).minus(translation));
        }
    }

    public HashSet<Point> getBeacons() {
        return beacons;
    }

    public Point tryToAddScanner(Scanner otherScanner) {
        for (var basePoint : beacons) {
            for (var otherPoint : otherScanner.beacons) {
                for (int[][] rotation : Point.ROTATIONS) {
                    var translation = otherPoint.rotate(rotation).minus(basePoint);
                    var transformedScanner = new Scanner(otherScanner, rotation, translation);
                    if (matches(transformedScanner)) {
                        beacons.addAll(transformedScanner.getBeacons());
                        return translation.rotate(new int[][]{{-1,0,0}, {0,-1,0}, {0,0,-1}});
                    }
                }
            }
        }
        return null;
    }

    private boolean matches(Scanner other) {
        int count = 0;
        for (Point p : other.getBeacons()) {
            if (beacons.contains(p)) {
                count++;
            }
        }
        return count >= OVERLAP_NUMBER;
    }

    @Override
    public String toString() {
        return "Scanner{" +
                "beacons=" + beacons +
                '}';
    }
}
