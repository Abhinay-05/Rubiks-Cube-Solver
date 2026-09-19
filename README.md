# Rubiks-Cube-Solver
# Rubik's Cube Solver

A modular Java-based 3×3 Rubik's Cube solver that models the cube at sticker level, supports standard cube notation and scrambling, provides an educational IDA* search implementation, and integrates a Kociemba-style two-phase solver for practical solving performance.

## Features

- 3×3 Rubik's Cube representation using six `Face` objects, each containing a 3×3 sticker matrix.
- Six-color model using a Java `enum`.
- Support for all 18 standard face moves:
  - `R`, `R'`, `R2`
  - `L`, `L'`, `L2`
  - `U`, `U'`, `U2`
  - `D`, `D'`, `D2`
  - `F`, `F'`, `F2`
  - `B`, `B'`, `B2`
- Algorithm parsing and execution using standard notation such as:
  ```text
  R U R' F2 D L2
  ```
- Algorithm utilities for parsing, validation, inversion, normalization, and conversion between move lists and notation.
- Random scramble generation.
- Cube copying, equality, and hashing for reliable state handling and testing.
- Educational IDA* solver using heuristic search, iterative `f = g + h` thresholds, depth-first search, and redundant same-face move pruning.
- Kociemba-style two-phase solver integration for practical solving performance.
- Conversion from the project's color-based cube representation to the standard 54-facelet `URFDLB` representation.
- Manual 54-sticker cube input and basic color-count validation.
- End-to-end verification by applying the generated solution and checking `cube.isSolved()`.
- JUnit tests for important cube and solver behavior.

## Project Architecture

```text
src
├── main
│   └── java
│       ├── model
│       │   ├── Cube.java
│       │   ├── Face.java
│       │   ├── Colour.java
│       │   └── Moves.java
│       ├── parser
│       │   └── AlgorithmParser.java
│       ├── util
│       │   ├── Algorithm.java
│       │   └── Scrambler.java
│       ├── solver
│       │   ├── Solver.java
│       │   ├── Neighbor.java
│       │   ├── NeighborGenerator.java
│       │   ├── SearchNode.java
│       │   ├── Heuristic.java
│       │   ├── IDAStarSolver.java
│       │   ├── CubeFaceletConverter.java
│       │   └── TwoPhaseSolver.java
│       ├── input
│       │   └── CubeInput.java
│       └── Main.java
└── test
    └── java
        └── solver
            └── TwoPhaseSolverTest.java
```

## How It Works

```text
Cube Input / Scramble
        │
        ▼
     Cube Model
        │
        ▼
   Move / State Engine
        │
        ▼
      Solver
     /      \
    /        \
 IDA*     Two-Phase
    \        /
     \      /
      Solution
         │
         ▼
  Apply Solution
         │
         ▼
   Verify Solved
```

### 1. Cube Model

The cube is represented as six faces:

```text
        U
    L   F   R   B
        D
```

Each face is a 3×3 matrix of `Colour` values.

The project's solved color mapping is:

| Face | Color |
|---|---|
| U | White |
| R | Blue |
| F | Red |
| D | Yellow |
| L | Green |
| B | Orange |

### 2. Move Engine

`Cube` implements the effects of the standard Rubik's Cube moves. A move rotates the selected face and updates the appropriate rows/columns on adjacent faces.

The engine supports normal, inverse, and double turns.

### 3. Algorithm Parser

`AlgorithmParser` converts human-readable cube notation into the project's `Moves` representation.

Example:

```text
R U R' F2
```

is converted into the corresponding sequence of `Moves`.

### 4. Scrambler

`Scrambler` generates random move sequences that can be applied to a solved cube to create test positions.

Example:

```text
R U2 F' L D B2 R'
```

This is useful for testing and benchmarking.

# Solving

## IDA* Solver

The project includes an educational implementation of **IDA\*** (Iterative Deepening A*).

IDA* combines depth-first search with heuristic evaluation and iterative cost thresholds.

The cost function is:

```text
f(n) = g(n) + h(n)
```

where:

- `g(n)` = number of moves already made
- `h(n)` = estimated number of moves remaining
- `f(n)` = estimated total cost

The solver starts with a threshold based on the heuristic. It performs a depth-first search and prunes nodes whose `f(n)` exceeds the current threshold. If no solution is found, the threshold is increased and the search is repeated.

### Search flow

```text
Start Cube
    │
    ▼
Calculate h(n)
    │
    ▼
Set threshold
    │
    ▼
Depth-first search
    │
    ├── Generate possible moves
    ├── Prune redundant same-face moves
    ├── Calculate f(n) = g(n) + h(n)
    ├── Prune if f(n) > threshold
    └── Continue until solved
```

The IDA* implementation is primarily useful for demonstrating heuristic search and understanding the solving problem.

## Two-Phase Solver

For practical solving performance, the project integrates a **Kociemba-style two-phase solver** through the Java `min2phase` implementation.

The two-phase approach separates solving into two major stages:

```text
Arbitrary Cube
      │
      ▼
   Phase 1
      │
      ▼
Restricted / oriented state
      │
      ▼
   Phase 2
      │
      ▼
Solved Cube
```

Instead of searching the complete cube state space in one step, Phase 1 moves the cube into a restricted subgroup and Phase 2 solves the remaining permutation problem.

The project keeps its own `Cube` representation while using an adapter to communicate with the two-phase solver.

## Cube Facelet Conversion

`CubeFaceletConverter` bridges the project's internal color representation and the standard facelet representation expected by the two-phase solver.

The mapping is:

```text
WHITE  → U
BLUE   → R
RED    → F
YELLOW → D
GREEN  → L
ORANGE → B
```

The six 3×3 faces are serialized into a 54-character facelet string in `URFDLB` order.

This keeps the solver implementation independent from the project's internal cube model.

# Manual Cube Input

The project supports entering a physical cube manually.

The user enters:

```text
UP
RIGHT
FRONT
DOWN
LEFT
BACK
```

Each face contains 9 stickers:

```text
6 × 9 = 54 stickers
```

Example:

```text
UP
W W W
W W W
W W W
```

The input is converted into a `Cube` object and passed to the solver.

The input layer checks that every color occurs exactly nine times.

> Having exactly nine stickers of each color is necessary but not sufficient to prove that a cube position is physically solvable. The two-phase solver performs additional state validation.

# Solver Abstraction

The project uses a `Solver` interface:

```java
public interface Solver {
    String solve(Cube cube);
}
```

This allows different solving algorithms to be swapped without changing the rest of the application.

For example:

```java
Solver solver = new IDAStarSolver();
```

or:

```java
Solver solver = new TwoPhaseSolver();
```

Both expose the same application-level operation:

```text
Cube → solution
```

This also makes future solver implementations easier to add.

# Testing

JUnit is used to test important cube and solver behavior, including:

- Cube copying
- Cube equality
- `hashCode()` consistency
- Move/inverse behavior
- Solving scrambled cubes
- End-to-end solution verification

A typical solver test follows:

```text
Create solved cube
      ↓
Apply scramble
      ↓
Run solver
      ↓
Apply returned solution
      ↓
Verify cube.isSolved()
```

This verifies that the returned move sequence actually solves the cube.

# Technologies

- **Java 21**
- **Maven**
- **JUnit 5**
- **IDA\***
- **Kociemba-style two-phase search**
- **Object-Oriented Design**
- **Depth-First Search / Heuristic Search**

# Running the Project

## Prerequisites

Install:

- JDK 21
- Maven
- IntelliJ IDEA or another Java IDE

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

## Build

From the project root:

```bash
mvn clean compile
```

## Run Tests

```bash
mvn test
```

## Run the Application

Run:

```text
src/main/java/Main.java
```

from IntelliJ IDEA.

Depending on the current `Main` configuration, you can use a generated scramble or enter a cube manually.

# Example

A cube can be scrambled using standard notation:

```text
R U R' F2 D L2 B U2 R F' D2 L U B2
```

The solver returns a move sequence such as:

```text
U' R2 F ...
```

The exact solution depends on the cube state and solver configuration.

The returned solution can then be applied to the cube and verified with:

```java
cube.isSolved()
```

# Design Decisions

### Why a 3×3 face matrix?

It provides a direct representation of individual stickers and makes face, row, and column operations straightforward.

### Why an interface for the solver?

It separates the cube model from the solving algorithm and allows multiple solver implementations.

### Why IDA*?

It demonstrates heuristic search while using much less memory than a conventional A* frontier-based implementation.

### Why add a two-phase solver?

The initial IDA* implementation demonstrated heuristic search but was not sufficiently fast for arbitrary deeper scrambles with a simple sticker-based heuristic. The two-phase approach provides a more specialized and practical solving backend.

### Why keep both?

IDA* demonstrates the search algorithm and provides an educational implementation, while the two-phase solver provides practical solving performance.

# Future Improvements

- [ ] OpenCV webcam integration
- [ ] Automatic detection of the 3×3 cube face
- [ ] Automatic sticker color recognition
- [ ] Scan all six faces using the camera
- [ ] Automatically construct the `Cube` state from camera input
- [ ] Interactive GUI/web interface
- [ ] Visual solution playback
- [ ] Solver performance benchmarking
- [ ] Stronger cube-state validation
- [ ] More advanced search/pruning techniques
- [ ] REST API for remote solving

# Project Goals

This project explores the intersection of:

```text
Object-Oriented Design
        +
Data Structures & Algorithms
        +
Heuristic Search
        +
Combinatorial Optimization
        +
Computer Vision (planned)
```

The architecture is modular so that the cube model, solving algorithms, input methods, and future computer-vision components can evolve independently.

# License / Attribution

The project's own source code can be licensed separately by the author.

The integrated two-phase solver is based on the `min2phase` implementation and its applicable license/attribution requirements should be retained when distributing the project.

See the upstream project and dependency metadata for the exact licensing terms.

# Author

**Abhinay Anand**




**Next:** Computer-vision-based cube scanning using OpenCV.
