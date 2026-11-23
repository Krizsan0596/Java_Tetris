package hu.krizsan.tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }

    public static void createWindow() {
            try {
                JFrame frame = new JFrame("Tetris");

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout(8, 8));

                JMenuBar menuBar = new JMenuBar();
                JMenu gameMenu = new JMenu("Game");
                JMenuItem startMenuItem = new JMenuItem("Start");
                gameMenu.add(startMenuItem);
                JMenuItem controlsMenuItem = new JMenuItem("Controls");
                controlsMenuItem.addActionListener(event -> JOptionPane.showOptionDialog(
                        frame,
                        "Left: Move left\n"
                                + "Right: Move right\n"
                                + "Down (hold): Soft drop\n"
                                + "Space: Hard drop\n"
                                + "X: Rotate clockwise\n"
                                + "Z: Rotate counterclockwise\n"
                                + "C: Hold piece",
                        "Controls",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[] { "Close" },
                        "Close"
                ));
                gameMenu.add(controlsMenuItem);
                JMenuItem quitMenuItem = new JMenuItem("Quit");
                quitMenuItem.addActionListener(event ->
                        frame.dispatchEvent(
                            new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
                        )
                );
                gameMenu.add(quitMenuItem);
                menuBar.add(gameMenu);
                frame.setJMenuBar(menuBar);

                Board board = new Board();
                GamePanel gamePanel = new GamePanel(board);
                JLabel scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
                JPanel gameContainer = new JPanel(new BorderLayout(0, 8));
                gameContainer.add(scoreLabel, BorderLayout.NORTH);
                gameContainer.add(gamePanel, BorderLayout.CENTER);
                frame.add(gameContainer, BorderLayout.CENTER);
                
                JPanel sidePanel = new JPanel(new BorderLayout(5, 8));
                sidePanel.setPreferredSize(new Dimension(170, 0));

                JPanel previews = new JPanel(new GridLayout(2, 1, 0, 8));

                PiecePanel heldPiecePanel = new PiecePanel("Hold");
                previews.add(heldPiecePanel);

                PiecePanel nextPiecePanel = new PiecePanel("Next");
                previews.add(nextPiecePanel);
                sidePanel.add(previews, BorderLayout.CENTER);

                HighScoreManager hsm = new HighScoreManager();
                LeaderboardDialog leaderboardDialog = new LeaderboardDialog(frame, hsm);
                JMenu leaderboardMenu = new JMenu("Leaderboard");
                JMenuItem viewMenuItem = new JMenuItem("View");
                viewMenuItem.addActionListener(event -> {
                    leaderboardDialog.setLocationRelativeTo(frame);
                    leaderboardDialog.setVisible(true);
                });
                leaderboardMenu.add(viewMenuItem);
                JMenuItem clearMenuItem = new JMenuItem("Clear");
                clearMenuItem.addActionListener(event -> {
                    int choice = JOptionPane.showConfirmDialog(
                            frame,
                            "Clear all leaderboard scores?",
                            "Clear Leaderboard",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        hsm.clear();
                    }
                });
                leaderboardMenu.add(clearMenuItem);
                menuBar.add(leaderboardMenu);

                GameManager gameManager = new GameManager(nextPiecePanel, heldPiecePanel, gamePanel, board, scoreLabel, hsm);
                startMenuItem.addActionListener(event -> {
                    startMenuItem.setText("Restart");
                    gameManager.start();
                });

                frame.add(sidePanel, BorderLayout.EAST);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (ClassNotFoundException | IOException | ClassCastException e) {
                e.printStackTrace();
            }
    }

}
