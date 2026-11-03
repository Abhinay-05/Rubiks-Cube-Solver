
import { useState, useCallback, useEffect } from "react";
import CubeScene from "./CubeScene";
import CubeInput from "./CubeInput";
import MoveNotation from "./MoveNotation";
import "./App.css";
import Watermark from "./components/Watermark";

const INITIAL_MOVE_STATUS = {
  status: "idle",
  move: null,
  progress: 0,
  step: 0,
};

function getMoveDetails(move) {
  if (!move) return null;

  const faceNames = {
    R: "Right",
    L: "Left",
    U: "Up",
    D: "Down",
    F: "Front",
    B: "Back",
  };

  const modifier = move.slice(1);

  let direction = "Clockwise";
  let angle = "90°";

  if (modifier === "'") {
    direction = "Anticlockwise";
  } else if (modifier === "2") {
    direction = "Half turn";
    angle = "180°";
  }

  return {
    face: faceNames[move[0]] || "Unknown",
    direction,
    angle,
  };
}

export default function App() {
  const [manualResult, setManualResult] = useState(null);
  const [customFaces, setCustomFaces] = useState(null);
  const [step, setStep] = useState(0);
  const [moveStatus, setMoveStatus] = useState(
    INITIAL_MOVE_STATUS
  );

  const currentSolution = manualResult?.moves || [];
  const totalMoves = currentSolution.length;

  const isCompleted =
    totalMoves > 0 && step === totalMoves;

  const selectedMove =
    totalMoves > 0
      ? currentSolution[Math.min(step, totalMoves - 1)]
      : null;

  const displayedMove =
    moveStatus.status === "animating" ||
    moveStatus.status === "completed" ||
    moveStatus.status === "finished"
      ? moveStatus.move
      : selectedMove;

  const moveDetails = getMoveDetails(displayedMove);

  const handleMoveStatusChange = useCallback((status) => {
    setMoveStatus(status);
  }, []);

  const handleManualResult = useCallback((data) => {
    setManualResult(data);
    setStep(0);
    setMoveStatus(INITIAL_MOVE_STATUS);
  }, []);

  const handleFacesChange = useCallback((faces) => {
    setCustomFaces(faces);
    setManualResult(null);
    setStep(0);
    setMoveStatus(INITIAL_MOVE_STATUS);
  }, []);

  function resetPlayback() {
    setMoveStatus(INITIAL_MOVE_STATUS);
    setStep(0);
  }

  function previousMove() {
    setStep((current) => Math.max(0, current - 1));
  }

  function nextMove() {
    setStep((current) => Math.min(totalMoves, current + 1));
  }

  function goToMove(index) {
    setStep(index + 1);
  }

  // KEYBOARD CONTROLS
  useEffect(() => {
    function handleKeyDown(event) {
      const target = event.target;
      const tagName = target?.tagName?.toLowerCase();

      // Don't interfere with typing in form fields
      if (
        ["input", "textarea", "select"].includes(tagName) ||
        target?.isContentEditable
      ) {
        return;
      }

      // Ignore keyboard shortcuts with modifier keys
      if (event.ctrlKey || event.altKey || event.metaKey) {
        return;
      }

      // Only enable shortcuts when a solution exists
      if (!manualResult || totalMoves === 0) {
        return;
      }

      if (event.key === "ArrowLeft") {
        event.preventDefault();
        previousMove();
      }

      if (event.key === "ArrowRight") {
        event.preventDefault();
        nextMove();
      }
    }

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [manualResult, totalMoves]);

  const trackerStatus =
    moveStatus.status === "animating"
      ? "In progress"
      : moveStatus.status === "finished"
      ? "Solved"
      : moveStatus.status === "completed"
      ? "Move completed"
      : isCompleted
      ? "Completed"
      : "Ready";

  const trackerProgress =
    moveStatus.status === "animating"
      ? moveStatus.progress
      : moveStatus.status === "completed" ||
        moveStatus.status === "finished"
      ? 100
      : 0;

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
          Enter. Solve. <span>Understand.</span>
        </h1>

        <p className="subtitle">
          Enter the colors of your physical Rubik's Cube,
          generate a solution using your Java solving engine,
          and watch every move in an interactive 3D environment.
        </p>
      </section>

      <section className="workspace">
        <div className="cube-panel">
          <p className="eyebrow">3D VIEWPORT</p>
          <h2>Your cube</h2>

          <CubeScene
            scramble=""
            solution={currentSolution}
            step={step}
            customFaces={customFaces}
            onMoveStatusChange={handleMoveStatusChange}
          />

          <p className="hint">
            Drag to rotate the view. Scroll to zoom.
          </p>

          {totalMoves > 0 && (
            <div className="viewer-progress">
              <div className="progress-label">
                <span>Playback progress</span>
                <span>
                  {step} / {totalMoves} moves
                </span>
              </div>

              <div className="progress-track">
                <div
                  className="progress-fill"
                  style={{
                    width: `${(step / totalMoves) * 100}%`,
                  }}
                />
              </div>
            </div>
          )}
        </div>

        <div className="control-panel">
          <p className="eyebrow">SOLVER CONTROLS</p>
          <h2>Enter your cube</h2>

          <p className="hint intro-hint">
            Enter the colors of all six faces of your physical
            Rubik's Cube. Select a face and click its stickers
            to change their colors. The 3D model updates as you
            enter the colors.
          </p>

          <CubeInput
            onResult={handleManualResult}
            onFacesChange={handleFacesChange}
          />

          {manualResult && (
            <div className="solution">
              <div className="solution-heading">
                <div>
                  <h3>Solution found!</h3>

                  <p className="success-message">
                    {manualResult.solved
                      ? "✓ Verified by your Java solver"
                      : "Solution returned by your Java solver"}
                  </p>
                </div>

                <span className="move-count">
                  {manualResult.moveCount} moves
                </span>
              </div>

              {/* LIVE CURRENT MOVE TRACKER */}
              <div className="current-move-card">
                <div className="current-move-top">
                  <div>
                    <p className="eyebrow">
                      {moveStatus.status === "animating"
                        ? "CURRENTLY ANIMATING"
                        : "CURRENT MOVE"}
                    </p>

                    <h3>
                      {displayedMove || (isCompleted ? "✓" : "—")}
                    </h3>
                  </div>

                  <span
                    className={`current-move-status ${
                      moveStatus.status === "finished"
                        ? "finished"
                        : moveStatus.status === "animating"
                        ? "in-progress"
                        : ""
                    }`}
                  >
                    {trackerStatus}
                  </span>
                </div>

                {moveDetails && (
                  <div className="current-move-details">
                    <span>{moveDetails.face} face</span>
                    <span>{moveDetails.direction}</span>
                    <span>{moveDetails.angle}</span>
                  </div>
                )}

                <div className="tracker-progress-section">
                  <div className="tracker-progress-label">
                    <span>Move animation</span>
                    <span>{trackerProgress}%</span>
                  </div>

                  <div className="tracker-progress-track">
                    <div
                      className="tracker-progress-fill"
                      style={{
                        width: `${trackerProgress}%`,
                      }}
                    />
                  </div>
                </div>

                <p className="current-move-counter">
                  {moveStatus.status === "animating"
                    ? `Animating move ${moveStatus.targetStep} of ${totalMoves}`
                    : isCompleted
                    ? "All moves completed successfully."
                    : `Next solution step: ${step + 1} of ${totalMoves}`}
                </p>
              </div>

              <p className="hint">
                Click any move to jump to that step in the 3D animation.
              </p>

              <div className="moves">
                {currentSolution.map((move, index) => {
                  let className = "move";

                  if (index < step) {
                    className += " completed";
                  }

                  if (
                    (step === 0 && index === 0) ||
                    index === step - 1
                  ) {
                    className += " active";
                  }

                  return (
                    <button
                      key={`${move}-${index}`}
                      type="button"
                      className={className}
                      onClick={() => goToMove(index)}
                      aria-label={`Go to move ${index + 1}: ${move}`}
                    >
                      {move}
                    </button>
                  );
                })}
              </div>

              <div className="playback">
                <button
                  type="button"
                  className="secondary"
                  onClick={resetPlayback}
                  disabled={step === 0}
                >
                  ↺ Reset
                </button>

                <button
                  type="button"
                  className="secondary"
                  onClick={previousMove}
                  disabled={step === 0}
                >
                  ← Previous
                </button>

                <button
                  type="button"
                  className="primary"
                  onClick={nextMove}
                  disabled={step === totalMoves}
                >
                  Next →
                </button>
              </div>

              {/* KEYBOARD SHORTCUT HINT */}
              <p className="keyboard-hint">
                Keyboard shortcuts: ← Previous move &nbsp; | &nbsp; → Next move
              </p>

              {isCompleted && (
                <p className="success-message completion-message">
                  ✓ Solution sequence completed!
                </p>
              )}
            </div>
          )}
        </div>
      </section>

      <MoveNotation />

      <Watermark />

      <footer>Built with React, Three.js and Java</footer>
    </main>
  );
}
