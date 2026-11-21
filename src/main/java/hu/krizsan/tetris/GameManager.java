package hu.krizsan.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GameManager {
    private final int LOCK_DELAY = 500;
    private final int MAX_LOCK_RESETS = 15;
    private final PieceBag pieceBag = new PieceBag();
    private Piece nextPiece = new Piece(pieceBag.pop());
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;
    private final Board board;
    private final JLabel scoreLabel;
    private final HighScoreManager highScoreManager;
    private boolean running;
    private boolean softDropping;
    private boolean lastTetris;
    private final Timer timer;
    private int rows_cleared = 0;
    private int combo = -1;
    private int score = 0;
    private final Timer lockTimer;
    private int resets = 0;

    private final int[] scores = {0, 100, 300, 500, 800}; // Clearing >4 lines should be impossible.

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel, Board board, JLabel scoreLabel, HighScoreManager highScoreManager) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.board = board;
        this.scoreLabel = scoreLabel;
        this.highScoreManager = highScoreManager;
        this.timer = new Timer(calculateFallTime(1), e -> update());
        this.lockTimer = new Timer(LOCK_DELAY, e -> newPiece());
        this.lockTimer.setRepeats(false);

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
        lockTimer.stop();
        timer.setDelay(calculateFallTime(1));
        timer.setInitialDelay(calculateFallTime(1));
        board.reset();
        score = 0;
        rows_cleared = 0;
        lastTetris = false;
        updateScoreLabel();
        softDropping = false;
        pieceBag.reset();
        nextPiece = new Piece(pieceBag.pop());
        running = true;
        newPiece();
    }

    public void update() {
        if (!board.hasCollision(0, 1)) {
            board.down();
            if (softDropping) {
                score++;
            }
        } else {
            lockTimer.start();
            timer.stop();
        }
        gamePanel.repaint();
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    private void resumeFallingIfUnsupported() {
        if (lockTimer.isRunning() && !board.hasCollision(0, 1)) {
            lockTimer.stop();
            timer.start();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int calculateFallTime(int level) {
        double seconds = Math.pow(0.8 - ((level - 1) * 0.007), level - 1);
        return (int) Math.round(seconds * 1000);
    }

    private int calculateTimerDelay() {
        int delay = calculateFallTime(rows_cleared / 10 + 1);
        return softDropping ? Math.max(1, delay / 20) : delay;
    }

    public void updateTimer() {
        int delay = calculateTimerDelay();
        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        timer.restart();
    }

    public void newPiece() {
        lockTimer.stop();
        resets = 0;
        board.lock();
        int cleared = board.clearRows();
        if (cleared == 0) {
            combo = -1;
        } else {
            combo++;
            score += (rows_cleared / 10 + 1) * combo * 50;
        }
        score += (rows_cleared / 10 + 1) * scores[cleared];
        if (cleared == 4 && lastTetris) {
            score += (rows_cleared / 10 + 1) * scores[cleared] / 2;
        }
        if (cleared > 0) {
            lastTetris = cleared == 4;
        }
        updateScoreLabel();
        rows_cleared += cleared;
        if (board.spawnPiece(nextPiece)) {
            updateTimer();

            gamePanel.repaint();
            nextPiece = new Piece(pieceBag.pop());
            nextPiecePanel.setPiece(nextPiece);
        } else {
            gameOver();
        }
    }

    public void gameOver() {
        running = false;
        timer.stop();
        lockTimer.stop();
        gamePanel.repaint();

        String name = "";
        while (name.isEmpty()) {
            name = JOptionPane.showInputDialog(
                    gamePanel,
                    "Game over!\n Score: " + score + "\nEnter your name to save your score:",
                    "Game Over",
                    JOptionPane.QUESTION_MESSAGE
                    );
            if (name == null) {
                return;
            }
        }

        Score highScore = new Score(name, score);
        highScoreManager.add(highScore);
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
                if (lockTimer.isRunning() && resets >= MAX_LOCK_RESETS) {
                    return;
                }
                if (!board.cw()) {
                    return;
                }
                if (lockTimer.isRunning()) {
                    resets++;
                    lockTimer.restart();
                }
                resumeFallingIfUnsupported();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("ccw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lockTimer.isRunning() && resets >= MAX_LOCK_RESETS) {
                    return;
                }
                if (!board.ccw()) {
                    return;
                }
                if (lockTimer.isRunning()) {
                    resets++;
                    lockTimer.restart();
                }
                resumeFallingIfUnsupported();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lockTimer.isRunning() && resets >= MAX_LOCK_RESETS) {
                    return;
                }
                if (!board.left()) {
                    return;
                }
                if (lockTimer.isRunning()) {
                    resets++;
                    lockTimer.restart();
                }
                resumeFallingIfUnsupported();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lockTimer.isRunning() && resets >= MAX_LOCK_RESETS) {
                    return;
                }
                if (!board.right()) {
                    return;
                }
                if (lockTimer.isRunning()) {
                    resets++;
                    lockTimer.restart();
                }
                resumeFallingIfUnsupported();
                gamePanel.repaint();
            }
        });
        gamePanel.getGameActionMap().put("speedUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                softDropping = true;
                setTimerDelay(calculateTimerDelay());
            }
        });
        gamePanel.getGameActionMap().put("slowDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                softDropping = false;
                setTimerDelay(calculateTimerDelay());
            }
        });
        gamePanel.getGameActionMap().put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    return;
                }
                score += 2 * board.hardDrop();
                newPiece();
            }
        });
    }
}
