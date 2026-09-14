package input;

import model.Colour;
import model.Cube;
import model.Face;

import java.util.Scanner;

public class CubeInput {

    private static final Scanner scanner = new Scanner(System.in);

    public static Cube readCube() {

        System.out.println("=== Rubik's Cube Input ===");
        System.out.println();
        System.out.println("Enter one character for each sticker:");
        System.out.println("W = White");
        System.out.println("R = Red");
        System.out.println("B = Blue");
        System.out.println("G = Green");
        System.out.println("Y = Yellow");
        System.out.println("O = Orange");
        System.out.println();

        Face front = readFace("FRONT");
        Face right = readFace("RIGHT");
        Face left = readFace("LEFT");
        Face up = readFace("UP");
        Face back = readFace("BACK");
        Face down = readFace("DOWN");

        validateColours(up, right, front, down, left, back);

        return new Cube(up, right, front, down, left, back);
    }

    private static Face readFace(String faceName) {

        System.out.println("Enter " + faceName + " face:");

        Colour[][] stickers = new Colour[3][3];

        for (int row = 0; row < 3; row++) {

            while (true) {

                String input = scanner.nextLine()
                        .trim()
                        .toUpperCase()
                        .replaceAll("\\s+", "");

                if (input.length() != 3) {
                    System.out.println(
                            "Please enter exactly 3 colours, for example: W R B"
                    );
                    continue;
                }

                boolean valid = true;

                for (int col = 0; col < 3; col++) {

                    try {
                        stickers[row][col] =
                                parseColour(input.charAt(col));
                    } catch (IllegalArgumentException e) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    break;
                }

                System.out.println(
                        "Invalid colour. Use only W, R, B, G, Y or O."
                );
            }
        }

        Face face = new Face(stickers[1][1]);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                face.setColour(row, col, stickers[row][col]);
            }
        }

        System.out.println();

        return face;
    }

    private static Colour parseColour(char c) {

        return switch (c) {
            case 'W' -> Colour.WHITE;
            case 'R' -> Colour.RED;
            case 'B' -> Colour.BLUE;
            case 'G' -> Colour.GREEN;
            case 'Y' -> Colour.YELLOW;
            case 'O' -> Colour.ORANGE;
            default -> throw new IllegalArgumentException(
                    "Invalid colour: " + c
            );
        };
    }

    private static void validateColours(Face... faces) {

        int white = 0;
        int red = 0;
        int blue = 0;
        int green = 0;
        int yellow = 0;
        int orange = 0;

        for (Face face : faces) {

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    switch (face.getColour(row, col)) {

                        case WHITE -> white++;
                        case RED -> red++;
                        case BLUE -> blue++;
                        case GREEN -> green++;
                        case YELLOW -> yellow++;
                        case ORANGE -> orange++;
                    }
                }
            }
        }

        if (white != 9 ||
                red != 9 ||
                blue != 9 ||
                green != 9 ||
                yellow != 9 ||
                orange != 9) {

            throw new IllegalArgumentException(
                    "Invalid cube: each colour must appear exactly 9 times."
            );
        }
    }
}