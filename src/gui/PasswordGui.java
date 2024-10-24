package gui;

import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import engine.Global;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordGui {
    public static String popup() {
        JFrame frame = new JFrame("Server Password");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Enter password:");
        panel.add(label);
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);
        final String[] password = new String[1];
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password[0] = new String(passwordField.getPassword());
                frame.dispose();
            }
        });
        frame.add(panel);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setAutoRequestFocus(true);
        frame.setLocationRelativeTo(null);
        glfwHideWindow(Global.window);
        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        glfwShowWindow(Global.window);

        return password[0];
    }
}
