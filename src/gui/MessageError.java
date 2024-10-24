package gui;

import javax.swing.JOptionPane;

public class MessageError {

    // Display an error message in a dialog box
    public static void showError(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    // Display a generic error message
    public static void showError(String message) {
        showError(message, "Error");
    }
}