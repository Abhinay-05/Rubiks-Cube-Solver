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
                right.rotateClockwise();

                Colour[] frontCol = front.getColumn(2);
                Colour[] upCol = up.getColumn(2);
                Colour[] backCol = back.getColumn(0);
                Colour[] downCol = down.getColumn(2);

                up.setColumn(2, frontCol);
                back.setColumn(0, reverse(upCol));
                down.setColumn(2, reverse(backCol));
                front.setColumn(2, downCol);

                break;
            }

            case R_PRIME -> {
                right.rotateCounterClockwise();

                Colour[] frontCol = front.getColumn(2);
                Colour[] upCol = up.getColumn(2);
                Colour[] backCol = back.getColumn(0);
                Colour[] downCol = down.getColumn(2);

                front.setColumn(2, upCol);
                up.setColumn(2, reverse(backCol));
                back.setColumn(0, reverse(downCol));
                down.setColumn(2, frontCol);

                break;
            }

            case R2 -> {
                right.rotate180();

                Colour[] frontCol = front.getColumn(2);
                Colour[] upCol = up.getColumn(2);
                Colour[] backCol = back.getColumn(0);
                Colour[] downCol = down.getColumn(2);

                front.setColumn(2, reverse(backCol));
                back.setColumn(0, reverse(frontCol));
                up.setColumn(2, downCol);
                down.setColumn(2, upCol);

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
}
