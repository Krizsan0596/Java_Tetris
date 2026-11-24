package hu.krizsan.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PiecePanel extends JPanel {
    private Piece piece;
    private final JLabel label;

    public PiecePanel(String title) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(160, 180));
        setBackground(Palette.BACKGROUND);
        label = new JLabel(title, JLabel.CENTER);
        label.setForeground(Palette.LABEL);
        add(label, BorderLayout.NORTH);
    }

    public void setLabelColor(Color color) {
        label.setForeground(color);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        repaint();
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (piece == null) {
            return;
        }

        g.setColor(piece.getColor());
        for (int[] offset : piece.getOffsets()) {
            g.fillRect(65 + offset[0] * 25, 75 + offset[1] * 25, 25, 25);
        }
    }
}
