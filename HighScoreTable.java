import java.time.LocalDateTime;

import javax.swing.table.AbstractTableModel;

public class HighScoreTable extends AbstractTableModel {
    private final String[] columns = { "Name", "Score", "Date" };
    private HighScoreManager hsm;

    @Override 
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return hsm.scores.size();
    }

    @Override 
    public Object getValueAt(int row, int column) {
        Score item = hsm.scores.get(row);
        switch (column) {
            case 0:
                return item.getName();
            case 1:
                return item.getScore();
            case 2:
                return item.getTime();
            default:
                throw new IllegalStateException("No such row: " + column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return LocalDateTime.class;
            default:
                return Object.class;
        }
    }
}
