package solver;

import model.Cube;
import model.Moves;

public final class SearchNode {
    private final Cube cube;
    private final SearchNode parent;
    private final Moves move;
    private final int depth;

    public SearchNode(Cube cube, SearchNode parent, Moves move, int depth) {
        this.cube = new Cube(cube);
        this.parent = parent;
        this.move = move;
        this.depth = depth;
    }
    public Cube getCube() { return new Cube(cube); }
    public SearchNode getParent() { return parent; }
    public Moves getMove() { return move; }
    public int getDepth() { return depth; }
}
