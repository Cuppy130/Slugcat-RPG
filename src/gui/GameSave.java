package gui;

import javax.swing.JOptionPane;

import engine.Global;
import engine.SaveFile;

public class GameSave {
    public static void Show(){
        if(Global.autosave){
            SaveFile.packData(Global.player, Global.saveint);
            return;
        }
        int option = JOptionPane.showConfirmDialog(null, "Save game?", "Save game", 0);
        if(option<1){
            SaveFile.packData(Global.player, Global.saveint);
        }
    }
}
