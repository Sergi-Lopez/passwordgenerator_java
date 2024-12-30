import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.security.SecureRandom;
import javax.swing.*;

public class PasswordGeneratorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordGeneratorUI().createAndShowGUI());
    }
}

class PasswordGeneratorUI {

    private JFrame frame;
    private JSpinner lengthSpinner;
    private JCheckBox uppercaseCheck;
    private JCheckBox lowercaseCheck;
    private JCheckBox numbersCheck;
    private JCheckBox symbolsCheck;
    private JTextField passwordField;
    private JLabel strengthLabel;

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";

    public void createAndShowGUI() {
        frame = new JFrame("Password Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 300);
        frame.setLayout(new BorderLayout());

        JPanel optionsPanel = new JPanel(new GridLayout(5, 2));

        optionsPanel.add(new JLabel("Password Length:"));
        lengthSpinner = new JSpinner(new SpinnerNumberModel(8, 4, 128, 1));
        optionsPanel.add(lengthSpinner);

        uppercaseCheck = new JCheckBox("Include Uppercase Letters", true);
        optionsPanel.add(uppercaseCheck);

        lowercaseCheck = new JCheckBox("Include Lowercase Letters", true);
        optionsPanel.add(lowercaseCheck);

        numbersCheck = new JCheckBox("Include Numbers", true);
        optionsPanel.add(numbersCheck);

        symbolsCheck = new JCheckBox("Include Symbols", false);
        optionsPanel.add(symbolsCheck);

        frame.add(optionsPanel, BorderLayout.CENTER);

        JPanel generatePanel = new JPanel(new FlowLayout());
        JButton generateButton = new JButton("Generate Password");
        generatePanel.add(generateButton);

        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        generatePanel.add(passwordField);

        JButton copyButton = new JButton("Copy to Clipboard");
        generatePanel.add(copyButton);

        frame.add(generatePanel, BorderLayout.SOUTH);

        JPanel strengthPanel = new JPanel(new FlowLayout());
        strengthLabel = new JLabel("Password Strength: ");
        strengthPanel.add(strengthLabel);
        frame.add(strengthPanel, BorderLayout.NORTH);

        generateButton.addActionListener(e -> generatePassword());
        copyButton.addActionListener(e -> copyToClipboard(passwordField.getText()));

        frame.setVisible(true);
    }

    private void generatePassword() {
        int length = (int) lengthSpinner.getValue();
        boolean useUppercase = uppercaseCheck.isSelected();
        boolean useLowercase = lowercaseCheck.isSelected();
        boolean useNumbers = numbersCheck.isSelected();
        boolean useSymbols = symbolsCheck.isSelected();

        if (!useUppercase && !useLowercase && !useNumbers && !useSymbols) {
            JOptionPane.showMessageDialog(frame, "Please select at least one character type!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder charPool = new StringBuilder();
        if (useUppercase) charPool.append(UPPERCASE);
        if (useLowercase) charPool.append(LOWERCASE);
        if (useNumbers) charPool.append(NUMBERS);
        if (useSymbols) charPool.append(SYMBOLS);

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        String generatedPassword = password.toString();
        passwordField.setText(generatedPassword);
        updatePasswordStrength(generatedPassword);
    }

    private void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(text);
        clipboard.setContents(transferable, null);
        JOptionPane.showMessageDialog(frame, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        String strengthText;
        if (strength <= 2) {
            strengthText = "Weak";
        } else if (strength <= 4) {
            strengthText = "Moderate";
        } else {
            strengthText = "Strong";
        }
        strengthLabel.setText("Password Strength: " + strengthText);
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()-_=+{}|:,.<>?/`~].*")) strength++;
        if (password.length() >= 12) strength++;
        return strength;
    }
}
