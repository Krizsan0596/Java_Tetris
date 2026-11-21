package hu.krizsan.tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
                Board board = new Board();
                GamePanel gamePanel = new GamePanel(board);
                JLabel scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
                JPanel gameContainer = new JPanel(new BorderLayout(0, 8));
                gameContainer.add(scoreLabel, BorderLayout.NORTH);
                gameContainer.add(gamePanel, BorderLayout.CENTER);
                frame.add(gameContainer, BorderLayout.CENTER);
                
                JPanel sidePanel = new JPanel(new BorderLayout(5, 8));
                sidePanel.setPreferredSize(new Dimension(170, 0));

                // Next piece display, to be owned by GameManager object
                JLabel nextPieceLabel = new JLabel("Next piece", JLabel.CENTER);
                sidePanel.add(nextPieceLabel, BorderLayout.NORTH);
                NextPiecePanel nextPiecePanel = new NextPiecePanel();
                sidePanel.add(nextPiecePanel, BorderLayout.CENTER);

                HighScoreManager hsm = new HighScoreManager();
                GameManager gameManager = new GameManager(nextPiecePanel, gamePanel, board, scoreLabel, hsm);
                gameManager.start();

                JButton quitButton = new JButton("Quit");
                quitButton.setFocusable(false);
                quitButton.addActionListener(event -> 
                        frame.dispatchEvent(
                            new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
                        )
                );

                sidePanel.add(quitButton, BorderLayout.SOUTH);
                frame.add(sidePanel, BorderLayout.EAST);

                // HighScoreTable
                JTable table = new JTable(hsm.getTable());
                table.setFocusable(false);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(0, 150));
                frame.add(scrollPane, BorderLayout.SOUTH);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (ClassNotFoundException | IOException | ClassCastException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }
}
