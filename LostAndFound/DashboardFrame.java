import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    static final Color PRIMARY = new Color(25, 55, 120);
    static final Color DANGER = new Color(192, 57, 43);
    static final Color SUCCESS = new Color(39, 174, 96);
    static final Color WARNING = new Color(180, 100, 10);
    static final Color NEUTRAL = new Color(80, 90, 110);
    static final Color BG = new Color(245, 248, 255);

    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblCount;
    private JComboBox<String> filterStatus, filterCategory;
    private JTextField txtSearch;

    private static final String[] COLUMNS = {
            "ID", "Item Name", "Category", "Description", "Location", "Status", "Date", "Contact", "Phone"
    };
    private static final int[] COL_WIDTHS = { 45, 130, 100, 190, 115, 75, 95, 115, 105 };

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Lost & Found — Dashboard (" + user.getUsername() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 550));
        setSize(1150, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(PRIMARY);
        bar.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lbl = new JLabel("🔍  Lost & Found  —  " +
                user("Welcome, ") + currentUser.getUsername().toUpperCase() +
                (isAdmin() ? "  [ADMIN]" : "  [User]"));
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        bar.add(lbl, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        if (isAdmin()) {
            JButton btnAdmin = topBtn("👤 Users", new Color(160, 110, 20));
            btnAdmin.addActionListener(e -> new AdminPanelDialog(this).setVisible(true));
            right.add(btnAdmin);
        }

        JButton btnStats = topBtn("📊 Statistics", new Color(40, 120, 170));
        btnStats.addActionListener(e -> new StatisticsDialog(this).setVisible(true));
        right.add(btnStats);

        JButton btnPwd = topBtn("🔑 Password", new Color(70, 100, 50));
        btnPwd.addActionListener(e -> new ChangePasswordDialog(this, currentUser).setVisible(true));
        right.add(btnPwd);

        JButton btnExport = topBtn("📥 Export CSV", new Color(80, 60, 130));
        btnExport.addActionListener(e -> exportCSV());
        right.add(btnExport);

        JButton btnLogout = topBtn("Logout", DANGER);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        right.add(btnLogout);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JButton topBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        Color hover = bg.darker();
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

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BG);
        center.add(buildFilterBar(), BorderLayout.NORTH);
        center.add(buildTable(), BorderLayout.CENTER);
        center.add(buildBtnBar(), BorderLayout.SOUTH);
        return center;
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bar.setBackground(new Color(235, 240, 252));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 215, 240)));

        bar.add(bold("Search:"));
        txtSearch = new JTextField(18);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSearch.setToolTipText("Search by name, location, description...");
        bar.add(txtSearch);

        bar.add(bold("Status:"));
        filterStatus = new JComboBox<>(new String[] { "All", "Lost", "Found", "Claimed" });
        filterStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        bar.add(filterStatus);

        bar.add(bold("Category:"));
        filterCategory = new JComboBox<>(new String[] {
                "All", "Electronics", "Accessory", "Book/Stationery", "Clothing", "ID/Documents", "Other" });
        filterCategory.setFont(new Font("Arial", Font.PLAIN, 12));
        bar.add(filterCategory);

        JButton btnClear = new JButton("✕ Clear");
        btnClear.setFont(new Font("Arial", Font.PLAIN, 11));
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            filterStatus.setSelectedIndex(0);
            filterCategory.setSelectedIndex(0);
            refreshTable();
        });
        bar.add(btnClear);

        lblCount = new JLabel();
        lblCount.setFont(new Font("Arial", Font.ITALIC, 12));
        lblCount.setForeground(new Color(80, 100, 130));
        bar.add(lblCount);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                refreshTable();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                refreshTable();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                refreshTable();
            }
        });
        filterStatus.addActionListener(e -> refreshTable());
        filterCategory.addActionListener(e -> refreshTable());

        return bar;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 226, 240));
        table.setSelectionBackground(new Color(173, 216, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(new Font("Arial", Font.BOLD, 12));
        hdr.setBackground(PRIMARY);
        hdr.setForeground(Color.WHITE);
        hdr.setReorderingAllowed(false);
        hdr.setPreferredSize(new Dimension(hdr.getPreferredSize().width, 34));
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(PRIMARY);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(40, 70, 140)),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)));
                label.setOpaque(true);
                return label;
            }
        });

        for (int i = 0; i < COL_WIDTHS.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(COL_WIDTHS[i]);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    String status = (String) tableModel.getValueAt(row, 5);
                    switch (status) {
                        case "Lost":
                            c.setBackground(new Color(255, 238, 238));
                            break;
                        case "Found":
                            c.setBackground(new Color(232, 255, 238));
                            break;
                        case "Claimed":
                            c.setBackground(new Color(255, 251, 224));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Double-click to view details
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    viewDetails();
            }
        });

        table.setRowSorter(new TableRowSorter<>(tableModel));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel buildBtnBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel.setBackground(new Color(235, 240, 252));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 240)));

        JButton btnAdd = actionBtn("＋ Add Item", PRIMARY);
        JButton btnEdit = actionBtn("✎ Edit Item", WARNING);
        JButton btnRemove = actionBtn("✕ Remove Item", DANGER);
        JButton btnClaim = actionBtn("✔ Mark Claimed", SUCCESS);
        JButton btnDetails = actionBtn("🔎 View Details", NEUTRAL);

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnClaim);
        panel.add(btnDetails);
        panel.add(btnRemove);

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnRemove.addActionListener(e -> removeSelectedItem());
        btnClaim.addActionListener(e -> markClaimed());
        btnDetails.addActionListener(e -> viewDetails());

        if (!isAdmin()) {
            btnRemove.setToolTipText("Only admin can remove items");
            btnRemove.setEnabled(false);
            btnRemove.setBackground(new Color(170, 170, 170));
        }

        return panel;
    }

    private JButton actionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(145, 36));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hover = bg.darker();
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

    void refreshTable() {
        tableModel.setRowCount(0);
        String keyword = txtSearch != null ? txtSearch.getText() : "";
        String status = filterStatus != null ? (String) filterStatus.getSelectedItem() : "All";
        String category = filterCategory != null ? (String) filterCategory.getSelectedItem() : "All";

        List<Item> list = DataManager.searchItems(keyword, status, category);
        for (Item item : list) {
            tableModel.addRow(new Object[] {
                    item.getId(), item.getItemName(), item.getCategory(),
                    item.getDescription(), item.getLocation(), item.getStatus(),
                    item.getDate(), item.getContactName(), item.getContactPhone()
            });
        }
        if (lblCount != null)
            lblCount.setText("  Showing " + list.size() + " item(s)");
    }

    private void openAddDialog() {
        new AddItemDialog(this, currentUser).setVisible(true);
        refreshTable();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warn("Please select an item to edit.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        Item item = DataManager.getItemById(id);
        if (item == null)
            return;
        // Only admin or the reporter can edit
        if (!isAdmin() && !currentUser.getUsername().equals(item.getReportedBy())) {
            JOptionPane.showMessageDialog(this, "You can only edit items you reported.",
                    "Permission Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new EditItemDialog(this, item).setVisible(true);
        refreshTable();
    }

    private void removeSelectedItem() {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(this, "Only admin can remove items.", "Permission Denied",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            warn("Please select an item to remove.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);
        if (JOptionPane.showConfirmDialog(this,
                "Remove \"" + name + "\"?", "Confirm Remove",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            DataManager.removeItem(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Item removed.", "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markClaimed() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warn("Please select an item to mark as claimed.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        Item item = DataManager.getItemById(id);
        if (item == null)
            return;
        if ("Claimed".equals(item.getStatus())) {
            JOptionPane.showMessageDialog(this, "Item is already marked as Claimed.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        item.setStatus("Claimed");
        refreshTable();
        JOptionPane.showMessageDialog(this, "Item marked as Claimed!", "Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warn("Please select an item to view.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        Item item = DataManager.getItemById(id);
        if (item == null)
            return;

        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBackground(new Color(248, 250, 255));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Status badge
        JLabel badge = new JLabel(item.getStatus(), SwingConstants.CENTER);
        badge.setFont(new Font("Arial", Font.BOLD, 13));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        switch (item.getStatus()) {
            case "Lost":
                badge.setBackground(DANGER);
                break;
            case "Found":
                badge.setBackground(SUCCESS);
                break;
            default:
                badge.setBackground(WARNING);
                break;
        }
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        badgeWrap.setOpaque(false);
        badgeWrap.add(badge);
        detailPanel.add(badgeWrap, BorderLayout.NORTH);

        String[][] rows = {
                { "Item ID", String.valueOf(item.getId()) },
                { "Item Name", item.getItemName() },
                { "Category", item.getCategory() },
                { "Description", item.getDescription() },
                { "Location", item.getLocation() },
                { "Date", item.getDate() },
                { "Contact Name", item.getContactName() },
                { "Phone", item.getContactPhone() },
                { "Reported By", item.getReportedBy() },
        };

        JPanel grid = new JPanel(new GridLayout(rows.length, 2, 8, 6));
        grid.setBackground(new Color(248, 250, 255));
        for (String[] r : rows) {
            JLabel k = new JLabel(r[0] + ":");
            k.setFont(new Font("Arial", Font.BOLD, 12));
            JLabel v = new JLabel(r[1]);
            v.setFont(new Font("Arial", Font.PLAIN, 12));
            grid.add(k);
            grid.add(v);
        }
        detailPanel.add(grid, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, detailPanel, "Item Details — #" + item.getId(), JOptionPane.PLAIN_MESSAGE);
    }

    private void exportCSV() {
        String keyword = txtSearch.getText();
        String status = (String) filterStatus.getSelectedItem();
        String category = (String) filterCategory.getSelectedItem();
        List<Item> list = DataManager.searchItems(keyword, status, category);
        String csv = DataManager.exportToCSV(list);

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("lost_found_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                fw.write(csv);
                JOptionPane.showMessageDialog(this, "Exported " + list.size() + " items to CSV.", "Export Done",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing file: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "No Selection", JOptionPane.WARNING_MESSAGE);
    }

    private boolean isAdmin() {
        return "admin".equals(currentUser.getRole());
    }

    private String user(String prefix) {
        return prefix;
    }

    private JLabel bold(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }
}