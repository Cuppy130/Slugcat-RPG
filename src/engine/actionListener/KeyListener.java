package engine.actionListener;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import engine.Global;
import gui.Color;
import gui.EnterString;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
            if(key==GLFW_KEY_F1){
                Global.DebugMenu = !Global.DebugMenu;
                return;
            } else if(key==GLFW_KEY_F2){
                Global.player.name = EnterString.getString("Change username", "Change username:");
                return;
            } else if(key==GLFW_KEY_F4){
                Global.openFolder(Global.APPDATA);
                return;
            } else if(key==GLFW_KEY_F5){
                Global.player.color = Color.randomRGB();
                return;
            }
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keycode){
        return get().keyPressed[keycode];
    }
}
