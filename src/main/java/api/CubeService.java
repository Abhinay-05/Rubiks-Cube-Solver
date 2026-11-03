
package api;

import model.Cube;
import org.springframework.stereotype.Service;
import solver.Solver;

import java.util.Arrays;
import java.util.List;

@Service
public class CubeService {

    private final Solver solver;
    private final CubeValidationService cubeValidationService;

    public CubeService(
            Solver solver,
            CubeValidationService cubeValidationService
    ) {
        this.solver = solver;
        this.cubeValidationService = cubeValidationService;
    }

    public SolveResponse solve(String scramble) {

        Cube cube = new Cube();

        cube.applyAlgorithm(scramble);

        String solution = solver.solve(cube);

        if (solution == null) {
            throw new IllegalStateException(
                    "Unable to solve the cube."
            );
        }

        List<String> moves = Arrays.stream(
                        solution.trim().split("\\s+")
                )
                .filter(move -> !move.isBlank())
                .toList();

        return new SolveResponse(
                scramble,
                solution.trim(),
                moves,
                moves.size(),
                true
        );
    }

    public ManualSolveResponse solveManualCube(
            CubeInputRequest request
    ) {

        // Step 1: Convert the frontend input into a Cube.
        Cube cube = request.toCube();

        // Step 2: Validate physical cube configuration.
        cubeValidationService.validate(cube);

        // Step 3: Create a copy for solution verification.
        Cube verificationCube = new Cube(cube);

        // Step 4: Solve the cube.
        String solution = solver.solve(cube);

        if (solution == null) {
            throw new IllegalStateException(
                    "Unable to find a solution for this cube."
            );
        }

        // Step 5: Apply the solution to the copy.
        verificationCube.applyAlgorithm(solution);

        boolean solved = verificationCube.isSolved();

        if (!solved) {
            throw new IllegalStateException(
                    "The generated solution failed verification."
            );
        }

        // Step 6: Convert solution into individual moves.
        List<String> moves = Arrays.stream(
                        solution.trim().split("\\s+")
                )
                .filter(move -> !move.isBlank())
                .toList();

        return new ManualSolveResponse(
                solution.trim(),
                moves,
                moves.size(),
                true
        );
    }
}
