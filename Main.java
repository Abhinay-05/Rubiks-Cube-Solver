import model.Cube;
import model.Moves;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube();

        cube.printCube();
        cube.applyMove(Moves.R);
        cube.printCube();
        cube.applyMove(Moves.R_PRIME);
        cube.printCube();
    }
}