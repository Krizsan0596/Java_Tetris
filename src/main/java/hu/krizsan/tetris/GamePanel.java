package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;

public final class GamePanel extends JPanel {
    private static final int BLOCK_SIZE = 25;

    private final Board board;
    private final InputMap gameInputMap;
    private final ActionMap gameActionMap;

    public GamePanel(Board board) {
        this.board = board;
        gameInputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        gameActionMap = getActionMap();
        setPreferredSize(new Dimension(BLOCK_SIZE * Board.WIDTH, BLOCK_SIZE * Board.HEIGHT));
        setBackground(Board.BACKGROUND_COLOR);
    }

    public Board getBoard() {
        return board;
    }

    public InputMap getGameInputMap() {
        return gameInputMap;
    }

    public ActionMap getGameActionMap() {
        return gameActionMap;
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color[][] colors = board.renderPiece();
        for (int x = 0; x < Board.WIDTH; x++) {
            for (int y = 0; y < Board.HEIGHT; y++) {
                g.setColor(colors[x][y]);
                g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }
}
