
package api;

import model.Colour;
import model.Cube;
import model.Face;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CubeInputRequest(
        List<List<String>> up,
        List<List<String>> right,
        List<List<String>> front,
        List<List<String>> down,
        List<List<String>> left,
        List<List<String>> back
) {

    public Cube toCube() {

        Face upFace = createFace(up);
        Face rightFace = createFace(right);
        Face frontFace = createFace(front);
        Face downFace = createFace(down);
        Face leftFace = createFace(left);
        Face backFace = createFace(back);

        validateColours(
                upFace, rightFace, frontFace,
                downFace, leftFace, backFace
        );

        return new Cube(
                upFace,
                rightFace,
                frontFace,
                downFace,
                leftFace,
                backFace
        );
    }

    private Face createFace(List<List<String>> stickers) {

        if (stickers == null || stickers.size() != 3) {
            throw new IllegalArgumentException(
                    "Each face must contain exactly 3 rows."
            );
        }

        Colour[][] colours = new Colour[3][3];

        for (int row = 0; row < 3; row++) {

            if (stickers.get(row) == null ||
                    stickers.get(row).size() != 3) {
                throw new IllegalArgumentException(
                        "Each row must contain exactly 3 stickers."
                );
            }

            for (int col = 0; col < 3; col++) {
                colours[row][col] =
                        parseColour(stickers.get(row).get(col));
            }
        }

        Face face = new Face(colours[1][1]);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                face.setColour(row, col, colours[row][col]);
            }
        }

        return face;
    }

    private Colour parseColour(String value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Sticker color cannot be empty."
            );
        }

        return switch (value.trim().toUpperCase()) {
            case "W" -> Colour.WHITE;
            case "R" -> Colour.RED;
            case "B" -> Colour.BLUE;
            case "G" -> Colour.GREEN;
            case "Y" -> Colour.YELLOW;
            case "O" -> Colour.ORANGE;

            default -> throw new IllegalArgumentException(
                    "Invalid color: " + value +
                    ". Use W, R, B, G, Y or O."
            );
        };
    }

    private void validateColours(Face... faces) {

        Map<Colour, Integer> counts = new HashMap<>();

        for (Colour colour : Colour.values()) {
            counts.put(colour, 0);
        }

        for (Face face : faces) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    Colour colour = face.getColour(row, col);

                    counts.put(
                            colour,
                            counts.get(colour) + 1
                    );
                }
            }
        }

        for (Colour colour : Colour.values()) {
            if (counts.get(colour) != 9) {
                throw new IllegalArgumentException(
                        "Invalid cube: each color must appear exactly 9 times."
                );
            }
        }
    }
}
