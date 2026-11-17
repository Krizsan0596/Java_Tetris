package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    public static final Color BACKGROUND_COLOR = new Color(48, 52, 70);

    private final Color[][] colors = new Color[WIDTH][HEIGHT];
    private PieceType currentPiece;
    private final Point position = new Point();

    public Board() {
        for (Color[] column : colors) {
            Arrays.fill(column, BACKGROUND_COLOR);
        }
    }

    public boolean spawnPiece(PieceType piece) {
        int spawnY = 0;
        for (int[] offset : piece.getOffsets()) {
            spawnY = Math.max(spawnY, -offset[1]);
        }
        for (int[] offset : piece.getOffsets()) {
            int x = WIDTH / 2 + offset[0];
            int y = spawnY + offset[1];
            if (!BACKGROUND_COLOR.equals(colors[x][y])) {
                return false;
            }
        }
        currentPiece = piece;
        position.setLocation(WIDTH / 2, spawnY);
        return true;
    }

    public Color[][] renderPiece() {
        Color[][] renderedColors = new Color[WIDTH][];
        for (int x = 0; x < WIDTH; x++) {
            renderedColors[x] = colors[x].clone();
        }

        if (currentPiece == null) {
            return renderedColors;
        }

        for (int[] offset : currentPiece.getOffsets()) {
            int x = position.x + offset[0];
            int y = position.y + offset[1];
            renderedColors[x][y] = currentPiece.getColor();
        }
        return renderedColors;
    }

    public PieceType getCurrentPiece() {
        return currentPiece;
    }
}
