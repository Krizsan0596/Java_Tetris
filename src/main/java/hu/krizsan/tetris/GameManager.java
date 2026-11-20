package hu.krizsan.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GameManager {
    private final PieceBag pieceBag = new PieceBag();
    private PieceType nextPiece = pieceBag.pop();
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;
    private final Board board;
    private boolean running;
    private final Timer timer;

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel, Board board) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.board = board;
        this.timer = new Timer(calculateFallTime(1), e -> update());

        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        gamePanel.getGameActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.left();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.right();
                gamePanel.repaint();
            }
        });
    }

    public void start() {
        timer.setDelay(calculateFallTime(1));
        timer.setInitialDelay(calculateFallTime(1));
        board.reset();
        pieceBag.reset();
        nextPiece = pieceBag.pop();
        running = true;
        newPiece();
    }

    public void update() {
        board.down();
        gamePanel.repaint();
    }

    public boolean isRunning() {
        return running;
    }

    public int calculateFallTime(int level) {
        double seconds = Math.pow(0.8 - ((level - 1) * 0.007), level - 1);
        return (int) Math.round(seconds * 1000);
    }

    public void newPiece() {
        if (board.spawnPiece(nextPiece)) {
            timer.restart();
            gamePanel.repaint();
            nextPiece = pieceBag.pop();
            nextPiecePanel.setPiece(nextPiece);
        } else {
            running = false;
        }
    }
}
