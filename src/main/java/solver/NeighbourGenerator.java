package solver;

import model.Cube;
import model.Moves;
import solver.Neighbour;

import java.util.ArrayList;
import java.util.List;

public final class NeighbourGenerator {

    public static List<Neighbour> generate(Cube cube) {
        List<Neighbour> Neighbours = new ArrayList<>();

        for (Moves move : Moves.values()) {
            Cube copy = new Cube(cube);
            copy.applyMove(move);

            Neighbours.add(new Neighbour(copy, move));
        }

        return Neighbours;
    }
}