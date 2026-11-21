package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    public static final Color BACKGROUND_COLOR = new Color(48, 52, 70);

    private final Color[][] colors = new Color[WIDTH][HEIGHT];
    private Piece currentPiece;
    private final Point position = new Point();

    public Board() {
        reset();
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void reset() {
        for (Color[] column : colors) {
            Arrays.fill(column, BACKGROUND_COLOR);
        }
        currentPiece = null;
        position.setLocation(0, 0);
    }

    public boolean spawnPiece(Piece piece) {
        if (currentPiece != null) {
            for (int[] offset : currentPiece.getOffsets()) {
                int x = position.x + offset[0];
                int y = position.y + offset[1];
                colors[x][y] = currentPiece.getColor();
            }
            currentPiece = null;
        }

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

    public boolean left() {
        return shift(-1, 0);
    }

    public boolean right() {
        return shift(1, 0);
    }

    public boolean down() {
        return shift(0, 1);
    }

    public boolean cw() {
        if (currentPiece == null) {
            return false;
        }
        return rotate(currentPiece.cw());
    }

    public boolean ccw() {
        if (currentPiece == null) {
            return false;
        }
        return rotate(currentPiece.ccw());
    }

    private boolean rotate(Piece rotated) {
        if (hasCollision(rotated, 0, 0)) {
            return false;
        }
        currentPiece = rotated;
        return true;
    }

    private boolean shift(int dx, int dy) {
        if (currentPiece == null) {
            return false;
        }

        if (hasCollision(dx, dy)) {
            return false;
        }

        position.translate(dx, dy);
        return true;
    }

    public boolean hasCollision(int dx, int dy) {
        return hasCollision(currentPiece, dx, dy);
    }

    private boolean hasCollision(Piece piece, int dx, int dy) {
        for (int[] offset : piece.getOffsets()) {
            int x = position.x + offset[0] + dx;
            int y = position.y + offset[1] + dy;
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT
                    || !BACKGROUND_COLOR.equals(colors[x][y])) {
                return true;
            }
        }

        return false;
    }

    public void hardDrop() {
        int distance = 0;

        while (!hasCollision(0, distance + 1)) {
            distance++;
        }
        shift(0, distance);
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
}
