package app;

import model.Cube;
import model.Moves;
import util.Scrambler;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube();
        cube.printCube();
        cube.scramble();
        System.out.println(cube);
    }
}
