package solver;

import model.Cube;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NeighborGeneratorTest {
    @Test void generates18Moves() { assertEquals(18, NeighborGenerator.generate(new Cube()).size()); }
}
