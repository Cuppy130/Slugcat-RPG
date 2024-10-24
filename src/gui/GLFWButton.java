package gui;

import org.lwjgl.opengl.GL11;
import engine.actionListener.MouseListener;
import main.Main;

import java.util.function.Consumer;

public class GLFWButton {
    private float x, y, width, height;
    private float anchorPointX, anchorPointY;
    private float[] color;
    private String text;
    
    // Variables for debounce
    private long lastClickTime = 0; // Track the last click time
    private final long debounceDelay; // Milliseconds for debounce
    private Consumer<GLFWButton> onClick; // Callback for button click

    private boolean hidden = false;

    // Constructor with default anchor points
    public GLFWButton(float x, float y, float width, float height, long debounceDelay) {
        this(x, y, width, height, 0, 0, "", debounceDelay);
    }

    public GLFWButton(float x, float y, float width, float height, String text, long debounceDelay) {
        this(x, y, width, height, 0, 0, text, debounceDelay);
    }

    // Constructor with custom anchor points and debounce
    public GLFWButton(float x, float y, float width, float height, float anchorX, float anchorY, String text, long debounceDelay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.anchorPointX = anchorX;
        this.anchorPointY = anchorY;
        this.color = new float[]{1.0f, 1.0f, 1.0f, 1.0f}; // Default color is white
        this.text = text;
        this.debounceDelay = debounceDelay; // Set the debounce delay
        // Adjust position based on anchor points
        adjustPosition();
    }

    // Adjust the position of the button based on anchor points
    private void adjustPosition() {
        this.x -= width * anchorPointX;
        this.y -= height * anchorPointY;
    }

    // Check if the button is clicked
    public void update() {

        if(hidden) return;

        if (isClicked()) {
            long currentTime = System.currentTimeMillis();
            // Check if the debounce time has passed
            if (currentTime - lastClickTime > debounceDelay) {
                lastClickTime = currentTime; // Update last click time
                if (onClick != null) {
                    onClick.accept(this); // Call the callback function
                }
            }
        }
    }

    // Check if a point is inside the button bounds
    private boolean isClicked() {
        // Get mouse coordinates
        float mx = MouseListener.getX();
        float my = MouseListener.getY();

        // Check if the left mouse button is pressed
        boolean mouseLeftClick = MouseListener.mouseButtonDown(0);

        // Check if the mouse position is within the button's bounds
        return mouseLeftClick && (mx >= x && mx <= x + width) && (my >= y && my <= y + height);
    }

    // Set the color of the button (RGBA format)
    public void setColor(float r, float g, float b, float a) {
        this.color = new float[]{r, g, b, a};
    }

    // Draw the button using OpenGL
    public void draw() {
        if(hidden) return;
        // Enable 2D texturing (if using textures, otherwise for simple color quads, this can be omitted)
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Set the button's color
        GL11.glColor4f(color[0], color[1], color[2], color[3]);

        // Draw the button as a quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);                 // Top-left
        GL11.glVertex2f(x + width, y);         // Top-right
        GL11.glVertex2f(x + width, y + height); // Bottom-right
        GL11.glVertex2f(x, y + height);        // Bottom-left
        GL11.glEnd();

        // Re-enable texturing (if disabled above)
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Main.font.write(x, y, text, 50, new Color(255, 72, 255/2, 255));
    }

    // Set the callback function to be called on button click
    public void setOnClick(Consumer<GLFWButton> onClick) {
        this.onClick = onClick;
    }

    // Setters for position and size (if needed)
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        adjustPosition();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        adjustPosition();
    }

    // Getters for position and size
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void hide(){
        hidden = true;
    }

    public void unhide(){
        hidden = false;
    }

    @Override
    public String toString() {
        return "GLFWButton {" +
                "x=" + getX() +
                "y=" + getY() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                ", isClicked=" + isClicked();
    }
}
