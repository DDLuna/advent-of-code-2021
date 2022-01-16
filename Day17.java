import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Day17 {
    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Path.of("inputs/day17.txt")).get(0);
        var matcher = Pattern.compile(".*x=(\\d+)\\.\\.(\\d+).*y=(-?\\d+)\\.\\.(-?\\d+).*").matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid input");
        }
        int minX = Integer.parseInt(matcher.group(1));
        int maxX = Integer.parseInt(matcher.group(2));
        int minY = Integer.parseInt(matcher.group(3));
        int maxY = Integer.parseInt(matcher.group(4));
        int maxYVelocity = calculateMaxYVelocity(minY);
        int minXVelocity = calculateMinXVelocity(minX);
        int validVelocities = 0;
        for (int xVel = minXVelocity; xVel <= maxX; xVel++) {
            for (int yVel = minY; yVel <= maxYVelocity; yVel++) {
                if (reachesTarget(xVel, yVel, minX, maxX, minY, maxY)) {
                    validVelocities++;
                }
            }
        }
        System.out.println(calculateMaxY(minY, maxY));
        System.out.println(validVelocities);
    }

    private static boolean reachesTarget(int xVel, int yVel, int minX, int maxX, int minY, int maxY) {
        int xVelocity = xVel;
        int yVelocity = yVel;
        int x = 0;
        int y = 0;
        while (true) {
            x += xVelocity;
            y += yVelocity;
            if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                return true;
            }
            if (x > maxX || y < minY) {
                return false;
            }
            xVelocity--;
            xVelocity = Math.max(xVelocity, 0);
            yVelocity--;
        }
    }

    private static int calculateMaxY(int bot, int top) {
        int maxY = 0;
        int velocity = 1;
        while (true) {
            int currentMaxY = (velocity * (velocity + 1)) / 2;
            int velocityDown = - velocity - 1;
            if (velocityDown < bot) {
                break;
            }
            if (velocityDown <= top) {
                maxY = currentMaxY;
            }
            velocity++;
        }
        return maxY;
    }

    public static int calculateMaxYVelocity(int bot) {
        int velocity = 1;
        while (true) {
            int velocityDown = - velocity - 1;
            if (velocityDown < bot) {
                break;
            }
            velocity++;
        }
        return velocity - 1;
    }

    public static int calculateMinXVelocity(int minX) {
        int minXVelocity = 1;
        while ((minXVelocity * (minXVelocity + 1)) / 2 < minX) {
            minXVelocity++;
        }
        return minXVelocity;
    }
}
