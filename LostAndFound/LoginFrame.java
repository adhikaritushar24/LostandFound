import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private JLabel lblMessage;

    public LoginFrame() {
        setTitle("Lost & Found System - Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 60, 114));
        mainPanel.setLayout(null);

        // Title label
        JLabel lblTitle = new JLabel("Lost & Found System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 420, 35);
        mainPanel.add(lblTitle);

        JLabel lblSub = new JLabel("Please login to continue", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(180, 200, 255));
        lblSub.setBounds(0, 55, 420, 20);
        mainPanel.add(lblSub);

        // White form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        formPanel.setBounds(40, 90, 340, 185);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));
        mainPanel.add(formPanel);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 13));
        lblUser.setBounds(20, 20, 90, 25);
        formPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(110, 20, 200, 28);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPass.setBounds(20, 60, 90, 25);
        formPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(110, 60, 200, 28);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(30, 105, 120, 33);
        btnLogin.setBackground(new Color(30, 60, 114));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(btnLogin);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(180, 105, 120, 33);
        btnRegister.setBackground(new Color(60, 160, 80));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(btnRegister);

        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 11));
        lblMessage.setForeground(Color.RED);
        lblMessage.setBounds(0, 145, 340, 20);
        formPanel.add(lblMessage);

        setContentPane(mainPanel);

        // Button Actions
        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> doRegister());

        // Allow Enter key on password field to login
        txtPassword.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password.");
            return;
        }

        User user = DataManager.login(username, password);
        if (user != null) {
            lblMessage.setForeground(new Color(0, 140, 0));
            lblMessage.setText("Login successful! Opening dashboard...");
            Timer timer = new Timer(800, evt -> {
                dispose();
                DashboardFrame dash = new DashboardFrame(user);
                dash.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Invalid username or password.");
            txtPassword.setText("");
        }
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Fill both fields to register.");
            return;
        }
        if (password.length() < 4) {
            lblMessage.setText("Password must be at least 4 characters.");
            return;
        }

        boolean success = DataManager.registerUser(username, password);
        if (success) {
            lblMessage.setForeground(new Color(0, 140, 0));
            lblMessage.setText("Registered! You can now login.");
        } else {
            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Username already taken. Try another.");
        }
    }
}
