
import { useState, useEffect } from "react";
import "./CubeInput.css";

// Backend configuration
const API_BASE_URL = "https://cubelab-backend.onrender.com";
const API = `${API_BASE_URL}/api`;

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

  // Synchronize manual input with the 3D cube.
  useEffect(() => {
    onFacesChange?.(faces);
  }, [faces, onFacesChange]);

  // Update an individual sticker.
  function updateSticker(index) {
    // Center stickers cannot be changed.
    if (index === 4) return;

    setFaces(previous => ({
      ...previous,
      [activeFace]: previous[activeFace].map(
        (color, i) => i === index ? selectedColor : color
      ),
    }));

    setError("");
    onResult?.(null);
  }

  // Reset all six faces.
  function resetCube() {
    setFaces(copySolvedCube());
    setError("");
    onResult?.(null);
  }

  // Count the occurrence of each color.
  function getColorCount() {
    const counts = Object.fromEntries(
      COLORS.map(color => [color.id, 0])
    );

    Object.values(faces).flat().forEach(color => {
      counts[color]++;
    });

    return counts;
  }

  // Send the cube configuration to Spring Boot.
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
    onResult?.(null);

    // Convert each face into a 3x3 matrix.
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
          "Accept": "application/json",
        },
        body: JSON.stringify(payload),
      });

      // Handle both JSON and plain-text backend responses.
      const responseText = await response.text();

      let data;

      try {
        data = responseText ? JSON.parse(responseText) : {};
      } catch {
        data = { error: responseText };
      }

      if (!response.ok) {
        throw new Error(
          data.error ||
          data.message ||
          `Backend error: ${response.status}`
        );
      }

      onResult?.(data);

    } catch (err) {
      console.error("Cube solving API error:", err);

      if (err.name === "TypeError") {
        setError(
          "Unable to reach the backend. Check the backend status and CORS configuration."
        );
      } else {
        setError(err.message || "Unable to solve cube.");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="manual-editor">

      {/* Face selection */}
      <div className="face-tabs">
        {FACES.map(face => (
          <button
            key={face.id}
            type="button"
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

      {/* Active face heading */}
      <div className="editor-heading">
        <h3>
          {FACES.find(face => face.id === activeFace).label} Face
        </h3>
        <span>3 × 3 stickers</span>
      </div>

      {/* 3x3 sticker grid */}
      <div className="sticker-grid">
        {faces[activeFace].map((color, index) => {
          const hex = COLORS.find(
            c => c.id === color
          ).hex;

          return (
            <button
              key={index}
              type="button"
              className="sticker"
              style={{ backgroundColor: hex }}
              onClick={() => updateSticker(index)}
              disabled={index === 4 || loading}
              aria-label={`Sticker ${index + 1}: ${color}`}
            />
          );
        })}
      </div>

      {/* Color palette */}
      <p className="editor-label">
        Choose a sticker color
      </p>

      <div className="color-palette">
        {COLORS.map(color => (
          <button
            key={color.id}
            type="button"
            className={
              selectedColor === color.id
                ? "palette-color selected"
                : "palette-color"
            }
            onClick={() => setSelectedColor(color.id)}
            title={color.name}
            disabled={loading}
          >
            <span
              style={{ backgroundColor: color.hex }}
            />
            {color.name}
          </button>
        ))}
      </div>

      {/* Actions */}
      <div className="editor-actions">
        <button
          type="button"
          className="secondary"
          onClick={resetCube}
          disabled={loading}
        >
          Reset cube
        </button>

        <button
          type="button"
          className="primary"
          onClick={solveManualCube}
          disabled={loading}
        >
          {loading ? "Solving..." : "Solve my cube →"}
        </button>
      </div>

      {/* Error message */}
      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}

    </div>
  );
}
