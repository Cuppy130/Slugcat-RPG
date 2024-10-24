package engine.actionListener;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private float scrollX = 0.0f, scrollY = 0.0f;
    private float xPos = 0.0f, yPos = 0.0f, lastY = 0.0f, lastX = 0.0f;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging = false;

    public static MouseListener get(){
        if(MouseListener.instance==null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = (float) xpos;
        get().yPos = (float) ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE && button < get().mouseButtonPressed.length){
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, float xOffset, float yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static float getX() {
        return get().xPos;
    }
    public static float getY() {
        return get().yPos;
    }
    public static float getDx() {
        return get().lastX - get().xPos;
    }
    public static float getDy() {
        return get().lastY - get().yPos;
    }
    public static float getScrollX(){
        return get().scrollX;
    }
    public static float getScrollY(){
        return get().scrollY;
    }
    public static boolean isDragging(){
        return get().isDragging;
    }
    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        return false;
    }
}
