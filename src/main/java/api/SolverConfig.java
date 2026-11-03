
package api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solver.Solver;
import solver.TwoPhaseSolver;

@Configuration
public class SolverConfig {

    @Bean
    public Solver cubeSolver() {
        return new TwoPhaseSolver();
    }
}
