
import { useState } from "react";
import CubeScene from "./CubeScene";
import "./App.css";

const API = "http://localhost:8080/api";

const MOVES = [
  "U", "U'", "U2",
  "D", "D'", "D2",
  "L", "L'", "L2",
  "R", "R'", "R2",
  "F", "F'", "F2",
  "B", "B'", "B2",
];

function generateScramble(length = 20) {
  const result = [];

  while (result.length < length) {
    const move = MOVES[Math.floor(Math.random() * MOVES.length)];

    if (
      result.length === 0 ||
      result[result.length - 1][0] !== move[0]
    ) {
      result.push(move);
    }
  }

  return result.join(" ");
}

export default function App() {
  const [scramble, setScramble] = useState("");
  const [solution, setSolution] = useState([]);
  const [step, setStep] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [verified, setVerified] = useState(false);

  async function solveCube() {
    if (!scramble.trim()) {
      setError("Enter a scramble or generate a random one.");
      return;
    }

    setLoading(true);
    setError("");
    setSolution([]);
    setStep(0);
    setVerified(false);

    try {
      const response = await fetch(`${API}/solve`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          scramble: scramble.trim(),
        }),
      });

      if (!response.ok) {
        const message = await response.text();
        throw new Error(
          message || `Server error: ${response.status}`
        );
      }

      const data = await response.json();

      if (!Array.isArray(data.moves)) {
        throw new Error("Invalid solution received from the backend.");
      }

      setSolution(data.moves);
      setVerified(data.solved === true);
    } catch (err) {
      setError(
        err.message === "Failed to fetch"
          ? "Cannot connect to Spring Boot. Check that it is running on port 8080."
          : err.message
      );
    } finally {
      setLoading(false);
    }
  }

  function createScramble() {
    setScramble(generateScramble());
    setSolution([]);
    setStep(0);
    setVerified(false);
    setError("");
  }

  function reset() {
    setScramble("");
    setSolution([]);
    setStep(0);
    setVerified(false);
    setError("");
  }

  return (
    <main className="app">
      <header className="topbar">
        <a className="brand" href="#">
          <span className="brand-icon">▦</span>
          Cube<span className="brand-accent">Lab</span>
        </a>

        <span className="status">
          <span className="status-dot" />
          Java solver connected
        </span>
      </header>

      <section className="hero">
        <p className="eyebrow">INTERACTIVE CUBE SOLVER</p>

        <h1>
          Scramble. Solve. <span>Understand.</span>
        </h1>

        <p className="subtitle">
          Explore your Rubik's Cube in 3D and solve it using
          your Java solving engine.
        </p>
      </section>

      <section className="workspace">
        <div className="cube-panel">
          <p className="eyebrow">3D VIEWPORT</p>
          <h2>Your cube</h2>

          <CubeScene />

          <p className="hint">
            Drag to rotate the view. Scroll to zoom.
          </p>
        </div>

        <div className="control-panel">
          <p className="eyebrow">SOLVER CONTROLS</p>
          <h2>Solve your cube</h2>

          <label htmlFor="scramble">Scramble notation</label>

          <textarea
            id="scramble"
            rows={4}
            value={scramble}
            onChange={(event) => setScramble(event.target.value)}
            placeholder="R U R' U' F2 D ..."
            disabled={loading}
          />

          <button
            className="secondary"
            onClick={createScramble}
            disabled={loading}
          >
            Random scramble
          </button>

          <button
            className="primary"
            onClick={solveCube}
            disabled={loading}
          >
            {loading ? "Solving..." : "Solve cube →"}
          </button>

          <button
            className="secondary"
            onClick={reset}
            disabled={loading}
          >
            Reset
          </button>

          {error && (
            <p className="error" role="alert">
              {error}
            </p>
          )}

          <div className="solution">
            <h3>Solution sequence</h3>

            {verified && (
              <p className="success-message">
                ✓ Solution verified by Java
              </p>
            )}

            {solution.length > 0 ? (
              <>
                <p className="hint">
                  {solution.length} moves · Step {step} of {solution.length}
                </p>

                <div className="moves">
                  {solution.map((move, index) => (
                    <button
                      key={`${index}-${move}`}
                      className={
                        index < step
                          ? "move completed"
                          : index === step
                            ? "move active"
                            : "move"
                      }
                      onClick={() => setStep(index + 1)}
                      disabled={loading}
                    >
                      {move}
                    </button>
                  ))}
                </div>

                <div className="playback">
                  <button
                    className="secondary"
                    onClick={() => setStep(Math.max(0, step - 1))}
                    disabled={step === 0}
                  >
                    ← Previous
                  </button>

                  <button
                    className="primary"
                    onClick={() =>
                      setStep(Math.min(solution.length, step + 1))
                    }
                    disabled={step === solution.length}
                  >
                    Next →
                  </button>
                </div>

                {step === solution.length && (
                  <p className="success-message">
                    Solution sequence completed!
                  </p>
                )}
              </>
            ) : (
              <p className="hint">
                Enter a scramble and click Solve cube to see
                the solution returned by your Java solver.
              </p>
            )}
          </div>
        </div>
      </section>

      <footer>
        Built with React, Three.js and Java
      </footer>
    </main>
  );
}
