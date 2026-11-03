import React, {
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";

import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";

const COLORS = {
  U: "#f8fafc",
  D: "#facc15",
  F: "#ef4444",
  B: "#f97316",
  L: "#22c55e",
  R: "#3b82f6",
  INNER: "#20232b",
};

const COLOR_CODE_MAP = {
  W: "U",
  Y: "D",
  R: "F",
  O: "B",
  G: "L",
  B: "R",
};

const FACE_INFO = {
  R: { axis: 0, sign: 1 },
  L: { axis: 0, sign: -1 },
  U: { axis: 1, sign: 1 },
  D: { axis: 1, sign: -1 },
  F: { axis: 2, sign: 1 },
  B: { axis: 2, sign: -1 },
};

const NORMALS = {
  "+x": [1, 0, 0],
  "-x": [-1, 0, 0],
  "+y": [0, 1, 0],
  "-y": [0, -1, 0],
  "+z": [0, 0, 1],
  "-z": [0, 0, -1],
};

function createSolvedCube() {
  const cubies = [];

  for (let x = -1; x <= 1; x++) {
    for (let y = -1; y <= 1; y++) {
      for (let z = -1; z <= 1; z++) {
        const stickers = {};

        if (x === 1) stickers["+x"] = "R";
        if (x === -1) stickers["-x"] = "L";
        if (y === 1) stickers["+y"] = "U";
        if (y === -1) stickers["-y"] = "D";
        if (z === 1) stickers["+z"] = "F";
        if (z === -1) stickers["-z"] = "B";

        cubies.push({
          position: [x, y, z],
          stickers,
        });
      }
    }
  }

  return cubies;
}

function createCubeFromFaces(faces) {
  const cubies = createSolvedCube();

  if (!faces) return cubies;

  const faceDefinitions = {
    up: {
      normal: "+y",
      position: (row, col) => [col - 1, 1, row - 1],
    },
    right: {
      normal: "+x",
      position: (row, col) => [1, 1 - row, 1 - col],
    },
    front: {
      normal: "+z",
      position: (row, col) => [col - 1, 1 - row, 1],
    },
    down: {
      normal: "-y",
      position: (row, col) => [col - 1, -1, 1 - row],
    },
    left: {
      normal: "-x",
      position: (row, col) => [-1, 1 - row, col - 1],
    },
    back: {
      normal: "-z",
      position: (row, col) => [1 - col, 1 - row, -1],
    },
  };

  Object.entries(faceDefinitions).forEach(([faceName, definition]) => {
    const stickers = faces[faceName];

    if (!stickers || stickers.length !== 9) return;

    stickers.forEach((colorCode, index) => {
      const row = Math.floor(index / 3);
      const col = index % 3;
      const position = definition.position(row, col);

      const cubie = cubies.find(item =>
        item.position.every(
          (value, axis) => value === position[axis]
        )
      );

      const faceColor = COLOR_CODE_MAP[colorCode];

      if (cubie && faceColor) {
        cubie.stickers[definition.normal] = faceColor;
      }
    });
  });

  return cubies;
}

function rotateVector(vector, axis, turns) {
  let [x, y, z] = vector;
  const normalizedTurns = ((turns % 4) + 4) % 4;

  for (let i = 0; i < normalizedTurns; i++) {
    if (axis === 0) {
      [x, y, z] = [x, -z, y];
    } else if (axis === 1) {
      [x, y, z] = [z, y, -x];
    } else {
      [x, y, z] = [-y, x, z];
    }
  }

  return [x, y, z];
}

function directionFromNormal(normal) {
  if (normal[0] === 1) return "+x";
  if (normal[0] === -1) return "-x";
  if (normal[1] === 1) return "+y";
  if (normal[1] === -1) return "-y";
  if (normal[2] === 1) return "+z";
  return "-z";
}

function getTurns(move) {
  if (move.includes("2")) return 2;
  if (move.includes("'")) return -1;
  return 1;
}

function inverseMove(move) {
  if (!move) return null;
  if (move.includes("2")) return move;
  if (move.includes("'")) return move[0];
  return move[0] + "'";
}

function applyMove(cubies, move) {
  const info = FACE_INFO[move[0]];
  if (!info) return cubies;

  const turns = -info.sign * getTurns(move);

  return cubies.map(cubie => {
    if (cubie.position[info.axis] !== info.sign) {
      return cubie;
    }

    const position = rotateVector(
      cubie.position,
      info.axis,
      turns
    );

    const stickers = {};

    Object.entries(cubie.stickers).forEach(([direction, color]) => {
      const normal = rotateVector(
        NORMALS[direction],
        info.axis,
        turns
      );

      stickers[directionFromNormal(normal)] = color;
    });

    return { position, stickers };
  });
}

function applyAlgorithm(cubies, algorithm) {
  if (!algorithm?.trim()) return cubies;

  return algorithm
    .trim()
    .split(/\s+/)
    .reduce((state, move) => applyMove(state, move), cubies);
}

function getMaterialColors(stickers) {
  return [
    COLORS[stickers["+x"]] || COLORS.INNER,
    COLORS[stickers["-x"]] || COLORS.INNER,
    COLORS[stickers["+y"]] || COLORS.INNER,
    COLORS[stickers["-y"]] || COLORS.INNER,
    COLORS[stickers["+z"]] || COLORS.INNER,
    COLORS[stickers["-z"]] || COLORS.INNER,
  ];
}

function Cubie({ position, stickers }) {
  const materials = getMaterialColors(stickers);

  return (
    <mesh position={position}>
      <boxGeometry args={[0.96, 0.96, 0.96]} />

      {materials.map((color, index) => (
        <meshStandardMaterial
          key={index}
          attach={`material-${index}`}
          color={color}
          roughness={0.65}
        />
      ))}
    </mesh>
  );
}

function AnimatedCube({
  scramble,
  solution,
  step,
  customFaces,
  onMoveStatusChange,
}) {
  const [cube, setCube] = useState(() =>
    customFaces
      ? createCubeFromFaces(customFaces)
      : applyAlgorithm(createSolvedCube(), scramble)
  );

  const [activeMove, setActiveMove] = useState(null);

  const pivotRef = useRef();
  const cubeRef = useRef(cube);

  const displayedStepRef = useRef(0);
  const targetStepRef = useRef(0);
  const animationRef = useRef(null);

  // Prevent unnecessary repeated progress callbacks.
  const lastReportedProgressRef = useRef(-1);

  // Reset cube when manually entered stickers change.
  useEffect(() => {
    const initialCube = customFaces
      ? createCubeFromFaces(customFaces)
      : applyAlgorithm(createSolvedCube(), scramble);

    cubeRef.current = initialCube;

    displayedStepRef.current = 0;
    targetStepRef.current = 0;

    animationRef.current = null;
    lastReportedProgressRef.current = -1;

    setCube(initialCube);
    setActiveMove(null);

    if (pivotRef.current) {
      pivotRef.current.rotation.set(0, 0, 0);
    }

    onMoveStatusChange?.({
      status: "idle",
      move: null,
      progress: 0,
      step: 0,
    });
  }, [scramble, customFaces, onMoveStatusChange]);

  // Store the requested solution step.
  useEffect(() => {
    targetStepRef.current = Math.max(
      0,
      Math.min(step, solution.length)
    );
  }, [step, solution]);

  useFrame((_, delta) => {
    let animation = animationRef.current;

    // Start the next move if no move is currently animating.
    if (!animation) {
      const currentStep = displayedStepRef.current;
      const targetStep = targetStepRef.current;

      if (currentStep === targetStep) return;

      const movingForward = targetStep > currentStep;

      const move = movingForward
        ? solution[currentStep]
        : inverseMove(solution[currentStep - 1]);

      if (!move) return;

      const info = FACE_INFO[move[0]];
      if (!info) return;

      animation = {
        move,
        targetStep: movingForward
          ? currentStep + 1
          : currentStep - 1,
        movingForward,
        axis: info.axis,
        sign: info.sign,
        angle:
          -info.sign *
          getTurns(move) *
          (Math.PI / 2),
        elapsed: 0,
        duration: 1.75,
      };

      animationRef.current = animation;
      lastReportedProgressRef.current = -1;

      setActiveMove({
        axis: info.axis,
        sign: info.sign,
      });

      onMoveStatusChange?.({
        status: "animating",
        move: animation.move,
        progress: 0,
        step: currentStep,
        targetStep: animation.targetStep,
      });
    }

    if (!pivotRef.current) return;

    animation.elapsed += delta;

    const progress = Math.min(
      animation.elapsed / animation.duration,
      1
    );

    // Report progress in 5% increments.
    const progressPercent = Math.min(
      100,
      Math.floor(progress * 20) * 5
    );

    if (
      progressPercent !== lastReportedProgressRef.current
    ) {
      lastReportedProgressRef.current = progressPercent;

      onMoveStatusChange?.({
        status: "animating",
        move: animation.move,
        progress: progressPercent,
        step: displayedStepRef.current,
        targetStep: animation.targetStep,
      });
    }

    const eased = progress * progress * (3 - 2 * progress);
    const rotation = animation.angle * eased;

    pivotRef.current.rotation.set(0, 0, 0);

    if (animation.axis === 0) {
      pivotRef.current.rotation.x = rotation;
    } else if (animation.axis === 1) {
      pivotRef.current.rotation.y = rotation;
    } else {
      pivotRef.current.rotation.z = rotation;
    }

    // Commit the move after animation finishes.
    if (progress >= 1) {
      const updatedCube = applyMove(
        cubeRef.current,
        animation.move
      );

      cubeRef.current = updatedCube;
      displayedStepRef.current = animation.targetStep;

      animationRef.current = null;
      pivotRef.current.rotation.set(0, 0, 0);

      setCube(updatedCube);
      setActiveMove(null);

      const reachedEnd =
        animation.movingForward &&
        animation.targetStep === solution.length;

      onMoveStatusChange?.({
        status: reachedEnd ? "finished" : "completed",
        move: animation.move,
        progress: 100,
        step: animation.targetStep,
        targetStep: animation.targetStep,
      });
    }
  });

  const stationaryCubies = useMemo(
    () =>
      cube.filter(
        cubie =>
          !activeMove ||
          cubie.position[activeMove.axis] !== activeMove.sign
      ),
    [cube, activeMove]
  );

  const rotatingCubies = useMemo(
    () =>
      cube.filter(
        cubie =>
          activeMove &&
          cubie.position[activeMove.axis] === activeMove.sign
      ),
    [cube, activeMove]
  );

  return (
    <group>
      {stationaryCubies.map(cubie => (
        <Cubie
          key={cubie.position.join(",")}
          position={cubie.position}
          stickers={cubie.stickers}
        />
      ))}

      <group ref={pivotRef}>
        {rotatingCubies.map(cubie => (
          <Cubie
            key={cubie.position.join(",")}
            position={cubie.position}
            stickers={cubie.stickers}
          />
        ))}
      </group>
    </group>
  );
}

export default function CubeScene({
  scramble = "",
  solution = [],
  step = 0,
  customFaces = null,
  onMoveStatusChange,
}) {
  return (
    <div className="cube-canvas">
      <Canvas camera={{ position: [4, 3.5, 5], fov: 40 }}>
        <ambientLight intensity={2} />

        <directionalLight
          position={[5, 6, 5]}
          intensity={2}
        />

        <AnimatedCube
          scramble={scramble}
          solution={solution}
          step={step}
          customFaces={customFaces}
          onMoveStatusChange={onMoveStatusChange}
        />

        <OrbitControls enablePan={false} />
      </Canvas>
    </div>
  );
}
