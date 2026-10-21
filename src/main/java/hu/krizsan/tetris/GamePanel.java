package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public final class GamePanel extends JPanel {
    public GamePanel() {
        setPreferredSize(new Dimension(500, 450));
        setBackground(new Color(30, 35, 40));
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.drawString("Game", 20, 25);
    }
}
