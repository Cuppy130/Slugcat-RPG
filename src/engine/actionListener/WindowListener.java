package engine.actionListener;

import static org.lwjgl.opengl.GL11.*;

public class WindowListener {
    public static void resizeCallback(long windowID, int width, int height){
        if (width == 0 || height == 0) return;
        glViewport(0, 0, width, height);
    }
}
