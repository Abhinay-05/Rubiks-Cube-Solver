
package api;

import java.util.List;

public record ManualSolveResponse(
        String solution,
        List<String> moves,
        int moveCount,
        boolean solved
) {}
