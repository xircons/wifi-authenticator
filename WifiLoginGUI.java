import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
    private static final File CREDENTIALS_FILE = new File("/Users/jaifha_wongkunta/Documents/wuttikan/CafeWifiSystem/wifi_user.txt");
    // private static final File CREDENTIALS_FILE = new File("C:\\Users\\P\\Documents\\GitHub\\CafeWifiSystem\\wifi_user.txt");

    public WifiLoginGUI() {
        frame = new JFrame("Wifi authentication");
        frame.setSize(450, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        Font montserratFont = loadFont("/Users/jaifha_wongkunta/Documents/wuttikan/CafeWifiSystem/font/Montserrat-Regular.ttf", 14);
        // Font montserratFont = loadFont("C:\\Users\\P\\Documents\\GitHub\\CafeWifiSystem\\font\\Montserrat-Regular.ttf", 14);

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
        passwordField.setMargin(new Insets(0, 10, 0, 0));
        passwordField.setOpaque(true);
        passwordField.setBorder(createRoundedBorder(15));
        frame.add(passwordField);

        JButton loginButton = new JButton("Login") {        
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

        loginButton.addActionListener(this::checkLogin);
        frame.setVisible(true);
    }

    private Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 18);
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
            statusLabel.setText("You're logged in!");
            statusLabel.setBackground(new Color(59, 48, 38));
            statusLabel.setBounds(155, 405, 350, 42);
        } else {
            statusLabel.setText("Wrong username or password!");
            statusLabel.setForeground(Color.RED);
            statusLabel.setBounds(115, 405, 350, 42);
        }
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