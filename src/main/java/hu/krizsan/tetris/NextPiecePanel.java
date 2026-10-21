package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public final class NextPiecePanel extends JPanel {
    public NextPiecePanel() {
        setPreferredSize(new Dimension(160, 180));
        setBackground(new Color(30, 35, 40));
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.drawString("Next piece", 10, 20);

        g.setColor(Color.YELLOW);
        g.fillRect(45, 50, 25, 25);
        g.fillRect(45, 75, 25, 25);
        g.fillRect(70, 50, 25, 25);
        g.fillRect(70, 75, 25, 25);
    }
}
