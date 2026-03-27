public class Main {
    public static void main(String[] args) {
        // Start the application with Login Screen
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
