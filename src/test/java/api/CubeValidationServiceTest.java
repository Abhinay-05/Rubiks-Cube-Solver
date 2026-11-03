
package api;

import model.Colour;
import model.Cube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeValidationServiceTest {

    private CubeValidationService validator;

    @BeforeEach
    void setUp() {
        validator = new CubeValidationService();
    }

    @Test
    void solvedCubeShouldBeValid() {

        Cube cube = new Cube();

        assertDoesNotThrow(() -> validator.validate(cube));
    }

    @Test
    void scrambledCubeShouldBeValid() {

        // Test multiple randomly scrambled cubes.
        for (int i = 0; i < 20; i++) {

            Cube cube = new Cube();
            cube.scramble(20);

            assertDoesNotThrow(
                    () -> validator.validate(cube),
                    "A legally scrambled cube was rejected."
            );
        }
    }

    @Test
    void singleFlippedEdgeShouldBeInvalid() {

        Cube cube = new Cube();

        // Flip the UR edge by exchanging its two stickers.
        cube.getUp().setColour(1, 2, Colour.BLUE);
        cube.getRight().setColour(0, 1, Colour.WHITE);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(cube)
        );
    }

    @Test
    void singleTwistedCornerShouldBeInvalid() {

        Cube cube = new Cube();

        // Twist the URF corner.
        cube.getUp().setColour(2, 2, Colour.BLUE);
        cube.getRight().setColour(0, 0, Colour.RED);
        cube.getFront().setColour(0, 2, Colour.WHITE);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(cube)
        );
    }

    @Test
    void incorrectCentreShouldBeInvalid() {

        Cube cube = new Cube();

        cube.getUp().setColour(1, 1, Colour.RED);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(cube)
        );
    }

    @Test
    void allLegalMovesShouldPassValidation() {
        String[] moves = {
                "R", "R'", "R2",
                "U", "U'", "U2",
                "L", "L'", "L2",
                "F", "F'", "F2",
                "B", "B'", "B2",
                "D", "D'", "D2"
        };

        CubeValidationService validator = new CubeValidationService();

        for (String move : moves) {
            Cube cube = new Cube();
            cube.applyAlgorithm(move);

            assertDoesNotThrow(
                    () -> validator.validate(cube),
                    "Validation failed for legal move: " + move
            );
        }
    }

    @Test
    void randomScramblesShouldPassValidation() {
        CubeValidationService validator = new CubeValidationService();

        for (int i = 0; i < 100; i++) {
            Cube cube = new Cube();
            cube.scramble(25);

            assertDoesNotThrow(
                    () -> validator.validate(cube),
                    "Validation failed on scramble iteration: " + i
            );
        }
    }
}
