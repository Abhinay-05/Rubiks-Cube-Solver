
import React from "react";
import { Canvas } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";

const COLORS = {
  U: "#f8fafc", // White
  D: "#facc15", // Yellow
  F: "#ef4444", // Red
  B: "#f97316", // Orange
  L: "#22c55e", // Green
  R: "#3b82f6", // Blue
};

function Cubie({ x, y, z }) {
  const materials = [
    COLORS.R, COLORS.L,
    COLORS.U, COLORS.D,
    COLORS.F, COLORS.B,
  ];

  return (
    <mesh position={[x, y, z]}>
      <boxGeometry args={[0.96, 0.96, 0.96]} />
      <meshStandardMaterial
        attach="material-0"
        color={x === 1 ? COLORS.R : "#20232b"}
      />
      <meshStandardMaterial
        attach="material-1"
        color={x === -1 ? COLORS.L : "#20232b"}
      />
      <meshStandardMaterial
        attach="material-2"
        color={y === 1 ? COLORS.U : "#20232b"}
      />
      <meshStandardMaterial
        attach="material-3"
        color={y === -1 ? COLORS.D : "#20232b"}
      />
      <meshStandardMaterial
        attach="material-4"
        color={z === 1 ? COLORS.F : "#20232b"}
      />
      <meshStandardMaterial
        attach="material-5"
        color={z === -1 ? COLORS.B : "#20232b"}
      />
    </mesh>
  );
}

function CubeModel() {
  const cubies = [];

  for (let x = -1; x <= 1; x++) {
    for (let y = -1; y <= 1; y++) {
      for (let z = -1; z <= 1; z++) {
        cubies.push(
          <Cubie
            key={`${x},${y},${z}`}
            x={x}
            y={y}
            z={z}
          />
        );
      }
    }
  }

  return <group>{cubies}</group>;
}

export default function CubeScene() {
  return (
    <div className="cube-canvas">
      <Canvas camera={{ position: [4, 3.5, 5], fov: 40 }}>
        <ambientLight intensity={2} />
        <directionalLight position={[5, 6, 5]} intensity={2} />
        <CubeModel />
        <OrbitControls enablePan={false} />
      </Canvas>
    </div>
  );
}
