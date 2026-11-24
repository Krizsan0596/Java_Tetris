package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import hu.krizsan.tetris.Piece.RotationState;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    private static final double TRANSPARENCY = 0.7;
    private static final Map<PieceType, Map<RotationChange, Point[]>> wallkicks = new EnumMap<>(PieceType.class);

    private final Color[][] colors = new Color[HEIGHT][WIDTH];

    private Piece currentPiece;
    private final Point position = new Point();

    public Board() {
        setupWallkicks();
        reset();
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public static Color alphaBlend(Color color, double alpha) {
        if (!Double.isFinite(alpha) || alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha must be between 0 and 1.");
        }

        int red = (int) Math.round(color.getRed() * alpha + Palette.BACKGROUND.getRed() * (1 - alpha));
        int green = (int) Math.round(color.getGreen() * alpha + Palette.BACKGROUND.getGreen() * (1 - alpha));
        int blue = (int) Math.round(color.getBlue() * alpha + Palette.BACKGROUND.getBlue() * (1 - alpha));
        return new Color(red, green, blue);
    }

    public void reset() {
        for (Color[] row : colors) {
            Arrays.fill(row, Palette.BACKGROUND);
        }
        currentPiece = null;
        position.setLocation(0, 0);
    }

    public void lock() {
        if (currentPiece != null) {
            for (int[] offset : currentPiece.getOffsets()) {
                int x = position.x + offset[0];
                int y = position.y + offset[1];
                colors[y][x] = currentPiece.getColor();
            }
            currentPiece = null;
        }
    }

    public int clearRows() {
        int writeRow = colors.length - 1;
        int clearedRows = 0;

        for (int readRow = colors.length - 1; readRow >= 0; readRow--) {
            if (rowClear(readRow)) {
                clearedRows++;
            } else {
                System.arraycopy(colors[readRow], 0, colors[writeRow], 0, WIDTH);
                writeRow--;
            }
        }

        for (int i = 0; i <= writeRow; i++) {
            Arrays.fill(colors[i], Palette.BACKGROUND);
        }

        return clearedRows;
    }

    public boolean checkAllClear() {
        for (Color[] row : colors) {
            for (Color cell : row) {
                if (!Palette.BACKGROUND.equals(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean spawnPiece(Piece piece) {
        int spawnY = 0;
        for (int[] offset : piece.getOffsets()) {
            spawnY = Math.max(spawnY, -offset[1]);
        }
        for (int[] offset : piece.getOffsets()) {
            int x = WIDTH / 2 + offset[0];
            int y = spawnY + offset[1];
            if (!Palette.BACKGROUND.equals(colors[y][x])) {
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

    public boolean hasCollision(int dx, int dy) {
        return hasCollision(currentPiece, dx, dy);
    }

    public boolean hasCollision(Point offset) {
        return hasCollision(offset.x, offset.y);
    }

    public int hardDrop() {
        int distance = calculateDropDistance();
        shift(0, distance);
        return distance;
    }

    private int calculateDropDistance() {
        int distance = 0;

        while (!hasCollision(0, distance + 1)) {
            distance++;
        }
        return distance;
    }

    public Color[][] renderPiece() {
        Color[][] renderedColors = new Color[HEIGHT][];
        for (int y = 0; y < HEIGHT; y++) {
            renderedColors[y] = colors[y].clone();
        }

        if (currentPiece == null) {
            return renderedColors;
        }

        renderGhostPiece(renderedColors);

        for (int[] offset : currentPiece.getOffsets()) {
            int x = position.x + offset[0];
            int y = position.y + offset[1];
            renderedColors[y][x] = currentPiece.getColor();
        }
        return renderedColors;
    }

    private void renderGhostPiece(Color[][] renderedColors) {
        int distance = calculateDropDistance();
        Color ghostColor = alphaBlend(currentPiece.getColor(), 1 - TRANSPARENCY);

        for (int[] offset : currentPiece.getOffsets()) {
            int x = position.x + offset[0];
            int y = position.y + offset[1] + distance;
            renderedColors[y][x] = ghostColor;
        }
    }

    private boolean rowClear(int index) {
        for (Color block : colors[index]) {
            if (Palette.BACKGROUND.equals(block)) {
                return false;
            }
        }
        return true;
    }

    private boolean rotate(Piece rotated) {
        RotationChange change = new RotationChange(currentPiece.getRotationState(), rotated.getRotationState());
        for (Point offset : wallkicks.get(currentPiece.getType()).get(change)) {
            if (!hasCollision(rotated, offset.x, -offset.y)) {
                position.translate(offset.x, -offset.y);
                currentPiece = rotated;
                return true;
            }
        }
        return false;
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

    private boolean hasCollision(Piece piece, Point offset) {
        return hasCollision(piece, offset.x, offset.y);
    }

    private boolean hasCollision(Piece piece, int dx, int dy) {
        for (int[] offset : piece.getOffsets()) {
            int x = position.x + offset[0] + dx;
            int y = position.y + offset[1] + dy;
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT
                    || !Palette.BACKGROUND.equals(colors[y][x])) {
                return true;
            }
        }

        return false;
    }

    private void setupWallkicks() {
        Map<RotationChange, Point[]> standardKicks = Map.of(
            new RotationChange(RotationState.DOWN, RotationState.RIGHT),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(-1, -2)},
            new RotationChange(RotationState.RIGHT, RotationState.DOWN),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 2), new Point(1, 2)},
            new RotationChange(RotationState.RIGHT, RotationState.UP),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 2), new Point(1, 2)},
            new RotationChange(RotationState.UP, RotationState.RIGHT),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(-1, -2)},
            new RotationChange(RotationState.UP, RotationState.LEFT),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, -2), new Point(1, -2)},
            new RotationChange(RotationState.LEFT, RotationState.UP),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, 2), new Point(-1, 2)},
            new RotationChange(RotationState.LEFT, RotationState.DOWN),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, 2), new Point(-1, 2)},
            new RotationChange(RotationState.DOWN, RotationState.LEFT),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, -2), new Point(1, -2)}
        );
        for (PieceType type : new PieceType[] {PieceType.J, PieceType.L, PieceType.S, PieceType.T, PieceType.Z}) {
            wallkicks.put(type, standardKicks);
        }

        wallkicks.put(PieceType.O, Map.of(
            new RotationChange(RotationState.DOWN, RotationState.RIGHT),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.RIGHT, RotationState.DOWN),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.RIGHT, RotationState.UP),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.UP, RotationState.RIGHT),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.UP, RotationState.LEFT),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.LEFT, RotationState.UP),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.LEFT, RotationState.DOWN),
            new Point[] {new Point(0, 0)},
            new RotationChange(RotationState.DOWN, RotationState.LEFT),
            new Point[] {new Point(0, 0)}
        ));

        wallkicks.put(PieceType.I, Map.of(
            new RotationChange(RotationState.DOWN, RotationState.RIGHT),
            new Point[] {new Point(0, 0), new Point(-2, 0), new Point(1, 0), new Point(-2, -1), new Point(1, 2)},
            new RotationChange(RotationState.RIGHT, RotationState.DOWN),
            new Point[] {new Point(0, 0), new Point(2, 0), new Point(-1, 0), new Point(2, 1), new Point(-1, -2)},
            new RotationChange(RotationState.RIGHT, RotationState.UP),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(2, 0), new Point(-1, 2), new Point(2, -1)},
            new RotationChange(RotationState.UP, RotationState.RIGHT),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(-2, 0), new Point(1, -2), new Point(-2, 1)},
            new RotationChange(RotationState.UP, RotationState.LEFT),
            new Point[] {new Point(0, 0), new Point(2, 0), new Point(-1, 0), new Point(2, 1), new Point(-1, -2)},
            new RotationChange(RotationState.LEFT, RotationState.UP),
            new Point[] {new Point(0, 0), new Point(-2, 0), new Point(1, 0), new Point(-2, -1), new Point(1, 2)},
            new RotationChange(RotationState.LEFT, RotationState.DOWN),
            new Point[] {new Point(0, 0), new Point(1, 0), new Point(-2, 0), new Point(1, -2), new Point(-2, 1)},
            new RotationChange(RotationState.DOWN, RotationState.LEFT),
            new Point[] {new Point(0, 0), new Point(-1, 0), new Point(2, 0), new Point(-1, 2), new Point(2, -1)}
        ));
    }
}
