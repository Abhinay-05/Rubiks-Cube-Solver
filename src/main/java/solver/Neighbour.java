package solver;

import model.Cube;
import model.Moves;

import java.util.Objects;

public final class Neighbour {

    private final Cube cube;
    private final Moves move;

    public Neighbour(Cube cube, Moves move) {
        this.cube = Objects.requireNonNull(cube);
        this.move = Objects.requireNonNull(move);
    }

    public Cube getCube() {
        return cube;
    }

    public Moves getMove() {
        return move;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Neighbour other)) return false;

        return cube.equals(other.cube) &&
                move == other.move;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cube, move);
    }

    @Override
    public String toString() {
        return "Neighbor{" +
                "move=" + move +
                ", cube=" + cube +
                '}';
    }
}