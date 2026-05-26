import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminPanelDialog extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JLabel lblCount;

    private static final String[] COLS = { "#", "Username", "Role", "Email", "Phone" };

    public AdminPanelDialog(JFrame parent) {
        super(parent, "Admin Panel — User Management", true);
        setMinimumSize(new Dimension(650, 450));
        setSize(750, 550);
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(248, 250, 254));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 45, 100));
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("👤  User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        lblCount = new JLabel();
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCount.setForeground(new Color(200, 220, 255));
        header.add(lblCount, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        searchBar.setBackground(new Color(255, 255, 255));
        searchBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 245)));

        JLabel srchLbl = new JLabel("🔍");
        srchLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBar.add(srchLbl);

        txtSearch = new JTextField(22);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 228, 245), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        txtSearch.setToolTipText("Filter by username or role");
        searchBar.add(txtSearch);

        JButton btnClear = new JButton("✕");
        btnClear.setFont(new Font("Arial", Font.PLAIN, 12));
        btnClear.setFocusPainted(false);
        btnClear.setPreferredSize(new Dimension(32, 32));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            loadUsers("");
        });
        searchBar.add(btnClear);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                loadUsers(txtSearch.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                loadUsers(txtSearch.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                loadUsers(txtSearch.getText());
            }
        });

        // Table
        model = new DefaultTableModel(COLS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.setGridColor(new Color(235, 238, 245));
        table.setSelectionBackground(new Color(180, 215, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hdr.setBackground(new Color(15, 45, 100));
        hdr.setForeground(Color.WHITE);
        hdr.setReorderingAllowed(false);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                label.setBackground(new Color(15, 45, 100));
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                return label;
            }
        });

        int[] widths = { 35, 180, 80, 200, 130 };
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String role = (String) model.getValueAt(r, 2);
                    comp.setBackground("admin".equals(role)
                            ? new Color(255, 248, 240)
                            : (r % 2 == 0 ? Color.WHITE : new Color(252, 253, 255)));
                }
                ((JLabel) comp).setHorizontalAlignment(c == 2 ? CENTER : LEFT);
                ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return comp;
            }
        });

        loadUsers("");
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(248, 250, 254));

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.add(searchBar, BorderLayout.NORTH);
        centerWrap.add(scroll, BorderLayout.CENTER);
        root.add(centerWrap, BorderLayout.CENTER);

        // Button Row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnPanel.setBackground(new Color(255, 255, 255));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 235, 245)));

        JButton btnPromote = btn("⬆ Promote", new Color(150, 100, 20));
        JButton btnDemote = btn("⬇ Demote", new Color(70, 90, 170));
        JButton btnDelete = btn("🗑 Delete", new Color(220, 70, 70));
        JButton btnClose = btn("✖ Close", new Color(110, 125, 150));

        btnPanel.add(btnPromote);
        btnPanel.add(btnDemote);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClose);
        root.add(btnPanel, BorderLayout.SOUTH);

        btnPromote.addActionListener(e -> changeRole(true));
        btnDemote.addActionListener(e -> changeRole(false));
        btnDelete.addActionListener(e -> deleteUser());
        btnClose.addActionListener(e -> dispose());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    showUserDetails();
            }
        });

        setContentPane(root);
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        Color hover = new Color(
                Math.min(bg.getRed() + 30, 255),
                Math.min(bg.getGreen() + 30, 255),
                Math.min(bg.getBlue() + 30, 255));

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hover);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    private void loadUsers(String filter) {
        model.setRowCount(0);
        List<User> users = DataManager.getAllUsers();
        String kw = filter == null ? "" : filter.toLowerCase().trim();
        int count = 0;
        for (User u : users) {
            if (!kw.isEmpty() &&
                    !u.getUsername().toLowerCase().contains(kw) &&
                    !u.getRole().toLowerCase().contains(kw))
                continue;
            model.addRow(new Object[] {
                    ++count,
                    u.getUsername(),
                    u.getRole(),
                    u.getEmail() == null ? "—" : u.getEmail(),
                    u.getPhone() == null ? "—" : u.getPhone()
            });
        }
        lblCount.setText(count + " user(s)  ");
    }

    private String getSelectedUsername() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) model.getValueAt(row, 1);
    }

    private void changeRole(boolean promote) {
        String username = getSelectedUsername();
        if (username == null)
            return;
        if ("admin".equals(username) && !promote) {
            JOptionPane.showMessageDialog(this, "Cannot demote main admin",
                    "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = promote ? DataManager.promoteUser(username) : DataManager.demoteUser(username);
        if (ok) {
            loadUsers(txtSearch.getText());
            JOptionPane.showMessageDialog(this,
                    username + " is now " + (promote ? "Admin" : "User"),
                    "Role Updated", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteUser() {
        String username = getSelectedUsername();
        if (username == null)
            return;
        if ("admin".equals(username)) {
            JOptionPane.showMessageDialog(this, "Cannot delete main admin",
                    "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user \"" + username + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            DataManager.deleteUser(username);
            loadUsers(txtSearch.getText());
            JOptionPane.showMessageDialog(this, "User deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showUserDetails() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String username = (String) model.getValueAt(row, 1);
        User u = DataManager.getUserByUsername(username);
        if (u == null)
            return;

        JPanel detail = new JPanel(new GridLayout(0, 2, 12, 10));
        detail.setBackground(new Color(250, 252, 255));
        detail.setBorder(new EmptyBorder(12, 16, 12, 16));

        String[][] fields = {
                { "Username", u.getUsername() },
                { "Role", u.getRole() },
                { "Email", u.getEmail() == null ? "—" : u.getEmail() },
                { "Phone", u.getPhone() == null ? "—" : u.getPhone() },
                { "Items Reported", String.valueOf(DataManager.getItemsByUser(u.getUsername()).size()) }
        };
        for (String[] f : fields) {
            JLabel k = new JLabel(f[0] + ":");
            k.setFont(new Font("Segoe UI", Font.BOLD, 12));
            k.setForeground(new Color(15, 45, 100));
            JLabel v = new JLabel(f[1]);
            v.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            detail.add(k);
            detail.add(v);
        }
        JOptionPane.showMessageDialog(this, detail,
                "User: " + username, JOptionPane.PLAIN_MESSAGE);
    }
}