package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterString {

    private static String input = null;

    public static String getString(String title, String text) {
        // Create the frame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 120);
        frame.setLayout(new FlowLayout());
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Create the label
        JLabel label = new JLabel(text);

        // Create the text field
        JTextField textField = new JTextField(15);

        // Create the button
        JButton submitButton = new JButton("Submit");

        // Add an action listener to the button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = textField.getText();
                frame.dispose(); // Close the window
            }
        });

        // Add components to the frame
        frame.add(label);
        frame.add(textField);
        frame.add(submitButton);

        // Make the frame visible
        frame.setVisible(true);

        // This will block the current thread until the window is closed
        while (frame.isVisible()) {
            try {
                Thread.sleep(100);  // Wait for the user to submit
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return input;  // Return the input after the frame is closed
    }
}
