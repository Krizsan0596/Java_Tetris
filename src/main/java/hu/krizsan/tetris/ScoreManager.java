package hu.krizsan.tetris;

public class ScoreManager {
    private static final int[] SCORES = {0, 100, 300, 500, 800};
    private static final int[] ALL_CLEAR_SCORES = {0, 800, 1200, 1800, 2000};

    private int score = 0;
    private int rowsCleared = 0;
    private boolean lastTetris;
    private int combo = -1;

    public void reset() {
        score = 0;
        rowsCleared = 0;
        lastTetris = false;
        combo = -1;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return rowsCleared / 10 + 1;
    }

    public void addSoftDrop() {
        score++;
    }

    public void addHardDrop(int distance) {
        score += 2 * distance;
    }

    public void clearRows(int cleared) {
        clearRows(cleared, false);
    }

    public void clearRows(int cleared, boolean allClear) {
        int level = getLevel();
        if (cleared == 0) {
            combo = -1;
        } else {
            combo++;
            score += level * combo * 50;
        }

        score += level * SCORES[cleared];

        if (cleared == 4 && lastTetris) {
            score += level * SCORES[cleared] / 2;
        }
        if (cleared > 0) {
            if (allClear) {
                int bonus = cleared == 4 && lastTetris ? 3200 : ALL_CLEAR_SCORES[cleared];
                score += level * bonus;
            }
            lastTetris = cleared == 4;
        }

        rowsCleared += cleared;
    }
}
