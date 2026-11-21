package hu.krizsan.tetris;

import java.awt.Color;

public class Piece {
    private final int[][] offsets;
    private final Color color;

    public Piece(PieceType type) {
        this(type.getOffsets(), type.getColor());
    }

    private Piece(int[][] offsets, Color color) {
        this.offsets = offsets;
        this.color = color;
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
        return new Piece(rotated, color);
    }

    public Piece ccw() {
        int[][] rotated = new int[offsets.length][2];
        for (int i = 0; i < offsets.length; i++) {
            rotated[i][0] = offsets[i][1];
            rotated[i][1] = -offsets[i][0];
        }
        return new Piece(rotated, color);
    }
}
