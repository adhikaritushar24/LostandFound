import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblWelcome, lblCount;
    private JComboBox<String> filterCombo;

    private static final String[] COLUMNS = {
        "ID", "Item Name", "Category", "Description", "Location", "Status", "Date", "Contact Name", "Phone"
    };

    public DashboardFrame(User user) {
        this.currentUser = user;

        setTitle("Lost & Found - Dashboard (" + user.getUsername() + ")");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- TOP BAR ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 60, 114));
        topBar.setPreferredSize(new Dimension(1000, 55));

        lblWelcome = new JLabel("  Welcome, " + user.getUsername().toUpperCase() +
                (user.getRole().equals("admin") ? "  [ADMIN]" : "  [User]"));
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 15));
        lblWelcome.setForeground(Color.WHITE);
        topBar.add(lblWelcome, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(200, 60, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnLogout);
        topBar.add(rightPanel, BorderLayout.EAST);

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // ---- FILTER BAR ----
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterBar.setBackground(new Color(240, 245, 255));
        filterBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 215, 240)));

        filterBar.add(new JLabel("Show:"));
        filterCombo = new JComboBox<>(new String[]{"All Items", "Lost Only", "Found Only"});
        filterCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        filterBar.add(filterCombo);

        lblCount = new JLabel();
        lblCount.setFont(new Font("Arial", Font.ITALIC, 12));
        lblCount.setForeground(new Color(80, 100, 130));
        filterBar.add(lblCount);

        filterCombo.addActionListener(e -> refreshTable());

        // ---- TABLE ----
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(180, 210, 255));
        table.setGridColor(new Color(220, 225, 235));

        // Column widths
        int[] colWidths = {40, 120, 100, 180, 120, 70, 90, 110, 100};
        for (int i = 0; i < colWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }

        // Color lost/found rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    String status = (String) tableModel.getValueAt(row, 5);
                    if ("Lost".equals(status)) {
                        c.setBackground(new Color(255, 240, 240));
                    } else {
                        c.setBackground(new Color(235, 255, 240));
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // ---- BOTTOM BUTTONS ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(new Color(240, 245, 255));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 240)));

        JButton btnAdd = new JButton("+ Add Item");
        styleButton(btnAdd, new Color(30, 60, 114));

        JButton btnRemove = new JButton("- Remove Item");
        styleButton(btnRemove, new Color(180, 50, 50));

        JButton btnDetails = new JButton("View Details");
        styleButton(btnDetails, new Color(80, 130, 80));

        btnPanel.add(btnAdd);
        btnPanel.add(btnRemove);
        btnPanel.add(btnDetails);

        // Admin-only: remove is for all, but let's allow user to only remove own entries
        // For simplicity, only admin can remove
        if (!currentUser.getRole().equals("admin")) {
            btnRemove.setToolTipText("Only admin can remove items");
        }

        btnAdd.addActionListener(e -> openAddDialog());
        btnRemove.addActionListener(e -> removeSelectedItem());
        btnDetails.addActionListener(e -> viewDetails());

        // ---- LAYOUT ----
        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(filterBar, BorderLayout.BEFORE_FIRST_LINE); // trick for 2 top rows

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(filterBar, BorderLayout.NORTH);
        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        centerWrapper.add(btnPanel, BorderLayout.SOUTH);

        add(topBar, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        refreshTable();
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String filter = (String) filterCombo.getSelectedItem();
        List<Item> list;

        if ("Lost Only".equals(filter)) {
            list = DataManager.getLostItems();
        } else if ("Found Only".equals(filter)) {
            list = DataManager.getFoundItems();
        } else {
            list = DataManager.getAllItems();
        }

        for (Item item : list) {
            tableModel.addRow(new Object[]{
                item.getId(),
                item.getItemName(),
                item.getCategory(),
                item.getDescription(),
                item.getLocation(),
                item.getStatus(),
                item.getDate(),
                item.getContactName(),
                item.getContactPhone()
            });
        }
        lblCount.setText("  Total: " + list.size() + " item(s)");
    }

    private void openAddDialog() {
        AddItemDialog dialog = new AddItemDialog(this, currentUser);
        dialog.setVisible(true);
        refreshTable();
    }

    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!currentUser.getRole().equals("admin")) {
            JOptionPane.showMessageDialog(this, "Only Admin can remove items.",
                    "Permission Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove \"" + name + "\"?",
                "Confirm Remove", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DataManager.removeItem(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Item removed successfully.",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to view.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Item ID     : ").append(tableModel.getValueAt(selectedRow, 0)).append("\n");
        sb.append("Item Name   : ").append(tableModel.getValueAt(selectedRow, 1)).append("\n");
        sb.append("Category    : ").append(tableModel.getValueAt(selectedRow, 2)).append("\n");
        sb.append("Description : ").append(tableModel.getValueAt(selectedRow, 3)).append("\n");
        sb.append("Location    : ").append(tableModel.getValueAt(selectedRow, 4)).append("\n");
        sb.append("Status      : ").append(tableModel.getValueAt(selectedRow, 5)).append("\n");
        sb.append("Date        : ").append(tableModel.getValueAt(selectedRow, 6)).append("\n");
        sb.append("Contact     : ").append(tableModel.getValueAt(selectedRow, 7)).append("\n");
        sb.append("Phone       : ").append(tableModel.getValueAt(selectedRow, 8)).append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 250, 255));

        JOptionPane.showMessageDialog(this, textArea, "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
