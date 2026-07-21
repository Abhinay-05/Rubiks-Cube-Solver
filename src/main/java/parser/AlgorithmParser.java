package parser;

import model.Moves;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmParser {
    public static List<Moves> parse(String algorithm){
        List<Moves> moves = new ArrayList<>();

        if (algorithm == null || algorithm.isBlank()) {
            return moves;
        }

        String[] tokens = algorithm.trim().split("\\s+");

        for (String token : tokens) {
            moves.add(parseMove(token));
        }

        return moves;
    }

    private static Moves parseMove(String move){
        return switch (move){
            case "R" -> Moves.R;
            case "R'" -> Moves.R_PRIME;
            case "R2" -> Moves.R2;

            case "L" -> Moves.L;
            case "L'" -> Moves.L_PRIME;
            case "L2" -> Moves.L2;

            case "F" -> Moves.F;
            case "F'" -> Moves.F_PRIME;
            case "F2" -> Moves.F2;

            case "U" -> Moves.U;
            case "U'" -> Moves.U_PRIME;
            case "U2" -> Moves.U2;

            case "D" -> Moves.D;
            case "D'" -> Moves.D_PRIME;
            case "D2" -> Moves.D2;

            case "B" -> Moves.B;
            case "B'" -> Moves.B_PRIME;
            case "B2" -> Moves.B2;
            default -> throw new IllegalArgumentException("Invalid move: " + move);
        };
    }
}
