import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StatisticsDialog extends JDialog {

    public StatisticsDialog(JFrame parent) {
        super(parent, "System Statistics", true);
        setMinimumSize(new Dimension(480, 500));
        setSize(520, 560);
        setLocationRelativeTo(parent);
        setResizable(true);

        // Fetch data
        int total = DataManager.countTotal();
        int lost = DataManager.countLost();
        int found = DataManager.countFound();
        int claimed = DataManager.countClaimed();
        int users = DataManager.countUsers();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(DashboardFrame.BG);

        // ---- Header ----
        JPanel header = new JPanel();
        header.setBackground(new Color(40, 120, 170));
        header.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        JLabel title = new JLabel("📊  System Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // ---- Scrollable content ----
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(DashboardFrame.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        // --- Summary cards row ---
        JPanel cardRow = new JPanel(new GridLayout(1, 3, 12, 0));
        cardRow.setOpaque(false);
        cardRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        cardRow.add(summaryCard("📦 Total Items", total, new Color(60, 100, 180)));
        cardRow.add(summaryCard("👥 Total Users", users, new Color(80, 140, 80)));
        cardRow.add(summaryCard("✅ Claimed", claimed, new Color(150, 80, 10)));
        content.add(cardRow);
        content.add(Box.createVerticalStrut(18));

        // --- Status section ---
        content.add(sectionLabel("Item Status Breakdown"));
        content.add(Box.createVerticalStrut(8));
        content.add(statRow("🔴  Lost", lost, total, new Color(192, 57, 43)));
        content.add(Box.createVerticalStrut(6));
        content.add(statRow("🟢  Found", found, total, new Color(39, 174, 96)));
        content.add(Box.createVerticalStrut(6));
        content.add(statRow("✅  Claimed", claimed, total, new Color(180, 100, 10)));
        content.add(Box.createVerticalStrut(18));

        // --- Resolution rate ---
        content.add(sectionLabel("Resolution Rate"));
        content.add(Box.createVerticalStrut(8));
        int resolved = found + claimed;
        JProgressBar resBar = new JProgressBar(0, Math.max(total, 1));
        resBar.setValue(resolved);
        resBar.setStringPainted(true);
        int pct = total == 0 ? 0 : (resolved * 100 / total);
        resBar.setString(resolved + " / " + total + " items resolved  (" + pct + "%)");
        resBar.setForeground(new Color(50, 160, 80));
        resBar.setPreferredSize(new Dimension(0, 22));
        resBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        resBar.setFont(new Font("Arial", Font.BOLD, 11));
        content.add(resBar);
        content.add(Box.createVerticalStrut(18));

        // --- Category breakdown ---
        content.add(sectionLabel("Category Breakdown"));
        content.add(Box.createVerticalStrut(8));
        String[] categories = { "Electronics", "Accessory", "Book/Stationery", "Clothing", "ID/Documents", "Other" };
        Color[] catColors = {
                new Color(52, 152, 219),
                new Color(155, 89, 182),
                new Color(230, 126, 34),
                new Color(26, 188, 156),
                new Color(231, 76, 60),
                new Color(127, 140, 141)
        };
        for (int i = 0; i < categories.length; i++) {
            int count = DataManager.countByCategory(categories[i]);
            content.add(statRow(categories[i], count, total, catColors[i]));
            content.add(Box.createVerticalStrut(5));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        root.add(scroll, BorderLayout.CENTER);

        // ---- Footer ----
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        footer.setBackground(new Color(235, 240, 252));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 240)));

        JButton btnClose = new JButton("✖  Close");
        btnClose.setBackground(new Color(40, 120, 170));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 13));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setPreferredSize(new Dimension(130, 36));
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        btnClose.addMouseListener(new MouseAdapter() {
            Color bg = new Color(40, 120, 170);

            public void mouseEntered(MouseEvent e) {
                btnClose.setBackground(bg.darker());
            }

            public void mouseExited(MouseEvent e) {
                btnClose.setBackground(bg);
            }
        });
        footer.add(btnClose);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ---- UI helpers ----

    private JPanel summaryCard(String label, int value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 1, true),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(220, 235, 255));
        card.add(lbl, BorderLayout.NORTH);

        JLabel val = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 34));
        val.setForeground(Color.WHITE);
        card.add(val, BorderLayout.CENTER);

        return card;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setForeground(DashboardFrame.PRIMARY);
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(190, 210, 240)));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return l;
    }

    /** A row with label, count, and a proportional progress bar */
    private JPanel statRow(String label, int count, int total, Color color) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(50, 60, 80));
        lbl.setPreferredSize(new Dimension(150, 22));
        row.add(lbl, BorderLayout.WEST);

        JProgressBar bar = new JProgressBar(0, Math.max(total, 1));
        bar.setValue(count);
        int pct = total == 0 ? 0 : (count * 100 / total);
        bar.setStringPainted(true);
        bar.setString(count + "  (" + pct + "%)");
        bar.setForeground(color);
        bar.setFont(new Font("Arial", Font.PLAIN, 10));
        row.add(bar, BorderLayout.CENTER);

        return row;
    }
}