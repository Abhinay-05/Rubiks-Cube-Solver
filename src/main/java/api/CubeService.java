package api;

import model.Cube;
import org.springframework.stereotype.Service;
import solver.Solver;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
public class CubeService {
    private final Solver solver;

    public CubeService(Solver solver) {
        this.solver = solver;
    }

    public SolveResponse solve(String scramble){
        Cube cube = new Cube();
        cube.applyAlgorithm(scramble);

//        Preserve the initial state for verification.
        Cube verificationCube = new Cube(cube);

        String solution = solver.solve(cube);

        if(solution == null){
            throw new IllegalStateException("The solver did not return a solution.");
        }

//        Verify the returned algorithm independently.
        verificationCube.applyAlgorithm(solution);

        boolean solved = verificationCube.isSolved();

        if(!solved){
            throw new IllegalStateException(
                    "The returned solution did not solve the cube."
            );
        }

        List<String> moves = Arrays.stream(solution.trim().split("\\s+")).filter(move -> !move.isBlank()).toList();

        return new SolveResponse(
                scramble,
                solution.trim(),
                moves,
                moves.size(),
                true
        );
    }
}
