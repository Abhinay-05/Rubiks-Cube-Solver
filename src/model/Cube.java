package model;

public class Cube {
//    6 faces of the Cube
    private Face up;
    private Face down;
    private Face front;
    private Face back;
    private Face left;
    private Face right;

//    Ideal Cube
    public Cube(){
        up = new Face(Colour.WHITE);
        front = new Face(Colour.RED);
        down = new Face(Colour.YELLOW);
        back = new Face(Colour.ORANGE);
        left = new Face(Colour.GREEN);
        right = new Face(Colour.BLUE);
    }

//    Check if the cube is valid
    public boolean isSolved() {
        Face[] faces = {up, front, down, back, left, right};

        for (Face face : faces) {
            if (!validFace(face)) {
                return false;
            }
        }
        return true;
    }
//    Check if a particular face is valid
    private boolean validFace(Face face){
        int size = face.getFace().length;//size->3

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(face.getColour(i,j) != face.getColour(1,1)){
                    return false;
                }
            }
        }
        return true;
    }

    public void printCube(){
        Face[] faces = {up, front, down, back, left, right};
        for(Face fc : faces){
            System.out.print(fc + "->");
            fc.printFace();
            System.out.println();
        }
        System.out.println();
    }


    public void applyMove(Moves move){
        switch (move){
            case R -> {
                rotateRightClockwise();
                break;
            }

            case R_PRIME -> {
                rotateRightCounterClockwise();
                break;
            }

            case R2 -> {
                rotateRight180();
                break;
            }

            case U -> {
                rotateUpClockwise();
                break;
            }

            case U_PRIME -> {
                rotateUpCounterClockwise();
                break;
            }

            case U2 -> {
                rotateUp180();
                break;
            }

            case L -> {
                rotateLeftClockwise();
                break;
            }

            case L_PRIME -> {
                rotateLeftCounterClockwise();
                break;
            }

            case L2 -> {
                rotateLeft180();
                break;
            }

            case F -> {
                rotateFrontClockwise();
                break;
            }

            case F_PRIME -> {
                rotateFrontCounterClockwise();
                break;
            }

            case F2 -> {
                rotateFront180();
                break;
            }

            case B -> {
                rotateBackClockwise();
                break;
            }

            case B_PRIME -> {
                rotateBackCounterClockwise();
                break;
            }

            case B2 -> {
                rotateBack180();
                break;
            }

            case D -> {
                rotateDownClockwise();
                break;
            }

            case D_PRIME -> {
                rotateDownCounterClockwise();
                break;
            }

            case D2 -> {
                rotateDown180();
                break;
            }
        }
    }

    public Colour[] reverse(Colour[] cl){
        int s = 0, e = cl.length-1;
        while(s<e){
            Colour temp = cl[s];
            cl[s] = cl[e];
            cl[e] = temp;
            s++;
            e--;
        }
        return cl;
    }

    private void rotateRightClockwise(){
        right.rotateClockwise();

        Colour[] frontCol = front.getColumn(2);
        Colour[] upCol = up.getColumn(2);
        Colour[] backCol = back.getColumn(0);
        Colour[] downCol = down.getColumn(2);

        up.setColumn(2, frontCol);
        back.setColumn(0, reverse(upCol));
        down.setColumn(2, reverse(backCol));
        front.setColumn(2, downCol);
    }

    private void rotateRightCounterClockwise(){
        right.rotateCounterClockwise();

        Colour[] frontCol = front.getColumn(2);
        Colour[] upCol = up.getColumn(2);
        Colour[] backCol = back.getColumn(0);
        Colour[] downCol = down.getColumn(2);

        front.setColumn(2, upCol);
        up.setColumn(2, reverse(backCol));
        back.setColumn(0, reverse(downCol));
        down.setColumn(2, frontCol);
    }

    private void rotateRight180(){
        right.rotate180();

        Colour[] frontCol = front.getColumn(2);
        Colour[] upCol = up.getColumn(2);
        Colour[] backCol = back.getColumn(0);
        Colour[] downCol = down.getColumn(2);

        front.setColumn(2, reverse(backCol));
        back.setColumn(0, reverse(frontCol));
        up.setColumn(2, downCol);
        down.setColumn(2, upCol);
    }

    private void rotateUpClockwise(){
        up.rotateClockwise();

        Colour[] frontRow = front.getRow(0);
        Colour[] leftRow = left.getRow(0);
        Colour[] rightRow = right.getRow(0);
        Colour[] backRow = back.getRow(0);

        front.setRow(0, rightRow);
        left.setRow(0, frontRow);
        back.setRow(0, leftRow);
        right.setRow(0, backRow);
    }

    private void rotateUpCounterClockwise(){
        up.rotateCounterClockwise();

        Colour[] frontRow = front.getRow(0);
        Colour[] leftRow = left.getRow(0);
        Colour[] rightRow = right.getRow(0);
        Colour[] backRow = back.getRow(0);

        front.setRow(0, leftRow);
        right.setRow(0, frontRow);
        back.setRow(0, rightRow);
        left.setRow(0, backRow);
    }

    private void rotateUp180(){
        up.rotate180();

        Colour[] frontRow = front.getRow(0);
        Colour[] leftRow = left.getRow(0);
        Colour[] rightRow = right.getRow(0);
        Colour[] backRow = back.getRow(0);

        front.setRow(0, backRow);
        back.setRow(0, frontRow);
        right.setRow(0, leftRow);
        left.setRow(0, rightRow);
    }

    private void rotateLeftClockwise(){
        left.rotateClockwise();

        Colour[] frontCol = front.getColumn(0);
        Colour[] upCol = up.getColumn(0);
        Colour[] backCol = back.getColumn(2);
        Colour[] downCol = down.getColumn(0);

        front.setColumn(0, upCol);
        down.setColumn(0, frontCol);
        back.setColumn(2, reverse(downCol));
        up.setColumn(0, reverse(backCol));
    }

    private void rotateLeftCounterClockwise(){
        left.rotateCounterClockwise();

        Colour[] frontCol = front.getColumn(0);
        Colour[] upCol = up.getColumn(0);
        Colour[] backCol = back.getColumn(2);
        Colour[] downCol = down.getColumn(0);

        front.setColumn(0,downCol);
        down.setColumn(0, reverse(backCol));
        back.setColumn(2, reverse(upCol));
        up.setColumn(0,frontCol);
    }

    private void rotateLeft180(){
        left.rotate180();

        Colour[] frontCol = front.getColumn(0);
        Colour[] upCol = up.getColumn(0);
        Colour[] backCol = back.getColumn(2);
        Colour[] downCol = down.getColumn(0);

        up.setColumn(0, downCol);
        down.setColumn(0, upCol);
        front.setColumn(0, reverse(backCol));
        back.setColumn(2, reverse(frontCol));
    }

    private void rotateFrontClockwise(){
        right.rotateClockwise();

        Colour[] upRow = up.getRow(2);
        Colour[] rightCol = right.getColumn(0);
        Colour[] downRow = down.getRow(0);
        Colour[] leftCol = left.getColumn(2);

        right.setColumn(0, upRow);
        down.setRow(0, reverse(rightCol));
        left.setColumn(2, downRow);
        up.setRow(2, reverse(leftCol));
    }

    private void rotateFrontCounterClockwise(){
        right.rotateCounterClockwise();

        Colour[] upRow = up.getRow(2);
        Colour[] rightCol = right.getColumn(0);
        Colour[] downRow = down.getRow(0);
        Colour[] leftCol = left.getColumn(2);

        right.setColumn(0, reverse(downRow));
        up.setRow(2, rightCol);
        left.setColumn(2, reverse(upRow));
        down.setRow(0, leftCol);
    }

    private void rotateFront180(){
        right.rotate180();

        Colour[] upRow = up.getRow(2);
        Colour[] rightCol = right.getColumn(0);
        Colour[] downRow = down.getRow(0);
        Colour[] leftCol = left.getColumn(2);

        up.setRow(2, reverse(downRow));
        down.setRow(0, reverse(upRow));
        right.setColumn(0, reverse(leftCol));
        left.setColumn(2, reverse(rightCol));
    }

    private void rotateBackClockwise(){
        back.rotateClockwise();

        Colour[] upRow = up.getRow(0);
        Colour[] leftCol = left.getColumn(0);
        Colour[] downRow = down.getRow(2);
        Colour[] rightCol = right.getColumn(2);

        left.setColumn(0, reverse(upRow));
        down.setRow(2, leftCol);
        right.setColumn(2, reverse(downRow));
        up.setRow(0, rightCol);
    }

    private void rotateBackCounterClockwise(){
        back.rotateCounterClockwise();

        Colour[] upRow = up.getRow(0);
        Colour[] leftCol = left.getColumn(0);
        Colour[] downRow = down.getRow(2);
        Colour[] rightCol = right.getColumn(2);

        up.setRow(0, reverse(leftCol));
        right.setColumn(2, upRow);
        down.setRow(2, reverse(rightCol));
        left.setColumn(0, downRow);
    }

    private void rotateBack180(){
        back.rotate180();

        Colour[] upRow = up.getRow(0);
        Colour[] leftCol = left.getColumn(0);
        Colour[] downRow = down.getRow(2);
        Colour[] rightCol = right.getColumn(2);

        up.setRow(0, reverse(downRow));
        down.setRow(2, reverse(upRow));
        left.setColumn(0, reverse(rightCol));
        right.setColumn(2, reverse(leftCol));
    }

    private void rotateDownClockwise(){
        down.rotateClockwise();

        Colour[] frontRow = front.getRow(2);
        Colour[] leftRow = left.getRow(2);
        Colour[] rightRow = right.getRow(2);
        Colour[] backRown = back.getRow(2);

        right.setRow(2, frontRow);
        back.setRow(2, rightRow);
        left.setRow(2, backRown);
        front.setRow(2, leftRow);
    }

    private void rotateDownCounterClockwise(){
        down.rotateCounterClockwise();

        Colour[] frontRow = front.getRow(2);
        Colour[] leftRow = left.getRow(2);
        Colour[] rightRow = right.getRow(2);
        Colour[] backRown = back.getRow(2);

        front.setRow(2, rightRow);
        right.setRow(2, backRown);
        back.setRow(2, leftRow);
        left.setRow(2, frontRow);
    }

    private void rotateDown180(){
        down.rotate180();

        Colour[] frontRow = front.getRow(2);
        Colour[] leftRow = left.getRow(2);
        Colour[] rightRow = right.getRow(2);
        Colour[] backRown = back.getRow(2);

        front.setRow(2, backRown);
        back.setRow(2, frontRow);
        left.setRow(2, rightRow);
        right.setRow(2, leftRow);
    }
}
