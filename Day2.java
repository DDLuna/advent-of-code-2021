import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> commands = Files.readAllLines(Path.of("inputs/day2.txt"));
        int x = 0;
        int y = 0;
        int aim = 0;
        for (var command : commands) {
            var commandSplit = command.split(" ");
            String direction = commandSplit[0];
            int amount = Integer.parseInt(commandSplit[1]);
            switch (direction) {
                case "forward" -> { x += amount; y += amount * aim; }
                case "down" -> aim += amount;
                case "up" -> aim -= amount;
                default -> throw new RuntimeException("Unrecognized command " + direction);
            }
        }
        System.out.println(x * y);
    }
}
