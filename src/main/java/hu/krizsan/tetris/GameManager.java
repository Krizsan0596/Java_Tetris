package hu.krizsan.tetris;

import java.awt.Color;
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

    private final Board board;
    private PiecePanel nextPiecePanel;
    private PiecePanel heldPiecePanel;
    private GamePanel gamePanel;
    private final JLabel scoreLabel;
    private final HighScoreManager highScoreManager;

    private final Timer timer;
    private final Timer lockTimer;

    private final PieceBag pieceBag = new PieceBag();
    private Piece nextPiece;
    private Piece holdPiece = null;

    private boolean running;
    private boolean softDropping;
    private boolean holdAvailable = true;
    private int resets = 0;

    private final ScoreManager scoreManager = new ScoreManager();

    public GameManager(PiecePanel nextPiecePanel, PiecePanel heldPiecePanel, GamePanel gamePanel, Board board, JLabel scoreLabel, HighScoreManager highScoreManager) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.heldPiecePanel = heldPiecePanel;
        this.board = board;
        this.scoreLabel = scoreLabel;
        this.highScoreManager = highScoreManager;

        this.timer = new Timer(calculateFallTime(1), e -> update());
        this.lockTimer = new Timer(LOCK_DELAY, e -> newPiece());
        this.lockTimer.setRepeats(false);

        keybindsSetup();
    }

    public void start() {
        timer.stop();
        lockTimer.stop();
        timer.setDelay(calculateFallTime(1));
        timer.setInitialDelay(calculateFallTime(1));
        board.reset();
        scoreManager.reset();
        updateScoreLabel();
        softDropping = false;
        holdPiece = null;
        heldPiecePanel.setPiece(null);
        setHoldAvailable(true);
        pieceBag.reset();

        nextPiece = new Piece(pieceBag.pop());
        running = true;
        newPiece();
    }

    public void update() {
        if (!running) {
            return;
        }
        if (!board.hasCollision(0, 1)) {
            board.down();
            if (softDropping) {
                scoreManager.addSoftDrop();
            }
        } else {
            lockTimer.start();
            timer.stop();
        }
        gamePanel.repaint();
        updateScoreLabel();
    }

    public void holdPiece() {
        if (!running || !holdAvailable) {
            return;
        }

        boolean emptyHold = holdPiece == null;
        Piece pieceToSpawn = emptyHold ? nextPiece : holdPiece;
        holdPiece = board.getCurrentPiece();
        heldPiecePanel.setPiece(holdPiece);

        if (!board.spawnPiece(pieceToSpawn)) {
            gameOver();
            return;
        }

        setHoldAvailable(false);

        if (emptyHold) {
            nextPiece = new Piece(pieceBag.pop());
            nextPiecePanel.setPiece(nextPiece);
        }

        lockTimer.stop();
        resets = 0;
        updateTimer();
        gamePanel.repaint();
    }

    public boolean isRunning() {
        return running;
    }

    public void withTimersPaused(Runnable action) {
        boolean gravityWasRunning = timer.isRunning();
        boolean lockWasRunning = lockTimer.isRunning();
        timer.stop();
        lockTimer.stop();
        try {
            action.run();
        } finally {
            if (running) {
                if (gravityWasRunning) {
                    timer.start();
                }
                if (lockWasRunning) {
                    lockTimer.start();
                }
            }
        }
    }

    public int calculateFallTime(int level) {
        double seconds = Math.pow(0.8 - ((level - 1) * 0.007), level - 1);
        return (int) Math.round(seconds * 1000);
    }

    public void updateTimer() {
        if (!running) {
            return;
        }
        int delay = calculateTimerDelay();
        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        timer.restart();
    }

    public void newPiece() {
        if (!running) {
            return;
        }
        lockTimer.stop();
        resets = 0;
        board.lock();

        scoreManager.clearRows(board.clearRows());
        updateScoreLabel();

        if (!holdAvailable) {
            setHoldAvailable(true);
        }

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
                    "Game over!\n Score: " + scoreManager.getScore() + "\nEnter your name to save your score:",
                    "Game Over",
                    JOptionPane.QUESTION_MESSAGE
                    );
            if (name == null) {
                return;
            }
        }

        Score highScore = new Score(name, scoreManager.getScore());
        highScoreManager.add(highScore);
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

    private void setHoldAvailable(boolean available) {
        holdAvailable = available;
        heldPiecePanel.setLabelColor(available ? Color.WHITE : Color.RED);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + scoreManager.getScore());
    }

    private void resumeFallingIfUnsupported() {
        if (lockTimer.isRunning() && !board.hasCollision(0, 1)) {
            lockTimer.stop();
            timer.start();
        }
    }

    private int calculateTimerDelay() {
        int delay = calculateFallTime(scoreManager.getLevel());
        return softDropping ? Math.max(1, delay / 20) : delay;
    }

    private void keybindsSetup() {
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "speedUp");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "slowDown");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "hardDrop");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "cw");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "ccw");
        gamePanel.getGameInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "hold");

        gamePanel.getGameActionMap().put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                holdPiece();
            }
        });

        gamePanel.getGameActionMap().put("cw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    return;
                }
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
                if (!running) {
                    return;
                }
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
                if (!running) {
                    return;
                }
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
                if (!running) {
                    return;
                }
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
                if (!running) {
                    return;
                }
                softDropping = true;
                setTimerDelay(calculateTimerDelay());
            }
        });
        gamePanel.getGameActionMap().put("slowDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    return;
                }
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
                scoreManager.addHardDrop(board.hardDrop());
                newPiece();
            }
        });
    }
}
