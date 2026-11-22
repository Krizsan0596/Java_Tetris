package hu.krizsan.tetris;

import java.awt.Color;

public class Piece {
    public enum RotationState {
        UP, DOWN, LEFT, RIGHT
    }

    private final int[][] offsets;
    private final Color color;
    private final PieceType type;
    private final RotationState rotationState;

    public Piece(PieceType type) {
        this(type.getOffsets(), type, RotationState.DOWN);
    }

    private Piece(int[][] offsets, PieceType type, RotationState rotationState) {
        this.offsets = offsets;
        this.type = type;
        this.color = type.getColor();
        this.rotationState = rotationState;
    }

    public PieceType getType() {
        return type;
    }

    public RotationState getRotationState() {
        return rotationState;
    }

    public Color getColor() {
        return color;
    }

    public int[][] getOffsets() {
        int[][] copy = new int[offsets.length][];
        for (int i = 0; i < offsets.length; i++) {
            copy[i] = offsets[i].clone();
        }
        return copy;
    }

    public Piece cw() {
        int[][] rotated = new int[offsets.length][2];
        for (int i = 0; i < offsets.length; i++) {
            rotated[i][0] = -offsets[i][1];
            rotated[i][1] = offsets[i][0];
        }
        RotationState nextState = switch (rotationState) {
            case UP -> RotationState.RIGHT;
            case RIGHT -> RotationState.DOWN;
            case DOWN -> RotationState.LEFT;
            case LEFT -> RotationState.UP;
        };
        return new Piece(rotated, type, nextState);
    }

    public Piece ccw() {
        int[][] rotated = new int[offsets.length][2];
        for (int i = 0; i < offsets.length; i++) {
            rotated[i][0] = offsets[i][1];
            rotated[i][1] = -offsets[i][0];
        }
        RotationState nextState = switch (rotationState) {
            case UP -> RotationState.LEFT;
            case LEFT -> RotationState.DOWN;
            case DOWN -> RotationState.RIGHT;
            case RIGHT -> RotationState.UP;
        };
        return new Piece(rotated, type, nextState);
    }
}
