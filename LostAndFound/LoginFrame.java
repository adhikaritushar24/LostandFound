import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private JLabel lblError;

    private static final Color DARK_BG = new Color(15, 35, 80);
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color ACCENT = new Color(25, 95, 210);
    private static final Color ACCENT_HOV = new Color(15, 75, 180);
    private static final Color REG_COLOR = new Color(34, 160, 100);
    private static final Color REG_HOV = new Color(24, 130, 78);
    private static final Color INPUT_BG = new Color(248, 250, 255);
    private static final Color INPUT_BDR = new Color(210, 220, 240);
    private static final Color LABEL_CLR = new Color(45, 60, 90);
    private static final Color ERROR_CLR = new Color(200, 50, 50);

    public LoginFrame() {
        setTitle("Lost & Found — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(440, 560);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(10, 25, 65),
                        getWidth(), getHeight(), new Color(25, 60, 130));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        card.setPreferredSize(new Dimension(360, 460));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel cardHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(60, 130, 230));
                g2.setPaint(gp);
                // fill rounded top + square bottom so it merges with card
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 22, 22, 22);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        cardHeader.setPreferredSize(new Dimension(360, 100));

        JLabel iconLbl = new JLabel("🔍", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconLbl.setForeground(Color.WHITE);
        iconLbl.setPreferredSize(new Dimension(60, 60));
        iconLbl.setOpaque(false);

        JLabel appTitle = new JLabel("Lost & Found", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);

        JLabel appSub = new JLabel("Campus Item Tracker", SwingConstants.CENTER);
        appSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appSub.setForeground(new Color(200, 220, 255));

        JPanel titleStack = new JPanel();
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setOpaque(false);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        appSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleStack.add(Box.createVerticalStrut(16));
        titleStack.add(iconLbl);
        titleStack.add(Box.createVerticalStrut(6));
        titleStack.add(appTitle);
        titleStack.add(Box.createVerticalStrut(4));
        titleStack.add(appSub);
        titleStack.add(Box.createVerticalStrut(16));
        cardHeader.add(titleStack, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(24, 32, 10, 32));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 4, 0);

        int row = 0;

        gc.gridy = row++;
        formPanel.add(fieldLabel("Username"), gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 14, 0);
        txtUsername = styledField("Enter your username");
        formPanel.add(txtUsername, gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(fieldLabel("Password"), gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 6, 0);
        txtPassword = styledPasswordField();
        formPanel.add(wrapPasswordToggle(txtPassword), gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 14, 0);
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblError.setForeground(ERROR_CLR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblError, gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 10, 0);
        btnLogin = bigBtn("→  Sign In", ACCENT, ACCENT_HOV);
        formPanel.add(btnLogin, gc);

        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 0, 0);
        btnRegister = bigBtn("＋  Create Account", REG_COLOR, REG_HOV);
        formPanel.add(btnRegister, gc);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        footerPanel.setOpaque(false);
        JLabel hint = new JLabel("Default admin: admin / admin123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hint.setForeground(new Color(160, 175, 200));
        footerPanel.add(hint);

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(formPanel, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(BorderFactory.createEmptyBorder(6, 6, 10, 6));
        cardWrap.add(card);

        root.add(cardWrap);
        setContentPane(root);

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> doRegister());
        getRootPane().setDefaultButton(btnLogin);

        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        txtPassword.addActionListener(e -> doLogin());
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(LABEL_CLR);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(180, 190, 210));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    g2.drawString(placeholder, 12, 20);
                }
            }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(INPUT_BG);
        f.setForeground(new Color(30, 40, 70));
        f.setCaretColor(ACCENT);
        f.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BDR, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setPreferredSize(new Dimension(280, 38));

        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT, 2, true),
                        new EmptyBorder(7, 11, 7, 11)));
            }

            public void focusLost(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(INPUT_BDR, 1, true),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });
        return f;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(INPUT_BG);
        f.setForeground(new Color(30, 40, 70));
        f.setCaretColor(ACCENT);
        f.setEchoChar('●');
        f.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BDR, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setPreferredSize(new Dimension(240, 38));

        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT, 2, true),
                        new EmptyBorder(7, 11, 7, 11)));
            }

            public void focusLost(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(INPUT_BDR, 1, true),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });
        return f;
    }

    private JPanel wrapPasswordToggle(JPasswordField field) {
        JPanel wrap = new JPanel(new BorderLayout(0, 0));
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(280, 38));

        JButton eye = new JButton("👁") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 235, 248));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        eye.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        eye.setFocusPainted(false);
        eye.setBorderPainted(false);
        eye.setContentAreaFilled(false);
        eye.setOpaque(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.setToolTipText("Show/Hide password");
        eye.setPreferredSize(new Dimension(38, 38));
        eye.setMargin(new Insets(0, 0, 0, 0));

        eye.addActionListener(e -> {
            if (field.getEchoChar() == 0) {
                field.setEchoChar('●');
                eye.setToolTipText("Show password");
            } else {
                field.setEchoChar((char) 0);
                eye.setToolTipText("Hide password");
            }
        });

        wrap.add(field, BorderLayout.CENTER);
        wrap.add(eye, BorderLayout.EAST);
        return wrap;
    }

    private JButton bigBtn(String text, Color bg, Color hov) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setPreferredSize(new Dimension(280, 42));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hov);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        User user = DataManager.login(username, password);
        if (user != null) {
            lblError.setText(" ");
            dispose();
            new DashboardFrame(user).setVisible(true);
        } else {
            showError("Invalid username or password. Please try again.");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Enter a username and password to register.");
            return;
        }
        if (username.length() < 3) {
            showError("Username must be at least 3 characters.");
            return;
        }
        if (password.length() < 4) {
            showError("Password must be at least 4 characters.");
            return;
        }

        boolean ok = DataManager.registerUser(username, password);
        if (ok) {
            lblError.setForeground(new Color(30, 140, 80));
            lblError.setText("Account created! You can now sign in.");
            txtPassword.setText("");
        } else {
            showError("Username \"" + username + "\" is already taken.");
        }
    }

    private void showError(String msg) {
        lblError.setForeground(ERROR_CLR);
        lblError.setText(msg);
    }
}