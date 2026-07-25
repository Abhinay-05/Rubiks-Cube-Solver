package model;

import org.junit.jupiter.api.Test;
import parser.AlgorithmParser;
import util.Algorithm;
import util.Scrambler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {

    @Test
    void newCubeShouldBeSolved() {
        Cube cube = new Cube();
        assertTrue(cube.isSolved());
    }

    @Test
    void RoriginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.R);
        cube.applyMove(Moves.R_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void UoriginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.U);
        cube.applyMove(Moves.U_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void ForiginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.F);
        cube.applyMove(Moves.F_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void LoriginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.L);
        cube.applyMove(Moves.L_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void DoriginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.D);
        cube.applyMove(Moves.D_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void BoriginalAndPrime(){
        Cube cube = new Cube();
        cube.applyMove(Moves.B);
        cube.applyMove(Moves.B_PRIME);
        assertTrue(cube.isSolved());
    }

    @Test
    void U180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.U2);
        cube.applyMove(Moves.U2);
        assertTrue(cube.isSolved());
    }

    @Test
    void F180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.F2);
        cube.applyMove(Moves.F2);
        assertTrue(cube.isSolved());
    }

    @Test
    void D180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.D2);
        cube.applyMove(Moves.D2);
        assertTrue(cube.isSolved());
    }

    @Test
    void B180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.B2);
        cube.applyMove(Moves.B2);
        assertTrue(cube.isSolved());
    }

    @Test
    void L180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.L2);
        cube.applyMove(Moves.L2);
        assertTrue(cube.isSolved());
    }

    @Test
    void R180(){
        Cube cube = new Cube();
        cube.applyMove(Moves.R2);
        cube.applyMove(Moves.R2);
        assertTrue(cube.isSolved());
    }

    @Test
    void RU105(){
        Cube cube = new Cube();
        for(int i=0; i<105; i++){
            cube.applyMove(Moves.R);
            cube.applyMove(Moves.U);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void R4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.R);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void L4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.L);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void F4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.F);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void D4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.D);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void U4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.U);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void B4(){
        Cube cube = new Cube();
        for (int i = 0; i < 4; i++) {
            cube.applyMove(Moves.B);
        }
        assertTrue(cube.isSolved());
    }

    @Test
    void checkParsing(){
        Cube cube = new Cube();
        System.out.println(cube);
        cube.applyAlgorithm("R U R' U'");
        System.out.println(cube);
    }

    @Test
    void RUUR(){
        Cube cube = new Cube();
        cube.applyAlgorithm("R U U' R'");
        assertTrue(cube.isSolved());
    }

    @Test
    void generatedScrambleHas20Moves() {
        String scramble = Scrambler.generate();

        assertEquals(20,
                scramble.trim().split("\\s+").length);
    }

    @Test
    void generatedScrambleShouldParse() {
        Cube cube = new Cube();
        cube.applyAlgorithm(Scrambler.generate());//20 moves
        cube.printCube();
    }

    @Test
    void generateInvert() {
        String scrambled = Scrambler.generate();
        System.out.println(scrambled);
        System.out.println(Algorithm.invert(scrambled));
    }

    @Test
    void checkNormalize(){
        String scrambled = Algorithm.random(20);
        System.out.println(scrambled);
        String normalized = Algorithm.normalize(scrambled);
        System.out.println(normalized);
    }

    @Test
    void ScrambleThenInverseShouldSolve(){
        boolean ans = true;
        for(int i=0; i<1000; i++){
            if(!ScrambleThenInverseShouldSolve1()){
                ans = false;
                break;
            }
        }
        assertTrue(ans);
    }
    boolean ScrambleThenInverseShouldSolve1() {
        Cube cube = new Cube();

        String scramble = Scrambler.generate();
        cube.applyAlgorithm(scramble);
        cube.applyAlgorithm(Algorithm.invert(scramble));

        return cube.isSolved();
    }

    @Test
    void copyIsEqualToOriginal(){
        Cube cube = new Cube();
        Cube cube1 = new Cube(cube);

        assertTrue(cube.equals(cube1));
    }

    @Test
    void modifyingCopyShouldNotModifyOriginal() {
        Cube cube = new Cube();

        Cube copy = new Cube(cube);

        copy.applyMove(Moves.R);

        assertNotEquals(cube, copy);
        assertTrue(cube.isSolved());
    }

    @Test
    void cubeEqualsItself() {
        Cube cube = new Cube();

        assertEquals(cube, cube);
    }

    @Test
    void equalsShouldBeSymmetric() {
        Cube cube1 = new Cube();
        Cube cube2 = new Cube(cube1);

        assertEquals(cube1, cube2);
        assertEquals(cube2, cube1);
    }

    @Test
    void equalCubesShouldHaveSameHashCode() {
        Cube cube1 = new Cube();
        Cube cube2 = new Cube(cube1);

        assertEquals(cube1.hashCode(), cube2.hashCode());
    }

    @Test
    void everyMoveShouldUndoWithInverse() {

        String[] algorithms = {
                "R R'",
                "L L'",
                "U U'",
                "D D'",
                "F F'",
                "B B'"
        };

        for (String algorithm : algorithms) {

            Cube cube = new Cube();

            cube.applyAlgorithm(algorithm);

            assertTrue(cube.isSolved());
        }
    }

    @Test
    void inverseThenMoveShouldSolve() {

        String[] algorithms = {
                "R' R",
                "L' L",
                "U' U",
                "D' D",
                "F' F",
                "B' B"
        };

        for (String algorithm : algorithms) {

            Cube cube = new Cube();

            cube.applyAlgorithm(algorithm);

            assertTrue(cube.isSolved());
        }
    }

    @Test
    void fourQuarterTurnsShouldSolve() {

        String[] algorithms = {
                "R R R R",
                "L L L L",
                "U U U U",
                "D D D D",
                "F F F F",
                "B B B B"
        };

        for (String algorithm : algorithms) {

            Cube cube = new Cube();

            cube.applyAlgorithm(algorithm);

            assertTrue(cube.isSolved());
        }
    }

    @Test
    void twoHalfTurnsShouldSolve() {

        String[] algorithms = {
                "R2 R2",
                "L2 L2",
                "U2 U2",
                "D2 D2",
                "F2 F2",
                "B2 B2"
        };

        for (String algorithm : algorithms) {

            Cube cube = new Cube();

            cube.applyAlgorithm(algorithm);

            assertTrue(cube.isSolved());
        }
    }

    @Test
    void algorithmFollowedByInverseShouldSolve() {

        String algorithm = "R U R' U' F R2 D";

        Cube cube = new Cube();

        cube.applyAlgorithm(algorithm);
        cube.applyAlgorithm(Algorithm.invert(algorithm));

        assertTrue(cube.isSolved());
    }

    @Test
    void randomScrambleShouldUndo() {

        for (int i = 0; i < 100; i++) {

            Cube cube = new Cube();

            String scramble = Scrambler.generate();

            cube.applyAlgorithm(scramble);
            cube.applyAlgorithm(Algorithm.invert(scramble));

            assertTrue(cube.isSolved());
        }
    }

    @Test
    void parserShouldProduceCorrectMoveCount() {

        List<Moves> moves = AlgorithmParser.parse("R U R' U'");

        assertEquals(4, moves.size());
    }

    @Test
    void validateShouldAcceptValidAlgorithm() {

        assertTrue(
                Algorithm.validate("R U R' U'")
        );
    }

    @Test
    void validateShouldRejectInvalidAlgorithm() {

        assertFalse(
                Algorithm.validate("R X Y")
        );
    }

    @Test
    void multipleCopiesShouldRemainEqual() {

        Cube cube = new Cube();

        Cube copy1 = new Cube(cube);
        Cube copy2 = new Cube(copy1);

        assertEquals(cube, copy2);
    }

    @Test
    void cubesShouldNotBeEqualAfterMove() {

        Cube cube1 = new Cube();
        Cube cube2 = new Cube();

        cube2.applyMove(Moves.R);

        assertNotEquals(cube1, cube2);
    }

    @Test
    void applyAlgorithmShouldMatchIndividualMoves() {

        Cube cube1 = new Cube();
        Cube cube2 = new Cube();

        cube1.applyAlgorithm("R U F");

        cube2.applyMove(Moves.R);
        cube2.applyMove(Moves.U);
        cube2.applyMove(Moves.F);

        assertEquals(cube1, cube2);
    }
}