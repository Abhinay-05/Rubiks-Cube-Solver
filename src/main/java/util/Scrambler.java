package util;

import model.Moves;

import java.util.ArrayList;
import java.util.List;

public class Scrambler {
    static String[] moves = {"U", "U'", "U2", "F", "F'", "F2", "L", "L'", "L2", "R", "R'", "R2", "B", "B'", "B2", "D", "D'", "D2"};

    public static String generate(int length){
        StringBuilder movesList = new StringBuilder();

        for (int i = 0; i < length; i++) {
            movesList.append(moves[(int)(Math.random()*18)]);

            if(i == (length-1))continue;

            movesList.append(" ");
        }
        return movesList.toString();
    }

    public static String generate(){
        return generate(20);
    }
}
