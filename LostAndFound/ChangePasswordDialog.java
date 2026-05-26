import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ChangePasswordDialog extends JDialog {

    private User currentUser;
    private JPasswordField txtOld, txtNew, txtConfirm;
    private JProgressBar strengthBar;
    private JLabel lblStrength;

    public ChangePasswordDialog(JFrame parent, User user) {
        super(parent, "Change Password", true);
        this.currentUser = user;
        setMinimumSize(new Dimension(420, 360));
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(DashboardFrame.BG);

        // ---- Header ----
        JPanel header = new JPanel();
        header.setBackground(new Color(50, 100, 50));
        header.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        JLabel title = new JLabel("🔑  Change Password", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // ---- Form ----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(DashboardFrame.BG);
        form.setBorder(new EmptyBorder(20, 28, 10, 28));

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

        // Current password
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Current Password"), lc);
        txtOld = passField();
        form.add(wrapWithToggle(txtOld), fc);

        // New password
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("New Password"), lc);
        txtNew = passField();
        form.add(wrapWithToggle(txtNew), fc);

        // Strength bar
        lc.gridy = row;
        fc.gridy = row++;
        lc.insets = new Insets(0, 0, 4, 12);
        fc.insets = new Insets(0, 0, 4, 0);
        lblStrength = new JLabel("Strength:");
        lblStrength.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStrength.setForeground(new Color(100, 110, 130));
        form.add(lblStrength, lc);

        strengthBar = new JProgressBar(0, 4);
        strengthBar.setPreferredSize(new Dimension(200, 10));
        strengthBar.setStringPainted(false);
        strengthBar.setForeground(Color.GRAY);
        form.add(strengthBar, fc);

        // Restore insets
        lc.insets = new Insets(8, 0, 8, 12);
        fc.insets = new Insets(8, 0, 8, 0);

        // Confirm password
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Confirm Password"), lc);
        txtConfirm = passField();
        form.add(wrapWithToggle(txtConfirm), fc);

        // Logged in as (read-only hint)
        lc.gridy = row;
        fc.gridy = row++;
        form.add(lbl("Account"), lc);
        JLabel lblUser = new JLabel(user.getUsername() + "  [" + user.getRole() + "]");
        lblUser.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUser.setForeground(new Color(100, 110, 130));
        form.add(lblUser, fc);

        root.add(form, BorderLayout.CENTER);

        // ---- Button Row ----
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        btnRow.setBackground(new Color(235, 240, 252));
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 240)));

        JButton btnSave = btn("✔  Update Password", new Color(50, 130, 60));
        JButton btnCancel = btn("✖  Cancel", DashboardFrame.NEUTRAL);
        btnRow.add(btnSave);
        btnRow.add(btnCancel);
        root.add(btnRow, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> doChange());
        btnCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnSave);

        // Live strength check
        txtNew.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateStrength();
            }

            public void removeUpdate(DocumentEvent e) {
                updateStrength();
            }

            public void changedUpdate(DocumentEvent e) {
                updateStrength();
            }
        });

        setContentPane(root);
    }

    // ---- helpers ----

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text + ":");
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        l.setForeground(new Color(50, 60, 80));
        return l;
    }

    private JPasswordField passField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(190, 210, 240), 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        f.setPreferredSize(new Dimension(200, 30));
        return f;
    }

    /** Wraps a password field with a small show/hide toggle button */
    private JPanel wrapWithToggle(JPasswordField field) {
        JPanel wrap = new JPanel(new BorderLayout(4, 0));
        wrap.setOpaque(false);
        wrap.add(field, BorderLayout.CENTER);

        JButton toggle = new JButton("👁");
        toggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        toggle.setFocusPainted(false);
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setToolTipText("Show / Hide password");
        toggle.addActionListener(e -> {
            if (field.getEchoChar() == 0) {
                field.setEchoChar('•');
            } else {
                field.setEchoChar((char) 0);
            }
        });
        wrap.add(toggle, BorderLayout.EAST);
        return wrap;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(175, 36));
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

    private void updateStrength() {
        String pwd = new String(txtNew.getPassword());
        int score = 0;
        if (pwd.length() >= 6)
            score++;
        if (pwd.length() >= 10)
            score++;
        if (pwd.matches(".*[A-Z].*"))
            score++;
        if (pwd.matches(".*[0-9].*"))
            score++;
        if (pwd.matches(".*[^A-Za-z0-9].*"))
            score++;
        strengthBar.setValue(Math.min(score, 4));

        Color[] colors = { Color.GRAY, new Color(220, 60, 60), new Color(230, 140, 0),
                new Color(180, 180, 0), new Color(50, 160, 60) };
        String[] labels = { "", "Weak", "Fair", "Good", "Strong" };
        strengthBar.setForeground(colors[Math.min(score, 4)]);
        lblStrength.setText("Strength: " + (score == 0 ? "—" : labels[Math.min(score, 4)]));
    }

    // ---- logic ----

    private void doChange() {
        String oldPass = new String(txtOld.getPassword()).trim();
        String newPass = new String(txtNew.getPassword()).trim();
        String confirm = new String(txtConfirm.getPassword()).trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "New password must be at least 4 characters.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            txtConfirm.setText("");
            txtConfirm.requestFocus();
            return;
        }
        if (oldPass.equals(newPass)) {
            JOptionPane.showMessageDialog(this, "New password must be different from the current one.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = DataManager.changePassword(currentUser.getUsername(), oldPass, newPass);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Password changed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Current password is incorrect. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtOld.setText("");
            txtOld.requestFocus();
        }
    }
}