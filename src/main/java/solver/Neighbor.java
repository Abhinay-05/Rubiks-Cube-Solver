package solver;

import model.Cube;
import model.Moves;
import java.util.Objects;

public final class Neighbor {
    private final Cube cube;
    private final Moves move;

    public Neighbor(Cube cube, Moves move) {
        this.cube = new Cube(Objects.requireNonNull(cube));
        this.move = Objects.requireNonNull(move);
    }
    public Cube getCube() { return new Cube(cube); }
    public Moves getMove() { return move; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neighbor n)) return false;
        return cube.equals(n.cube) && move == n.move;
    }
    @Override public int hashCode() { return Objects.hash(cube, move); }
    @Override public String toString() { return "Neighbor{move=" + move + '}'; }
}
