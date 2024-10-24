package gui;

import javax.swing.*;

public class Credits {
    public static void show(){
        
        JFrame frame = new JFrame("Credits");
        frame.setSize(300, 150);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JLabel[] credits = new JLabel[] {
            new JLabel("Sunter - Emotional Support/Cutscene art"),
            new JLabel("Inventor - Story writer/Concept art"),
            new JLabel("Sir Apple The First And Second - Battle points"),
            new JLabel("Tennyballs - Lead Programmer")};
        frame.add(panel);
        for(JLabel credit : credits){
            panel.add(credit);
        }
        frame.setVisible(true);
    }
}
