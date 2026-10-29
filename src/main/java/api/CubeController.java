package api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CubeController {

    private final CubeService cubeService;

    public CubeController(CubeService cubeService) {
        this.cubeService = cubeService;
    }

    @PostMapping("/solve")
    public SolveResponse solve(
            @Valid @RequestBody SolveRequest request
    ) {
        return cubeService.solve(request.scramble());
    }

    @GetMapping("/health")
    public String health() {
        return "Rubik's Cube Solver API is running";
    }
}
