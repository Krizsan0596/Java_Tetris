package hu.krizsan.tetris;

import java.awt.Color;

public enum PieceType {
    I(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {2, 0}
    }, Color.CYAN),

    O(new int[][] {
        {0, 0}, {1, 0}, {0, 1}, {1, 1}
    }, Color.YELLOW),

    T(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {0, 1}
    }, Color.MAGENTA),

    S(new int[][] {
        {0, 0}, {1, 0}, {-1, 1}, {0, 1}
    }, Color.GREEN),

    Z(new int[][] {
        {-1, 0}, {0, 0}, {0, 1}, {1, 1}
    }, Color.RED),

    J(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {-1, -1}
    }, Color.BLUE),

    L(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {1, -1}
    }, Color.ORANGE);

    private final int[][] offsets;
    private final Color color;

    PieceType(int[][] offsets, Color color) {
        this.offsets = offsets;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int[][] getOffsets() {
        int[][] copy = new int[offsets.length][2];

        for (int i = 0; i < offsets.length; i++) {
            copy[i] = offsets[i].clone();
        }

        return copy;
    }
}
