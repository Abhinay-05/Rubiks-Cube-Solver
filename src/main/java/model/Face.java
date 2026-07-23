package model;

import java.util.Arrays;

public class Face {
    private static final int SIZE = 3;
    private Colour[][] face = new Colour[SIZE][SIZE];

    //    Provide colour to a face
//    fill it with same colour
    public Face(Colour colour){
        for(Colour[] fc : face){
            Arrays.fill(fc, colour);
        }
    }

    public Face(Face other) {
        face = new Colour[3][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(other.face[i], 0, face[i], 0, 3);
        }
    }

    //    return colour of a sticker
    public Colour getColour(int row, int col){
        return face[row][col];
    }

    //    set colour of a sticker
    public void setColour(int r, int c, Colour cl){
        face[r][c] = cl;
    }

    //    return a face of the Cube
    public Colour[][] getFace(){
        Colour[][] copyFace = new Colour[SIZE][SIZE];

        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                copyFace[i][j] = face[i][j];
            }
        }
        return copyFace;
    }

    public void setFace(Face face){
        for(int i=0; i<SIZE; i++){
            this.setRow(i, face.getRow(i));
        }
    }

    //    return a particular row
    public Colour[] getRow(int r){
        Colour[] copy = new Colour[SIZE];

        for(int i=0; i<SIZE; i++){
            copy[i] = face[r][i];
        }

        return copy;
    }

    //    return a column
    public Colour[] getColumn(int c){
        Colour[] copy = new Colour[SIZE];

        for(int i=0; i<SIZE; i++){
            copy[i] = face[i][c];
        }

        return copy;
    }

    //    change a particular row
    public void setRow(int r, Colour[] row) throws IllegalArgumentException{
        if (row == null || row.length != SIZE) {
            throw new IllegalArgumentException("Invalid row");
        }

        for(int i=0; i<SIZE; i++){
            face[r][i] = row[i];
        }
    }

    //     change a particular column
    public void setColumn(int c, Colour[] col) throws IllegalArgumentException{
        if (col == null || col.length != SIZE) {
            throw new IllegalArgumentException("Invalid row");
        }

        for(int i=0; i<SIZE; i++){
            face[i][c] = col[i];
        }
    }

    //    rotate face clock-wise 90°
    public void rotateClockwise(){
//        1. reverse rows
        for(Colour[] fc : face){
            reverseRow(fc);
        }
//        2. transpose matrix along anti-diagonal{/}
        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE-i-1; j++){
                Colour temp = face[i][j];
                face[i][j] = face[SIZE-j-1][SIZE-i-1];
                face[SIZE-j-1][SIZE-i-1] = temp;
            }
        }
    }
    //    reverse a row
    public void reverseRow(Colour[] row){
        int s = 0, e = SIZE-1;
        while(s<e){
            Colour temp = row[s];
            row[s] = row[e];
            row[e] = temp;
            s++;
            e--;
        }
    }

    public void rotateCounterClockwise(){
//        1.reverse row
        for(Colour[] row : face){
            reverseRow(row);
        }
//        2.transpose along principal diagonal{\}
        for(int i=0; i<SIZE; i++){
            for(int j=i+1; j<SIZE; j++){
                Colour temp = face[i][j];
                face[i][j] = face[j][i];
                face[j][i] = temp;
            }
        }
    }
    //  rotate 180°
    public void rotate180(){
//        1. reverse rows
        for(Colour[] row : face){
            reverseRow(row);
        }
//        2. reverse columns
        for(int i=0; i<SIZE; i++){
            reverseColumn(i);
        }
    }
    //    reverse a column
    public void reverseColumn(int col){
        int s = 0, e = SIZE-1;
        while(s<e){
            Colour temp = face[s][col];
            face[s][col] = face[e][col];
            face[e][col] = temp;
            s++;
            e--;
        }
    }

    public void printFace(){
        for(Colour[] fc : face){
            System.out.print(Arrays.toString(fc));
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Face other))
            return false;

        return Arrays.deepEquals(face, other.face);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(face);
    }


}

