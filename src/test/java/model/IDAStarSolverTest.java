package solver;

import model.Cube;
import org.junit.jupiter.api.Test;
import util.Algorithm;
import static org.junit.jupiter.api.Assertions.*;

class IDAStarSolverTest {
    @Test void solvedCubeNeedsNoMoves() { assertEquals("", new IDAStarSolver().solve(new Cube())); }

    @Test void solvesSingleMove() {
        Cube cube = new Cube(); cube.applyAlgorithm("R");
        String solution = new IDAStarSolver().solve(cube);
        Cube check = new Cube(); check.applyAlgorithm("R"); check.applyAlgorithm(solution);
        assertTrue(check.isSolved()); assertEquals(1, Algorithm.length(solution));
    }

    @Test void solvesShortScramble() {
        String scramble = "R U F";
        Cube cube = new Cube(); cube.applyAlgorithm(scramble);
        String solution = new IDAStarSolver().solve(cube);
        Cube check = new Cube(); check.applyAlgorithm(scramble); check.applyAlgorithm(solution);
        assertTrue(check.isSolved());
    }
}
