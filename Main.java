import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class Main {
    public static void createWindow() {
            try {
                JFrame frame = new JFrame("Tetris");

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout(8, 8));

                // Main game display, to be owned by GameManager object
                GamePanel gamePanel = new GamePanel();
                frame.add(gamePanel, BorderLayout.CENTER);
                
                JPanel sidePanel = new JPanel(new BorderLayout(5, 5));
                sidePanel.setPreferredSize(new Dimension(170, 0));

                // Next piece display, to be owned by GameManager object
                NextPiecePanel nextPiecePanel = new NextPiecePanel();
                sidePanel.add(nextPiecePanel, BorderLayout.CENTER);

                JButton quitButton = new JButton("Quit");
                quitButton.addActionListener(event -> 
                        frame.dispatchEvent(
                            new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
                        )
                );

                sidePanel.add(quitButton, BorderLayout.SOUTH);
                frame.add(sidePanel, BorderLayout.EAST);

                // HighScoreTable
                HighScoreManager hsm = new HighScoreManager();
                JTable table = new JTable(hsm.getTable());

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(0, 150));
                frame.add(scrollPane, BorderLayout.SOUTH);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                hsm.add(new Score("Potato", 43));
                hsm.add(new Score("Carrot", 20));
                hsm.add(new Score("Cabbage", 33));
            } catch (ClassNotFoundException | IOException | ClassCastException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }
}
