package solver;

import model.Cube;
import model.Moves;
import util.Algorithm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IDAStarSolver implements Solver {
    private static final int FOUND = -1;
    private static final int MAX_DEPTH = 40;
    private SearchNode solution;

    @Override
    public String solve(Cube cube) {
        if (cube == null) throw new IllegalArgumentException("Cube cannot be null");
        if (cube.isSolved()) return "";

        SearchNode root = new SearchNode(cube, null, null, 0);
        int threshold = Heuristic.estimate(cube);

        while (threshold <= MAX_DEPTH) {
            solution = null;
            int result = search(root, threshold, null);
            if (result == FOUND) return buildSolution(solution);
            if (result == Integer.MAX_VALUE) break;
            threshold = result;
        }
        throw new IllegalStateException("No solution found within depth " + MAX_DEPTH);
    }

    private int search(SearchNode node, int threshold, Moves previous) {
        Cube cube = node.getCube();
        int f = node.getDepth() + Heuristic.estimate(cube);
        if (f > threshold) return f;
        if (cube.isSolved()) { solution = node; return FOUND; }

        int minimum = Integer.MAX_VALUE;
        for (Moves move : Moves.values()) {
            if (previous != null && sameFace(move, previous)) continue;
            Cube next = new Cube(cube);
            next.applyMove(move);
            SearchNode child = new SearchNode(next, node, move, node.getDepth() + 1);
            int result = search(child, threshold, move);
            if (result == FOUND) return FOUND;
            minimum = Math.min(minimum, result);
        }
        return minimum;
    }

    private String buildSolution(SearchNode node) {
        List<Moves> path = new ArrayList<>();
        while (node != null && node.getMove() != null) {
            path.add(node.getMove());
            node = node.getParent();
        }
        Collections.reverse(path);
        return Algorithm.fromMoves(path);
    }

    private boolean sameFace(Moves a, Moves b) { return face(a) == face(b); }
    private char face(Moves m) { return switch (m) {
        case R,R_PRIME,R2 -> 'R'; case L,L_PRIME,L2 -> 'L'; case F,F_PRIME,F2 -> 'F';
        case U,U_PRIME,U2 -> 'U'; case D,D_PRIME,D2 -> 'D'; case B,B_PRIME,B2 -> 'B';
    }; }
}
