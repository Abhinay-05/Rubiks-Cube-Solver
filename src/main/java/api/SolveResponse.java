package api;

import java.util.List;

public record SolveResponse(
        String scramble,
        String solution,
        List<String> moves,
        int moveCount,
        boolean solved
) {}
