import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Day15 {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        List<String> lines = Files.readAllLines(Path.of("inputs/day15.txt"));
        lines = expandLines(lines);

        int[][] graph = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                graph[i][j] = lines.get(i).charAt(j) - '0';
            }
        }
        boolean[][] inQueue = new boolean[lines.size()][lines.get(0).length()];
        int[][] distances = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        int result = 0;
        distances[0][0] = 0;
        var q = new PriorityQueue<Node>();
        q.add(new Node(0,0,0));
        inQueue[0][0] = true;
        while (!q.isEmpty()) {
            Node currentNode = q.poll();
            if (currentNode.row == graph.length - 1 && currentNode.col == graph[0].length - 1) {
                result = distances[currentNode.row][currentNode.col];
                break;
            }
            var neighbors = new ArrayList<Integer[]>();
            if (currentNode.row > 0) neighbors.add(new Integer[]{currentNode.row - 1, currentNode.col});
            if (currentNode.col > 0) neighbors.add(new Integer[]{currentNode.row, currentNode.col - 1});
            if (currentNode.row < graph.length - 1) neighbors.add(new Integer[]{currentNode.row + 1, currentNode.col});
            if (currentNode.col < graph[0].length - 1) neighbors.add(new Integer[]{currentNode.row, currentNode.col + 1});
            for (var neighbor : neighbors) {
                Integer row = neighbor[0];
                Integer col = neighbor[1];
                if (!inQueue[row][col]) {
                    distances[row][col] = Math.min(distances[row][col], currentNode.distance + graph[row][col]);
                    q.add(new Node(row, col, distances[row][col]));
                    inQueue[row][col] = true;
                }
            }
        }
        System.out.println((System.currentTimeMillis() - start));
        System.out.println(result);
    }

    private static List<String> expandLines(List<String> lines) {
        var expanded = new ArrayList<String>();
        for (var line : lines) {
            var builder = new StringBuilder(line);
            for (int i = 1; i < 5; i++) {
                for (char num : line.toCharArray()) {
                    num -= '0';
                    byte sum = (byte) (num + i);
                    num = (char) ((sum % 10 + sum / 10) + '0');
                    builder.append(num);
                }
            }
            expanded.add(builder.toString());
        }
        var rows = new ArrayList<String>();
        for (int i = 1; i < 5; i++) {
            for (String line : expanded) {
                var builder = new StringBuilder();
                for (char num : line.toCharArray()) {
                    num -= '0';
                    byte sum = (byte) (num + i);
                    num = (char) ((sum % 10 + sum / 10) + '0');
                    builder.append(num);
                }
                rows.add(builder.toString());
            }
        }
        expanded.addAll(rows);
        return expanded;
    }

    private static class Node implements Comparable<Node> {
        int row;
        int col;
        int distance;

        Node(int row, int col, int distance) {
            this.row = row;
            this.col = col;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node o) {
            return this.distance - o.distance;
        }
    }
}
