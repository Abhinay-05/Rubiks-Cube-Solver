package api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolveRequest(
        @NotBlank
        @Size(max = 500)
        String scramble
) {}

