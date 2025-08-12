import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class WifiLoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel loggedInLabel;
    private JLabel countdownLabel;
    private JButton loginButton;
    private javax.swing.Timer countdownTimer;
    private long remainingSeconds;
    private String loggedInUsername;
    private Font montserratFont;
    private static final File CREDENTIALS_FILE = new File("wifi_user.txt");

    public WifiLoginGUI() {
        frame = new JFrame("Wifi authentication");
        frame.setSize(450, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        montserratFont = loadFont("font/Montserrat-Regular.ttf", 14);

        ImageIcon logoIcon = new ImageIcon("logo/resizelogo.jpg");
        frame.setIconImage(logoIcon.getImage());

        ImageIcon originalIcon = new ImageIcon("logo/LOGO.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel label = new JLabel(resizedIcon);
        label.setBounds(70, -20, 300, 300);
        frame.add(label);
        
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(50, 255, 250, 42);
        usernameLabel.setFont(montserratFont);
        frame.add(usernameLabel);

        usernameField = new JTextField("");
        usernameField.setBounds(50, 290, 340, 42);
        usernameField.setFont(montserratFont);
        usernameField.setMargin(new Insets(0, 10, 0, 0));
        usernameField.setOpaque(true);
        usernameField.setBorder(createRoundedBorder(15));
        frame.add(usernameField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 325, 250, 42);
        passwordLabel.setFont(montserratFont);
        frame.add(passwordLabel);

        passwordField = new JPasswordField("");
        passwordField.setBounds(50, 360, 340, 42);
        passwordField.setFont(montserratFont);
        passwordField.setMargin(new Insets(0, 10, 0, 0));
        passwordField.setOpaque(true);
        passwordField.setBorder(createRoundedBorder(15));
        // Configure password masking with a font-compatible bullet
        configurePasswordEchoChar();
        frame.add(passwordField);

        loginButton = new JButton("Login") {        
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
        
                super.paintComponent(g);
            }
        };
        loginButton.setBounds(50, 450, 340, 42);
        loginButton.setFont(montserratFont);
        loginButton.setBackground(new Color(59, 48, 38));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        frame.add(loginButton);              

        statusLabel = new JLabel("");
        statusLabel.setFont(montserratFont);
        frame.add(statusLabel);

        // Post-login UI elements (initially not visible)
        loggedInLabel = new JLabel("");
        loggedInLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loggedInLabel.setFont(montserratFont.deriveFont(18f));
        loggedInLabel.setForeground(new Color(59, 48, 38));
        loggedInLabel.setBounds(0, 300, 450, 40);
        loggedInLabel.setVisible(false);
        frame.add(loggedInLabel);

        countdownLabel = new JLabel("");
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownLabel.setFont(montserratFont.deriveFont(24f));
        countdownLabel.setForeground(new Color(59, 48, 38));
        countdownLabel.setBounds(0, 340, 450, 40);
        countdownLabel.setVisible(false);
        frame.add(countdownLabel);

        loginButton.addActionListener(this::checkLogin);
        frame.setVisible(true);
    }

    private void configurePasswordEchoChar() {
        // Prefer standard bullet '•' (U+2022). Fallback to black circle '●' (U+25CF) or LAF default, then '*'.
        char bullet = '\u2022';
        char blackCircle = '\u25CF';

        if (montserratFont != null && montserratFont.canDisplay(bullet)) {
            passwordField.setEchoChar(bullet);
            return;
        }
        if (montserratFont != null && montserratFont.canDisplay(blackCircle)) {
            passwordField.setEchoChar(blackCircle);
            return;
        }
        Object lafEcho = UIManager.get("PasswordField.echoChar");
        if (lafEcho instanceof Character) {
            passwordField.setEchoChar((Character) lafEcho);
            return;
        }
        passwordField.setEchoChar('*');
    }

    private Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Roboto", Font.PLAIN, 14);
        }
    }

    private Border createRoundedBorder(int radius) {
        return new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(59, 48, 38));
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
    
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0, 10, 0, 0);
            }
    
            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }    

    private void checkLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateCredentials(username, password)) {
            loggedInUsername = username;
            statusLabel.setText("");
            showMainPage();
        } else {
            statusLabel.setText("Wrong username or password!");
            statusLabel.setForeground(Color.RED);
            statusLabel.setBounds(115, 405, 350, 42);
        }
    }

    private void showMainPage() {
        // Hide login controls
        usernameLabel.setVisible(false);
        usernameField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        loginButton.setVisible(false);

        // Show main page labels
        loggedInLabel.setText("Username: " + loggedInUsername);
        loggedInLabel.setVisible(true);
        countdownLabel.setVisible(true);

        startCountdown(3600); // 1 hour
    }

    private void startCountdown(int totalSeconds) {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        remainingSeconds = totalSeconds;
        countdownLabel.setText("Time remaining: " + formatDuration(remainingSeconds));

        countdownTimer = new javax.swing.Timer(1000, ae -> {
            remainingSeconds--;
            if (remainingSeconds <= 0) {
                countdownLabel.setText("Time expired. Please login again.");
                ((javax.swing.Timer) ae.getSource()).stop();
                SwingUtilities.invokeLater(this::resetToLogin);
            } else {
                countdownLabel.setText("Time remaining: " + formatDuration(remainingSeconds));
            }
        });
        countdownTimer.start();
    }

    private void resetToLogin() {
        // Hide main page UI
        loggedInLabel.setVisible(false);
        countdownLabel.setVisible(false);

        // Show login controls again
        usernameLabel.setVisible(true);
        usernameField.setVisible(true);
        passwordLabel.setVisible(true);
        passwordField.setVisible(true);
        loginButton.setVisible(true);

        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText("Session expired. Please login again.");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBounds(95, 405, 350, 42);
    }

    private String formatDuration(long seconds) {
        long hrs = seconds / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hrs, mins, secs);
    }

    private boolean validateCredentials(String username, String password) {
        if (!CREDENTIALS_FILE.exists()) return false;

        try (Scanner scanner = new Scanner(CREDENTIALS_FILE)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] credentials = line.split(":");

                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WifiLoginGUI::new);
    }
}