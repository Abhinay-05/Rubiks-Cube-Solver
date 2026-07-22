package app;

import model.Cube;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube();
        cube.printCube();
        cube.scramble();//20
        System.out.println(cube);
    }
}
