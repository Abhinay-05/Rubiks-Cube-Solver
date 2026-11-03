
package api;

import model.Colour;
import model.Cube;
import model.Face;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CubeValidationService {

    /*
     * Face order:
     * U = Up, R = Right, F = Front,
     * D = Down, L = Left, B = Back
     *
     * Facelet positions use row-major indexing:
     * 0 1 2
     * 3 4 5
     * 6 7 8
     */

    private static final Colour[] FACE_COLOURS = {
            Colour.WHITE,   // U
            Colour.BLUE,    // R
            Colour.RED,     // F
            Colour.YELLOW,  // D
            Colour.GREEN,   // L
            Colour.ORANGE   // B
    };

    // The three facelets belonging to each corner position.
    private static final int[][] CORNER_FACELETS = {
            {8, 0, 2}, // URF
            {6, 0, 2}, // UFL
            {0, 0, 2}, // ULB
            {2, 0, 2}, // UBR
            {2, 8, 6}, // DFR
            {0, 8, 6}, // DLF
            {6, 8, 6}, // DBL
            {8, 8, 6}  // DRB
    };

    // Face index for every corner facelet.
    private static final int[][] CORNER_FACES = {
            {0, 1, 2}, // U R F
            {0, 2, 4}, // U F L
            {0, 4, 5}, // U L B
            {0, 5, 1}, // U B R
            {3, 2, 1}, // D F R
            {3, 4, 2}, // D L F
            {3, 5, 4}, // D B L
            {3, 1, 5}  // D R B
    };

    // The two facelets belonging to each edge position.
    private static final int[][] EDGE_FACELETS = {
            {5, 1}, // UR
            {7, 1}, // UF
            {3, 1}, // UL
            {1, 1}, // UB
            {5, 7}, // DR
            {1, 7}, // DF
            {3, 7}, // DL
            {7, 7}, // DB
            {5, 3}, // FR
            {3, 5}, // FL
            {5, 3}, // BL
            {3, 5}  // BR
    };

    private static final int[][] EDGE_FACES = {
            {0, 1}, // U R
            {0, 2}, // U F
            {0, 4}, // U L
            {0, 5}, // U B
            {3, 1}, // D R
            {3, 2}, // D F
            {3, 4}, // D L
            {3, 5}, // D B
            {2, 1}, // F R
            {2, 4}, // F L
            {5, 4}, // B L
            {5, 1}  // B R
    };

    private static final Colour[][] CORNER_COLOURS = {
            {Colour.WHITE, Colour.BLUE, Colour.RED},
            {Colour.WHITE, Colour.RED, Colour.GREEN},
            {Colour.WHITE, Colour.GREEN, Colour.ORANGE},
            {Colour.WHITE, Colour.ORANGE, Colour.BLUE},
            {Colour.YELLOW, Colour.RED, Colour.BLUE},
            {Colour.YELLOW, Colour.GREEN, Colour.RED},
            {Colour.YELLOW, Colour.ORANGE, Colour.GREEN},
            {Colour.YELLOW, Colour.BLUE, Colour.ORANGE}
    };

    private static final Colour[][] EDGE_COLOURS = {
            {Colour.WHITE, Colour.BLUE},
            {Colour.WHITE, Colour.RED},
            {Colour.WHITE, Colour.GREEN},
            {Colour.WHITE, Colour.ORANGE},
            {Colour.YELLOW, Colour.BLUE},
            {Colour.YELLOW, Colour.RED},
            {Colour.YELLOW, Colour.GREEN},
            {Colour.YELLOW, Colour.ORANGE},
            {Colour.RED, Colour.BLUE},
            {Colour.RED, Colour.GREEN},
            {Colour.ORANGE, Colour.GREEN},
            {Colour.ORANGE, Colour.BLUE}
    };

    public void validate(Cube cube) {

        if (cube == null) {
            throw new IllegalArgumentException("Cube cannot be null.");
        }

        Colour[][] stickers = extractStickers(cube);

        validateCentres(stickers);
        validateColourCount(stickers);

        int[] edgePermutation = validateEdges(stickers);
        int[] cornerPermutation = validateCorners(stickers);

        validateEdgeOrientation(stickers);
        validateCornerOrientation(stickers);

        validatePermutationParity(
                edgePermutation,
                cornerPermutation
        );
    }

    private Colour[][] extractStickers(Cube cube) {

        Face[] faces = {
                cube.getUp(),
                cube.getRight(),
                cube.getFront(),
                cube.getDown(),
                cube.getLeft(),
                cube.getBack()
        };

        Colour[][] stickers = new Colour[6][9];

        for (int face = 0; face < 6; face++) {

            if (faces[face] == null) {
                throw new IllegalArgumentException(
                        "Cube contains a missing face."
                );
            }

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    stickers[face][row * 3 + col] =
                            faces[face].getColour(row, col);

                    if (stickers[face][row * 3 + col] == null) {
                        throw new IllegalArgumentException(
                                "Cube contains an empty sticker."
                        );
                    }
                }
            }
        }

        return stickers;
    }

    private void validateCentres(Colour[][] stickers) {

        for (int face = 0; face < 6; face++) {

            Colour actual = stickers[face][4];

            if (actual != FACE_COLOURS[face]) {
                throw new IllegalArgumentException(
                        "Invalid centre colour on face " +
                                faceName(face) +
                                ". Expected " + FACE_COLOURS[face] +
                                " but found " + actual + "."
                );
            }
        }
    }

    private void validateColourCount(Colour[][] stickers) {

        int[] counts = new int[6];

        for (Colour[] face : stickers) {
            for (Colour colour : face) {

                int index = colourIndex(colour);

                if (index == -1) {
                    throw new IllegalArgumentException(
                            "Cube contains an unknown colour."
                    );
                }

                counts[index]++;
            }
        }

        for (int i = 0; i < 6; i++) {

            if (counts[i] != 9) {
                throw new IllegalArgumentException(
                        "Invalid cube: colour " +
                                FACE_COLOURS[i] +
                                " appears " + counts[i] +
                                " times instead of 9."
                );
            }
        }
    }

    private int[] validateEdges(Colour[][] stickers) {

        int[] permutation = new int[12];
        Set<Integer> foundEdges = new HashSet<>();

        for (int position = 0; position < 12; position++) {

            Colour first = getSticker(
                    stickers,
                    EDGE_FACES[position][0],
                    EDGE_FACELETS[position][0]
            );

            Colour second = getSticker(
                    stickers,
                    EDGE_FACES[position][1],
                    EDGE_FACELETS[position][1]
            );

            int cubie = findEdge(first, second);

            if (cubie == -1) {
                throw new IllegalArgumentException(
                        "Invalid edge piece at position " +
                                (position + 1) +
                                ": " + first + "-" + second +
                                " is not a valid edge."
                );
            }

            if (!foundEdges.add(cubie)) {
                throw new IllegalArgumentException(
                        "Duplicate edge piece found: " +
                                first + "-" + second + "."
                );
            }

            permutation[position] = cubie;
        }

        if (foundEdges.size() != 12) {
            throw new IllegalArgumentException(
                    "Cube must contain exactly 12 unique edges."
            );
        }

        return permutation;
    }

    private int findEdge(Colour first, Colour second) {

        for (int i = 0; i < 12; i++) {

            if (EDGE_COLOURS[i][0] == first &&
                    EDGE_COLOURS[i][1] == second) {
                return i;
            }

            if (EDGE_COLOURS[i][0] == second &&
                    EDGE_COLOURS[i][1] == first) {
                return i;
            }
        }

        return -1;
    }

    private void validateEdgeOrientation(Colour[][] stickers) {

        int flips = 0;

        for (int position = 0; position < 12; position++) {

            Colour first = getSticker(
                    stickers,
                    EDGE_FACES[position][0],
                    EDGE_FACELETS[position][0]
            );

            Colour second = getSticker(
                    stickers,
                    EDGE_FACES[position][1],
                    EDGE_FACELETS[position][1]
            );

            int cubie = findEdge(first, second);

            if (cubie == -1) {
                throw new IllegalArgumentException(
                        "Cannot determine edge orientation."
                );
            }

            if (first != EDGE_COLOURS[cubie][0]) {
                flips++;
            }
        }

        if (flips % 2 != 0) {
            throw new IllegalArgumentException(
                    "Invalid cube: edge orientation is impossible. " +
                            "The number of flipped edges must be even."
            );
        }
    }

    private int[] validateCorners(Colour[][] stickers) {

        int[] permutation = new int[8];
        Set<Integer> foundCorners = new HashSet<>();

        for (int position = 0; position < 8; position++) {

            Colour[] actual = new Colour[3];

            for (int i = 0; i < 3; i++) {
                actual[i] = getSticker(
                        stickers,
                        CORNER_FACES[position][i],
                        CORNER_FACELETS[position][i]
                );
            }

            int cubie = findCorner(actual);

            if (cubie == -1) {
                throw new IllegalArgumentException(
                        "Invalid corner piece at position " +
                                (position + 1) + "."
                );
            }

            if (!foundCorners.add(cubie)) {
                throw new IllegalArgumentException(
                        "Duplicate corner piece found."
                );
            }

            permutation[position] = cubie;
        }

        if (foundCorners.size() != 8) {
            throw new IllegalArgumentException(
                    "Cube must contain exactly 8 unique corners."
            );
        }

        return permutation;
    }

    private int findCorner(Colour[] actual) {

        for (int i = 0; i < 8; i++) {

            for (int orientation = 0; orientation < 3; orientation++) {

                if (actual[orientation] ==
                        CORNER_COLOURS[i][0] &&

                        actual[(orientation + 1) % 3] ==
                                CORNER_COLOURS[i][1] &&

                        actual[(orientation + 2) % 3] ==
                                CORNER_COLOURS[i][2]) {

                    return i;
                }
            }
        }

        return -1;
    }

    private void validateCornerOrientation(Colour[][] stickers) {

        int twists = 0;

        for (int position = 0; position < 8; position++) {

            int orientation = -1;

            for (int i = 0; i < 3; i++) {

                Colour colour = getSticker(
                        stickers,
                        CORNER_FACES[position][i],
                        CORNER_FACELETS[position][i]
                );

                if (colour == Colour.WHITE ||
                        colour == Colour.YELLOW) {

                    orientation = i;
                    break;
                }
            }

            if (orientation == -1) {
                throw new IllegalArgumentException(
                        "Cannot determine corner orientation."
                );
            }

            twists += orientation;
        }

        if (twists % 3 != 0) {
            throw new IllegalArgumentException(
                    "Invalid cube: corner orientation is impossible. " +
                            "The total corner twist must be divisible by 3."
            );
        }
    }

    private void validatePermutationParity(
            int[] edges,
            int[] corners
    ) {

        int edgeParity = calculateParity(edges);
        int cornerParity = calculateParity(corners);

        if (edgeParity != cornerParity) {
            throw new IllegalArgumentException(
                    "Invalid cube: edge and corner permutation parity " +
                            "do not match. This cube cannot be reached by legal moves."
            );
        }
    }

    private int calculateParity(int[] permutation) {

        int inversions = 0;

        for (int i = 0; i < permutation.length; i++) {
            for (int j = i + 1; j < permutation.length; j++) {

                if (permutation[i] > permutation[j]) {
                    inversions++;
                }
            }
        }

        return inversions % 2;
    }

    private Colour getSticker(
            Colour[][] stickers,
            int face,
            int index
    ) {
        return stickers[face][index];
    }

    private int colourIndex(Colour colour) {

        for (int i = 0; i < FACE_COLOURS.length; i++) {
            if (FACE_COLOURS[i] == colour) {
                return i;
            }
        }

        return -1;
    }

    private String faceName(int face) {

        return switch (face) {
            case 0 -> "UP";
            case 1 -> "RIGHT";
            case 2 -> "FRONT";
            case 3 -> "DOWN";
            case 4 -> "LEFT";
            case 5 -> "BACK";
            default -> "UNKNOWN";
        };
    }
}
