package hu.krizsan.tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LeaderboardDialog extends JDialog {
    public LeaderboardDialog(JFrame owner, HighScoreManager hsm) {
        super(owner, "Leaderboard", true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        JTable table = new JTable(hsm.getTable());
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(560, 300));
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(240);
        table.setDefaultRenderer(LocalDateTime.class, new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            protected void setValue(Object value) {
                setText(value == null ? "" : formatter.format((LocalDateTime) value));
            }
        });

        add(new JScrollPane(table));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> setVisible(false));
        JPanel buttons = new JPanel();
        buttons.add(backButton);
        add(buttons, BorderLayout.SOUTH);
        pack();
    }
}
