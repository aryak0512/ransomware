import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarExample {
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton startButton;

    public ProgressBarExample() {
        // Create frame
        frame = new JFrame("Progress Bar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        // Create progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        frame.add(progressBar, BorderLayout.CENTER);

        // Create start button
        startButton = new JButton("Start");
        frame.add(startButton, BorderLayout.SOUTH);

        // Button action listener
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startProgress();
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    private void startProgress() {
        progressBar.setValue(0); // Reset progress bar
        startButton.setEnabled(false); // Disable button during progress

        // Create a new thread to simulate a task
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50); // Simulate work by sleeping
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setValue(i); // Update progress
            }
            startButton.setEnabled(true); // Re-enable button when done
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProgressBarExample::new);
    }
}
