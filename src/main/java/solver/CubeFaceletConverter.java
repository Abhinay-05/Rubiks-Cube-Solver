package solver;

import model.Colour;
import model.Cube;
import model.Face;

/**
 * Converts the project's colour-based Cube representation to the
 * standard URFDLB facelet string expected by min2phase.
 *
 * The project's solved colour scheme is:
 * U = WHITE
 * R = BLUE
 * F = RED
 * D = YELLOW
 * L = GREEN
 * B = ORANGE
 */
public final class CubeFaceletConverter {

    private CubeFaceletConverter() {}

    public static String toFacelets(Cube cube) {
        StringBuilder result = new StringBuilder(54);

        appendFace(result, cube.getUp());
        appendFace(result, cube.getRight());
        appendFace(result, cube.getFront());
        appendFace(result, cube.getDown());
        appendFace(result, cube.getLeft());
        appendFace(result, cube.getBack());

        return result.toString();
    }

    private static void appendFace(StringBuilder result, Face face) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result.append(toFaceLetter(face.getColour(row, col)));
            }
        }
    }

    private static char toFaceLetter(Colour colour) {
        return switch (colour) {
            case WHITE -> 'U';
            case BLUE -> 'R';
            case RED -> 'F';
            case YELLOW -> 'D';
            case GREEN -> 'L';
            case ORANGE -> 'B';
        };
    }
}
