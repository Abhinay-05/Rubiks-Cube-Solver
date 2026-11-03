
import { useState, useEffect } from "react";
import "./CubeInput.css";

const COLORS = [
  { id: "W", name: "White", hex: "#ffffff" },
  { id: "R", name: "Red", hex: "#ef4444" },
  { id: "B", name: "Blue", hex: "#3b82f6" },
  { id: "G", name: "Green", hex: "#22c55e" },
  { id: "Y", name: "Yellow", hex: "#facc15" },
  { id: "O", name: "Orange", hex: "#f97316" },
];

const FACES = [
  { id: "up", label: "Up" },
  { id: "right", label: "Right" },
  { id: "front", label: "Front" },
  { id: "down", label: "Down" },
  { id: "left", label: "Left" },
  { id: "back", label: "Back" },
];

const SOLVED_CUBE = {
  up: Array(9).fill("W"),
  right: Array(9).fill("B"),
  front: Array(9).fill("R"),
  down: Array(9).fill("Y"),
  left: Array(9).fill("G"),
  back: Array(9).fill("O"),
};

// Use the deployed backend in production.
// Fall back to localhost during local development.
const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const API = `${API_BASE_URL.replace(/\/$/, "")}/api`;

function copySolvedCube() {
  return Object.fromEntries(
    Object.entries(SOLVED_CUBE).map(([key, value]) => [
      key,
      [...value],
    ])
  );
}

export default function CubeInput({ onResult, onFacesChange }) {
  const [faces, setFaces] = useState(copySolvedCube);
  const [activeFace, setActiveFace] = useState("up");
  const [selectedColor, setSelectedColor] = useState("W");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Keep the 3D cube synchronized with the editor.
  useEffect(() => {
    onFacesChange?.(faces);
  }, [faces, onFacesChange]);

  function updateSticker(index) {
    if (index === 4) return;

    setFaces(previous => ({
      ...previous,
      [activeFace]: previous[activeFace].map(
        (color, i) => i === index ? selectedColor : color
      ),
    }));

    setError("");
    onResult(null);
  }

  function resetCube() {
    setFaces(copySolvedCube());
    setError("");
    onResult(null);
  }

  function getColorCount() {
    const counts = Object.fromEntries(
      COLORS.map(color => [color.id, 0])
    );

    Object.values(faces).flat().forEach(color => {
      counts[color]++;
    });

    return counts;
  }

  async function solveManualCube() {
    const counts = getColorCount();

    const invalid = Object.entries(counts).filter(
      ([, count]) => count !== 9
    );

    if (invalid.length > 0) {
      setError(
        "Every color must appear exactly 9 times. " +
        invalid.map(([color, count]) =>
          `${color}: ${count}`
        ).join(", ")
      );
      return;
    }

    setLoading(true);
    setError("");
    onResult(null);

    const payload = Object.fromEntries(
      Object.entries(faces).map(([face, stickers]) => [
        face,
        [
          stickers.slice(0, 3),
          stickers.slice(3, 6),
          stickers.slice(6, 9),
        ],
      ])
    );

    try {
      const response = await fetch(`${API}/solve-cube`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || "Unable to solve cube.");
      }

      onResult(data);
    } catch (err) {
      setError(
        err.message === "Failed to fetch"
          ? "Cannot connect to the Java backend. Please try again."
          : err.message
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="manual-editor">
      <div className="face-tabs">
        {FACES.map(face => (
          <button
            key={face.id}
            className={
              activeFace === face.id
                ? "face-tab active"
                : "face-tab"
            }
            onClick={() => setActiveFace(face.id)}
          >
            {face.label}
          </button>
        ))}
      </div>

      <div className="editor-heading">
        <h3>
          {FACES.find(face => face.id === activeFace).label} Face
        </h3>
        <span>3 × 3 stickers</span>
      </div>

      <div className="sticker-grid">
        {faces[activeFace].map((color, index) => {
          const hex = COLORS.find(c => c.id === color).hex;

          return (
            <button
              key={index}
              className="sticker"
              style={{ backgroundColor: hex }}
              onClick={() => updateSticker(index)}
              disabled={index === 4}
              aria-label={`Sticker ${index + 1}: ${color}`}
            />
          );
        })}
      </div>

      <p className="editor-label">Choose a sticker color</p>

      <div className="color-palette">
        {COLORS.map(color => (
          <button
            key={color.id}
            className={
              selectedColor === color.id
                ? "palette-color selected"
                : "palette-color"
            }
            onClick={() => setSelectedColor(color.id)}
            title={color.name}
          >
            <span style={{ backgroundColor: color.hex }} />
            {color.name}
          </button>
        ))}
      </div>

      <div className="editor-actions">
        <button
          className="secondary"
          onClick={resetCube}
          disabled={loading}
        >
          Reset cube
        </button>

        <button
          className="primary"
          onClick={solveManualCube}
          disabled={loading}
        >
          {loading ? "Solving..." : "Solve my cube →"}
        </button>
      </div>

      {error && (
        <p className="error" role="alert">{error}</p>
      )}
    </div>
  );
}
