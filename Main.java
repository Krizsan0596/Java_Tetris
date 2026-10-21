import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = new JFrame("High Scores");

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                HighScoreManager hsm = new HighScoreManager();
                JTable table = new JTable(hsm.getTable());

                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                hsm.add(new Score("Potato", 43));
                hsm.add(new Score("Carrot", 20));
                hsm.add(new Score("Cabbage", 33));
            } catch (ClassNotFoundException | IOException | ClassCastException e) {
                e.printStackTrace();
            }
        });
    }
}
