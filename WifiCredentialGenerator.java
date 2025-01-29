import java.io.*;
import java.security.SecureRandom;

public class WifiCredentialGenerator {
    private static final File USER_FILE = new File("/Users/jaifha_wongkunta/Desktop/WifiSystem/wifi_user.txt");

    public static void main(String[] args) {
        String username = generateUsername(4);
        String password = generatePassword();

        System.out.println("Generate wifi:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        saveUser(username, password);
    }

    private static String generateUsername(int length) {
        String chars = "ABCDEFGHIKLMNOPQRSTUVWXYZ1234567890";
        SecureRandom random = new SecureRandom();
        StringBuilder username = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            username.append(chars.charAt(random.nextInt(chars.length())));
        }
        return username.toString();
    }

    private static String generatePassword() {
        return "" + new SecureRandom().nextInt(1000, 9999);
    }

    private static void saveUser(String username, String password) {
        try {
            File directory = new File("/Users/jaifha_wongkunta/Desktop/WifiSystem");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
                writer.write(username + ":" + password);
                writer.newLine();
                System.out.println("✅ Saved to: " + USER_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving: " + e.getMessage());
        }
    }
}
