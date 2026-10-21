import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

public class HighScoreManager {
    private static final int MAX_SCORES = 10;
    private static FileManager fm = new FileManager();
    private HighScoreTable hst = new HighScoreTable(this);
    public LinkedList<Score> scores = new LinkedList<>();

    public HighScoreManager() throws ClassCastException, ClassNotFoundException, IOException {
        scores = fm.load();
    }

    public HighScoreTable getTable() {
        return hst;
    }

    public boolean add(Score score) {
        ListIterator<Score> it = this.scores.listIterator();
        
        while (it.hasNext()) {
            if (score.compareTo(it.next()) > 0) {
                it.previous();
                break;
            }
        }

        if (!it.hasNext() && scores.size() >= MAX_SCORES) {
            return false;
        }

        it.add(score);

        if (scores.size() > MAX_SCORES) {
            scores.removeLast();
        }

        hst.fireTableDataChanged();
        return true;
    }
}
