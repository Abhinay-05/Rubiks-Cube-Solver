package solver;

import model.Cube;
import model.Moves;
import java.util.ArrayList;
import java.util.List;

public final class NeighborGenerator {
    private NeighborGenerator() {}
    public static List<Neighbor> generate(Cube cube) {
        List<Neighbor> result = new ArrayList<>(Moves.values().length);
        for (Moves move : Moves.values()) {
            Cube next = new Cube(cube);
            next.applyMove(move);
            result.add(new Neighbor(next, move));
        }
        return result;
    }
}
