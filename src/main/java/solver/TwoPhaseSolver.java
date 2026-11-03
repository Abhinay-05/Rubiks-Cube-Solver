package solver;

import model.Cube;
import cs.min2phase.Search;

/**
 * Fast 3x3 solver based on Chen Shuang's min2phase implementation
 * of Kociemba's two-phase algorithm.
 *
 * The existing Cube class remains the application's user-facing model.
 * This class converts that model to the standard URFDLB facelet format,
 * runs the two-phase search, and returns normal R/L/U/D/F/B notation.
 */
public final class TwoPhaseSolver implements Solver {

    private static final int MAX_DEPTH = 21;
    private static final long PROBE_MAX = 100_000_000L;
    private static final long PROBE_MIN = 0L;

    private static volatile boolean initialized = false;

    @Override
    public String solve(Cube cube) {
        if (cube == null) {
            throw new IllegalArgumentException("Cube cannot be null");
        }

        if (cube.isSolved()) {
            return "";
        }

        initialize();

        String facelets = CubeFaceletConverter.toFacelets(cube);

        Search search = new Search();
        String result = search.solution(
                facelets,
                MAX_DEPTH,
                PROBE_MAX,
                PROBE_MIN,
                0
        );

        if (result.startsWith("Error")) {
            throw new IllegalStateException(
                    "Two-phase solver failed: " + explainError(result)
            );
        }

        return result.trim();
    }

    private static void initialize() {
        if (!initialized) {
            synchronized (TwoPhaseSolver.class) {
                if (!initialized) {
                    Search.init();
                    initialized = true;
                }
            }
        }
    }

    private static String explainError(String result) {
        return switch (result) {
            case "Error 1" -> "invalid colour count";
            case "Error 2" -> "not all 12 edge pieces exist exactly once";
            case "Error 3" -> "edge orientation error";
            case "Error 4" -> "not all 8 corner pieces exist exactly once";
            case "Error 5" -> "corner orientation error";
            case "Error 6" -> "permutation parity error";
            case "Error 7" -> "no solution within the maximum depth";
            case "Error 8" -> "phase-2 probe limit exceeded";
            default -> result;
        };
    }
}
