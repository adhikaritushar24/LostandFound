import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddItemDialog extends JDialog {

    private JTextField txtName, txtDescription, txtLocation, txtContact, txtPhone, txtDate;
    private JComboBox<String> cmbCategory, cmbStatus;

    public AddItemDialog(JFrame parent, User user) {
        super(parent, "Add New Item", true);
        setSize(460, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 248, 255));

        JLabel lblTitle = new JLabel("Add Lost / Found Item", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 60, 114));
        lblTitle.setBounds(0, 10, 460, 30);
        panel.add(lblTitle);

        // Form fields
        int startY = 55;
        int gap = 40;

        addLabel(panel, "Item Name *", 30, startY);
        txtName = addField(panel, 160, startY, 260);

        addLabel(panel, "Category *", 30, startY + gap);
        cmbCategory = new JComboBox<>(new String[]{
            "Electronics", "Accessory", "Book/Stationery", "Clothing", "ID/Documents", "Other"
        });
        cmbCategory.setBounds(160, startY + gap, 260, 28);
        cmbCategory.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(cmbCategory);

        addLabel(panel, "Status *", 30, startY + gap * 2);
        cmbStatus = new JComboBox<>(new String[]{"Lost", "Found"});
        cmbStatus.setBounds(160, startY + gap * 2, 260, 28);
        cmbStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(cmbStatus);

        addLabel(panel, "Description", 30, startY + gap * 3);
        txtDescription = addField(panel, 160, startY + gap * 3, 260);

        addLabel(panel, "Location *", 30, startY + gap * 4);
        txtLocation = addField(panel, 160, startY + gap * 4, 260);

        addLabel(panel, "Date (YYYY-MM-DD)", 30, startY + gap * 5);
        txtDate = addField(panel, 160, startY + gap * 5, 160);
        txtDate.setText(LocalDate.now().toString()); // auto fill today's date

        addLabel(panel, "Contact Name *", 30, startY + gap * 6);
        txtContact = addField(panel, 160, startY + gap * 6, 260);
        txtContact.setText(user.getUsername()); // auto fill current user

        addLabel(panel, "Phone Number", 30, startY + gap * 7);
        txtPhone = addField(panel, 160, startY + gap * 7, 180);

        // Buttons
        JButton btnSave = new JButton("Save Item");
        btnSave.setBounds(100, startY + gap * 8 + 5, 120, 35);
        btnSave.setBackground(new Color(30, 60, 114));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 13));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(240, startY + gap * 8 + 5, 100, 35);
        btnCancel.setBackground(new Color(150, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(btnCancel);

        btnSave.addActionListener(e -> saveItem());
        btnCancel.addActionListener(e -> dispose());

        setContentPane(panel);
    }

    private JLabel addLabel(JPanel panel, String text, int x, int y) {
        JLabel lbl = new JLabel(text + ":");
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(50, 60, 80));
        lbl.setBounds(x, y + 2, 130, 24);
        panel.add(lbl);
        return lbl;
    }

    private JTextField addField(JPanel panel, int x, int y, int width) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, 28);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(field);
        return field;
    }

    private void saveItem() {
        String name = txtName.getText().trim();
        String location = txtLocation.getText().trim();
        String contact = txtContact.getText().trim();
        String date = txtDate.getText().trim();

        if (name.isEmpty() || location.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all required (*) fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            date = java.time.LocalDate.now().toString();
        }

        Item newItem = new Item(
            DataManager.getNextId(),
            name,
            (String) cmbCategory.getSelectedItem(),
            txtDescription.getText().trim(),
            location,
            (String) cmbStatus.getSelectedItem(),
            date,
            contact,
            txtPhone.getText().trim()
        );

        DataManager.addItem(newItem);
        JOptionPane.showMessageDialog(this,
                "Item added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
