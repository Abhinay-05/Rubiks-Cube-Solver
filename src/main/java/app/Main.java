package app;

import input.CubeInput;
import model.Cube;
import solver.IDAStarSolver;
import solver.Solver;

public class Main {

    public static void main(String[] args) {

        Cube cube;

        try {
            cube = CubeInput.readCube();
        } catch (IllegalArgumentException e) {

            System.out.println();
            System.out.println("Invalid cube:");
            System.out.println(e.getMessage());

            return;
        }

        System.out.println("Cube entered successfully.");
        System.out.println();

        System.out.println("Is cube solved? " + cube.isSolved());

        if (!cube.isSolved()) {

            System.out.println();
            System.out.println("Cube is scrambled.");
            System.out.println("Starting solver...");

            Solver solver = new IDAStarSolver();

            String solution = solver.solve(cube);

            System.out.println();
            System.out.println("Solution: " + solution);

            cube.applyAlgorithm(solution);

            System.out.println();
            System.out.println("Solved after applying solution: "
                    + cube.isSolved());
        }
    }
}