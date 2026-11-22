package hu.krizsan.tetris;

import hu.krizsan.tetris.Piece.RotationState;

public record RotationChange(RotationState before, RotationState after) {
}
