package hu.krizsan.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GameManager {
    private final PieceBag pieceBag = new PieceBag();
    private Piece nextPiece = new Piece(pieceBag.pop());
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;
    private final Board board;
    private boolean running;
    private int score = 0;
    private final Timer timer;

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel, Board board) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.board = board;
        this.timer = new Timer(calculateFallTime(1), e -> update());

        keybindsSetup();
    }

    private void setTimerDelay(int delay) {
        if (timer.getDelay() == delay) {
            return;
        }
        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        if (timer.isRunning()) {
            timer.restart();
        }
    }

    public void start() {
        timer.setDelay(calculateFallTime(1));
        timer.setInitialDelay(calculateFallTime(1));
        board.reset();
        score = 0;
        pieceBag.reset();
        nextPiece = new Piece(pieceBag.pop());
        running = true;
        newPiece();
    }

    public void update() {
        if (!board.down()) {
            // TODO: Lock delay
            newPiece();
        }
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
        board.lock();
        board.clearRows();
        if (board.spawnPiece(nextPiece)) {
            timer.restart();
            gamePanel.repaint();
            nextPiece = new Piece(pieceBag.pop());
            nextPiecePanel.setPiece(nextPiece);
        } else {
            running = false;
            timer.stop();
            gamePanel.repaint();
        }
    }

    private void keybindsSetup() {
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "speedUp");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "slowDown");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "hardDrop");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "cw");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "ccw");
        gamePanel.getGameActionMap().put("cw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.cw();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("ccw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.ccw();
                gamePanel.repaint();
            }
        });
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
        gamePanel.getGameActionMap().put("speedUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTimerDelay(Math.max(1, calculateFallTime(1) / 20));
            }
        });
        gamePanel.getGameActionMap().put("slowDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTimerDelay(calculateFallTime(1));
            }
        });
        gamePanel.getGameActionMap().put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    return;
                }
                board.hardDrop();
                newPiece();
            }
        });
    }
}
