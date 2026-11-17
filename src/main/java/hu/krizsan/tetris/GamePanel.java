package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public final class GamePanel extends JPanel {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static final int BLOCK_SIZE = 25;

    public GamePanel() {
        setPreferredSize(new Dimension(BLOCK_SIZE * WIDTH, BLOCK_SIZE * HEIGHT));
        setBackground(new Color(48, 52, 70));
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.drawString("Game", 20, 25);
    }
}
