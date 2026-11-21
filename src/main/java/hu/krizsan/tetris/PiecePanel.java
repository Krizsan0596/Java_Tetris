package hu.krizsan.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public final class PiecePanel extends JPanel {
    private Piece piece;

    public PiecePanel() {
        setPreferredSize(new Dimension(160, 180));
        setBackground(new Color(48, 52, 70));
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
