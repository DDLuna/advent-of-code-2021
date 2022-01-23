import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day20 {
    public static void main(String[] args) throws IOException {
        String imageEnhancement;
        var imageChars = new ArrayList<ArrayList<Character>>();
        try (var reader = new BufferedReader(new FileReader("inputs/day20.txt"))) {
            imageEnhancement = reader.readLine();
            reader.readLine();
            reader.lines().forEach(line -> {
                var imageLine = new ArrayList<Character>();
                for (char dot : line.toCharArray()) {
                    imageLine.add(dot);
                }
                imageChars.add(imageLine);
            });
        }
        var part1Image = new Image(imageChars, imageEnhancement);
        for (int i = 0; i < 2; i++) {
            part1Image.enhance();
        }
        var part2Image = new Image(imageChars, imageEnhancement);
        for (int i = 0; i < 50; i++) {
            part2Image.enhance();
        }
        System.out.println(part1Image.pixelsLit());
        System.out.println(part2Image.pixelsLit());
    }
}

class Image {
    private final String enhancement;
    private ArrayList<ArrayList<Character>> image;
    private char unseenPixelType = '.';

    Image(ArrayList<ArrayList<Character>> image, String enhancement) {
        this.image = image;
        this.enhancement = enhancement;
    }

    public void enhance() {
        var dottedRow = new ArrayList<Character>(image.get(0).size());
        for (int i = 0; i < image.get(0).size(); i++) {
            dottedRow.add(unseenPixelType);
        }
        image.add(0, dottedRow);
        image.add(new ArrayList<>(dottedRow));
        for (var row : image) {
            row.add(0, unseenPixelType);
            row.add(unseenPixelType);
        }

        var newImage = new ArrayList<ArrayList<Character>>(image.size());
        for (int row = 0; row < image.size(); row++) {
            var newRow = new ArrayList<Character>(image.get(0).size());
            for (int col = 0; col < image.get(0).size(); col++) {
                if (isGoingToBeLit(row, col)) {
                    newRow.add('#');
                } else {
                    newRow.add('.');
                }
            }
            newImage.add(newRow);
        }
        image = newImage;

        if (unseenPixelType == '.') {
            if (enhancement.charAt(0) == '#') {
                unseenPixelType = '#';
            }
        } else {
            if (enhancement.charAt(0b111_111_111) == '.') {
                unseenPixelType = '.';
            }
        }
    }

    public long pixelsLit() {
        if (unseenPixelType == '#') {
            throw new RuntimeException("Infinite lit pixels");
        }
        return image.stream()
                .map(row -> row.stream().filter(c -> c == '#').count())
                .reduce(0L, (a, e) -> a += e);
    }

    private boolean isGoingToBeLit(int row, int col) {
        var builder = new StringBuilder();
        for (int rowIndex = row - 1; rowIndex <= row + 1; rowIndex++) {
            for (int colIndex = col - 1; colIndex <= col + 1; colIndex++) {
                char pixel;
                try {
                    pixel = image.get(rowIndex).get(colIndex);
                    pixel = pixel == '#' ? '1' : '0';
                } catch (IndexOutOfBoundsException e) {
                    pixel = unseenPixelType == '#' ? '1' : '0';
                }
                builder.append(pixel);
            }
        }
        int enhancerIndex = Integer.parseInt(builder.toString(), 2);
        return enhancement.charAt(enhancerIndex) == '#';
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (var row : image) {
            builder.append('\n');
            for (var pixel : row) {
                builder.append(pixel);
            }
        }
        return builder.toString();
    }
}
