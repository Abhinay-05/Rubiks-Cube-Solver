package solver;

import model.Cube;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoPhaseSolverTest {

    @Test
    void solvesSolvedCubeImmediately() {
        Cube cube = new Cube();

        TwoPhaseSolver solver = new TwoPhaseSolver();

        assertEquals("", solver.solve(cube));
        assertTrue(cube.isSolved());
    }

    @Test
    void solvesRandomMoveScramble() {
        Cube cube = new Cube();

        cube.applyAlgorithm(
                "R U R' F2 D L2 B U2 R F' D2 L U B2"
        );

        TwoPhaseSolver solver = new TwoPhaseSolver();
        String solution = solver.solve(cube);

        assertNotNull(solution);
        assertFalse(solution.isBlank());

        cube.applyAlgorithm(solution);

        assertTrue(cube.isSolved(),
                "Returned solution did not solve the cube: " + solution);
    }
}
