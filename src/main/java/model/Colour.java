package model;

public enum Colour {
    WHITE,
    RED,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE;

    @Override
    public String toString() {
        return switch (this) {
            case WHITE -> "W";
            case YELLOW -> "Y";
            case RED -> "R";
            case ORANGE -> "O";
            case GREEN -> "G";
            case BLUE -> "B";
        };
    }
}

