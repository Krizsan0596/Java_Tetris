package hu.krizsan.tetris;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Score implements Comparable<Score>, Serializable{
    private final String name;
    private final int score;
    private final LocalDateTime time;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
        this.time = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override 
    public int compareTo(Score other) {
        return Integer.compare(this.score, other.score);
    }
}
