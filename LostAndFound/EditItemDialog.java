import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class EditItemDialog extends JDialog {

    private Item item;
    private JTextField txtName, txtDescription, txtLocation, txtContact, txtPhone, txtDate;
    private JComboBox<String> cmbCategory, cmbStatus;

    public EditItemDialog(JFrame parent, Item item) {
        super(parent, "Edit Item #" + item.getId(), true);
        this.item = item;
        setMinimumSize(new Dimension(500, 540));
        setSize(560, 620);
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(248, 250, 254));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(200, 130, 50));
        header.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel titleLbl = new JLabel("✏  Edit Item  #" + item.getId());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLbl.setForeground(Color.WHITE);
        header.add(titleLbl, BorderLayout.WEST);

        JLabel statusBadge = makeBadge(item.getStatus());
        header.add(statusBadge, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(248, 250, 254));
        form.setBorder(new EmptyBorder(18, 26, 12, 26));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(8, 0, 8, 12);
        lc.gridx = 0;
        lc.weightx = 0;

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.insets = new Insets(8, 0, 8, 0);
        fc.gridx = 1;
        fc.weightx = 1;

        int row = 0;

        // Item Name
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Item Name *"), lc);
        txtName = field(item.getItemName());
        form.add(txtName, fc);

        // Category
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Category *"), lc);
        cmbCategory = new JComboBox<>(
                new String[] { "Electronics", "Accessory", "Book/Stationery", "Clothing", "ID/Documents", "Other" });
        styleCombo(cmbCategory);
        cmbCategory.setSelectedItem(item.getCategory());
        form.add(cmbCategory, fc);

        // Status
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Status *"), lc);
        cmbStatus = new JComboBox<>(new String[] { "Lost", "Found", "Claimed" });
        styleCombo(cmbStatus);
        cmbStatus.setSelectedItem(item.getStatus());
        cmbStatus.addActionListener(e -> updateBadge(statusBadge, (String) cmbStatus.getSelectedItem()));
        form.add(cmbStatus, fc);

        // Description
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Description"), lc);
        txtDescription = field(item.getDescription());
        form.add(txtDescription, fc);

        // Location
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Location *"), lc);
        txtLocation = field(item.getLocation());
        form.add(txtLocation, fc);

        // Date
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Date (YYYY-MM-DD)"), lc);
        txtDate = field(item.getDate());
        form.add(txtDate, fc);

        // Contact Name
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Contact Name *"), lc);
        txtContact = field(item.getContactName());
        form.add(txtContact, fc);

        // Phone
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Phone Number"), lc);
        txtPhone = field(item.getContactPhone());
        form.add(txtPhone, fc);

        // Reported By (read-only)
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Reported By"), lc);
        JTextField txtReportedBy = field(item.getReportedBy());
        txtReportedBy.setEditable(false);
        txtReportedBy.setBackground(new Color(240, 242, 248));
        txtReportedBy.setForeground(new Color(110, 125, 150));
        form.add(txtReportedBy, fc);

        root.add(form, BorderLayout.CENTER);

        // Button row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        btnRow.setBackground(new Color(255, 255, 255));
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 235, 245)));

        JButton btnSave = btn("✔ Update", new Color(200, 130, 50));
        JButton btnCancel = btn("✖ Cancel", new Color(110, 125, 150));
        btnRow.add(btnSave);
        btnRow.add(btnCancel);
        root.add(btnRow, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveEdit());
        btnCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnSave);

        setContentPane(root);
    }

    private JLabel makeBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        updateBadge(badge, status);
        return badge;
    }

    private void updateBadge(JLabel badge, String status) {
        switch (status) {
            case "Lost":
                badge.setBackground(new Color(220, 70, 70));
                break;
            case "Found":
                badge.setBackground(new Color(45, 185, 110));
                break;
            default:
                badge.setBackground(new Color(200, 130, 50));
                break;
        }
        badge.setText("  " + status + "  ");
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text + ":");
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 90));
        return l;
    }

    private JTextField field(String value) {
        JTextField f = new JTextField(value == null ? "" : value);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 228, 245), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(280, 32));
        f.setBackground(new Color(252, 253, 255));
        return f;
    }

    private void styleCombo(JComboBox<String> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        c.setPreferredSize(new Dimension(280, 32));
        c.setBackground(new Color(252, 253, 255));
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(140, 38));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

    private void saveEdit() {
        String name = txtName.getText().trim();
        String loc = txtLocation.getText().trim();
        String contact = txtContact.getText().trim();
        String date = txtDate.getText().trim();

        if (name.isEmpty() || loc.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Fill all required (*) fields",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!date.isEmpty() && !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Date format: YYYY-MM-DD",
                    "Invalid Format", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (date.isEmpty())
            date = java.time.LocalDate.now().toString();

        item.setItemName(name);
        item.setCategory((String) cmbCategory.getSelectedItem());
        item.setStatus((String) cmbStatus.getSelectedItem());
        item.setDescription(txtDescription.getText().trim());
        item.setLocation(loc);
        item.setDate(date);
        item.setContactName(contact);
        item.setContactPhone(txtPhone.getText().trim());

        JOptionPane.showMessageDialog(this,
                "✓ Item #" + item.getId() + " updated",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}