package hu.krizsan.tetris;

import java.awt.Color;

public enum PieceType {
    I(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {2, 0}
    }, Palette.I_PIECE),

    O(new int[][] {
        {0, 0}, {1, 0}, {0, 1}, {1, 1}
    }, Palette.O_PIECE),

    T(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {0, 1}
    }, Palette.T_PIECE),

    S(new int[][] {
        {0, 0}, {1, 0}, {-1, 1}, {0, 1}
    }, Palette.S_PIECE),

    Z(new int[][] {
        {-1, 0}, {0, 0}, {0, 1}, {1, 1}
    }, Palette.Z_PIECE),

    J(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {-1, -1}
    }, Palette.J_PIECE),

    L(new int[][] {
        {-1, 0}, {0, 0}, {1, 0}, {1, -1}
    }, Palette.L_PIECE);

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
