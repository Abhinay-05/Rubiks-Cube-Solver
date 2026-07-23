package util;

import model.Moves;
import parser.AlgorithmParser;

import java.util.List;

public class Algorithm {
//        inverts a move
        public static String invert (String algorithm){
            String[] tokens = algorithm.trim().split("\\s+");
            int length = tokens.length;;

            StringBuilder inversion = new StringBuilder();
            for(int i=length-1; i>=0; i--){
                inversion.append(invertMove(tokens[i]));
                if(i == 0)continue;
                inversion.append(" ");
            }
            return inversion.toString();
        }
        private static String invertMove(String move){
            return switch (move) {
                case "R" -> "R'";
                case "R'" -> "R";
                case "R2" -> "R2";

                case "L" -> "L'";
                case "L'" -> "L";
                case "L2" -> "L2";

                case "F" -> "F'";
                case "F'" -> "F";
                case "F2" -> "F2";

                case "U" -> "U'";
                case "U'" -> "U";
                case "U2" -> "U2";

                case "D" -> "D'";
                case "D'" -> "D";
                case "D2" -> "D2";

                case "B" -> "B'";
                case "B'" -> "B";
                case "B2" -> "B2";
                default -> throw new IllegalArgumentException("Invalid move: " + move);
            };
        }

//        Normalize moves
//          ex: R R R = R'
        public static String normalize(String algorithm){
            if(algorithm == null || algorithm.isBlank())return "";

            String[] tokens = algorithm.trim().split("\\s+");

            StringBuilder normalized = new StringBuilder();

            int i=0;
            int len = tokens.length;
            while (i < len){
                char currentFace = face(tokens[i]);

                int rotation = 0;
//                compute moves of the same face that appear consecutively
                while(i < len && face(tokens[i]) == currentFace){
                    rotation += amount(tokens[i]);
                    i++;
                }
//                  calculate the final move
                String normalizedMove = makeMove(currentFace, rotation);
//                  add move in Normalized String
                if(!normalizedMove.isEmpty()){
                    if(!normalized.isEmpty()){
                        normalized.append(" ");
                    }
                    normalized.append(normalizedMove);
                }
            }

            return normalized.toString();
        }
//        returns face of the current move
        private static char face(String move) {
            return move.charAt(0);
        }
//        return cardinality of the current move
        private static int amount(String move) {
            if(move.endsWith("2"))//F2
                return 2;

            if(move.endsWith("'"))//F'
                return 3;

            return 1;//F
        }
        private static String makeMove(char face, int rotation){
            rotation %= 4;

            return switch (rotation){
                case 0 -> "";//no move
                case 1 -> String.valueOf(face);//R
                case 2 -> face + "2";//R2
                case 3 -> face + "'";//R3 -> R'
                default -> throw new IllegalStateException();
            };
        }


//      Check if every move is valid
    public static boolean validate(String algorithm){
        try {
            AlgorithmParser.parse(algorithm);//this function already checks it
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

//    Return number of moves
    public static int length(String algorithm){
        String[] tokens = algorithm.trim().split("\\s+");
        return tokens.length;
    }

//    Converts Moves String to MovesList
    public static List<Moves> toMoves(String algorithm){
            return AlgorithmParser.parse(algorithm);
    }

//    Converts MovesList to String of moves
    public static String fromMoves(List<Moves> moves){
            StringBuilder movesString = new StringBuilder();
            for(Moves move : moves){
                movesString.append(moveToString(move));
                movesString.append(" ");
            }
            return movesString.toString();
    }
    private static String moveToString(Moves move){
        return switch (move){
            case Moves.R -> "R";
            case Moves.R_PRIME -> "R'";
            case Moves.R2 -> "R2";

            case Moves.L -> "L";
            case Moves.L_PRIME -> "L'";
            case Moves.L2 -> "L2";

            case Moves.F -> "F";
            case Moves.F_PRIME -> "F'";
            case Moves.F2 -> "F2";

            case Moves.U -> "U";
            case Moves.U_PRIME -> "U'";
            case Moves.U2 -> "U2";

            case Moves.D -> "D";
            case Moves.D_PRIME -> "D'";
            case Moves.D2 -> "D2";

            case Moves.B -> "B";
            case Moves.B_PRIME -> "B'";
            case Moves.B2 -> "B2";
            default -> throw new IllegalArgumentException("Invalid move: " + move);
        };
    }

//    generates a random string
    public static String random(){
            return random(20);
    }
    public static String random(int l){
            return Scrambler.generate(l);
    }
}
