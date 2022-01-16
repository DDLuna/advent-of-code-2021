import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        int intersections = howManyIntersect();
        System.out.println("we found " + intersections + " intersections");
    }


    static int howManyIntersect() throws IOException {
        HashMap<Pos, Integer> marked = new HashMap<>();
        ArrayList<Pos[]> input = readInput("inputs/day5.txt");

        for (Pos[] point : input) {
            Pos start = point[0];
            Pos end = point[1];

            if (start.x == end.y && start.y == end.y) {
                marked.put(start, marked.getOrDefault(start, 0) + 1);
            } else if (start.x == end.x) {
                int lower = Math.min(start.y, end.y);
                for (int i = 0; i <= Math.abs(start.y - end.y); i++) {
                    Pos key = new Pos(start.x, lower + i);
                    marked.put(key, marked.getOrDefault(key, 0) + 1);
                }
            } else if (start.y == end.y) {
                int lower = Math.min(start.x, end.x);
                for (int i = 0; i <= Math.abs(start.x - end.x); i++) {
                    Pos key = new Pos(lower + i, start.y);
                    marked.put(key, marked.getOrDefault(key, 0) + 1);
                }
            } else {
                int xDir = start.x < end.x ? 1 : -1;
                int yDir = start.y < end.y ? 1 : -1;
                while(!start.equals(end)) {
                    Pos temp = new Pos(start.x, start.y);
                    marked.put(temp, marked.getOrDefault(temp, 0) + 1);
                    start.x += xDir;
                    start.y += yDir;
                }
                Pos temp = new Pos(start.x, start .y);
                marked.put(temp, marked.getOrDefault(temp, 0) + 1);
            }
        }

        int overlapCount = 0;
        for (int mark : marked.values()) {
            if (mark > 1) {
                overlapCount++;
            }
        }
        return overlapCount;
    }

    private static ArrayList<Pos[]> readInput(String filepath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filepath));
        ArrayList<Pos[]> result = new ArrayList<>();
        for (String line : lines) {
            String[] pair = line.split("( )+->( )+");
            String[] start = pair[0].split(",");
            String[] end = pair[1].split(",");
            int xStart = Integer.parseInt(start[0]);
            int yStart = Integer.parseInt(start[1]);
            int xEnd = Integer.parseInt(end[0]);
            int yEnd = Integer.parseInt(end[1]);
            result.add(new Pos[]{new Pos(xStart, yStart), new Pos(xEnd, yEnd)});
        }
        return result;
    }
}

class Pos {
    int x;
    int y;

    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos pos = (Pos) o;
        return y == pos.y && x == pos.x;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
