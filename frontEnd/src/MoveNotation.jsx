import React from "react";

const NOTATION = [
  { move: "R", face: "Right", action: "Clockwise quarter-turn" },
  { move: "L", face: "Left", action: "Clockwise quarter-turn" },
  { move: "U", face: "Up", action: "Clockwise quarter-turn" },
  { move: "D", face: "Down", action: "Clockwise quarter-turn" },
  { move: "F", face: "Front", action: "Clockwise quarter-turn" },
  { move: "B", face: "Back", action: "Clockwise quarter-turn" },
];

export default function MoveNotation() {
  return (
    <section className="notation-guide" aria-labelledby="notation-title">
      <div className="notation-guide-heading">
        <div>
          <p className="eyebrow">QUICK REFERENCE</p>
          <h3 id="notation-title">Move notation guide</h3>
        </div>

        <span className="notation-guide-caption">
          Standard Singmaster notation
        </span>
      </div>

      <div className="notation-grid">
        {NOTATION.map(({ move, face, action }) => (
          <div className="notation-item" key={move}>
            <span className="notation-symbol">{move}</span>
            <span className="notation-face">{face} face</span>
            <span className="notation-action">{action}</span>
          </div>
        ))}
      </div>

      <div className="notation-modifiers">
        <div className="notation-modifier">
          <code>R&apos;</code>
          <span>Prime: anticlockwise quarter-turn</span>
        </div>

        <div className="notation-modifier">
          <code>R2</code>
          <span>2: half-turn (180°)</span>
        </div>
      </div>

      <p className="notation-footnote">
        Clockwise and anticlockwise are judged while looking directly
        at the face being turned.
      </p>
    </section>
  );
}