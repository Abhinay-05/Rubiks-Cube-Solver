package solver;

import model.Colour;
import model.Cube;
import model.Face;

public final class Heuristic {
    private Heuristic() {}

    public static int estimate(Cube cube) {
        if (cube.isSolved()) return 0;
        int misplaced = 0;
        Face[] faces = {cube.getUp(), cube.getDown(), cube.getFront(), cube.getBack(), cube.getLeft(), cube.getRight()};
        for (Face face : faces) {
            Colour center = face.getColour(1, 1);
            for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) {
                if (r != 1 || c != 1) if (face.getColour(r, c) != center) misplaced++;
            }
        }
        return (misplaced + 7) / 8;
    }
}
