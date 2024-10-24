package gui;

import java.io.Serializable;

import org.joml.Random;

public class Color implements Serializable {
    private static final long serialVersionUID = 1L;  // Optional, but recommended for version control of the serialized class

    public int value;

    // Constructor for ARGB color with full opacity by default
    public Color(int value) {
        this.value = value;
    }

    // Constructor for RGB color, defaulting alpha to full opacity (255)
    public Color(int r, int g, int b) {
        this(r, g, b, 255); // Default alpha to 255 (full opacity)
    }

    // Constructor for ARGB color
    public Color(int r, int g, int b, int a) {
        // Ensure values are in range 0-255
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 || a < 0 || a > 255) {
            throw new IllegalArgumentException("RGB and Alpha values must be between (0 - 255)");
        }
        // Shift and combine to create ARGB value
        this.value = ((a << 24) | (r << 16) | (g << 8) | b);
    }

    // Example method to retrieve the ARGB value
    public int getValue() {
        return this.value;
    }

    // Additional helper methods, e.g., for getting individual components
    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return value & 0xFF;
    }

    public int getAlpha() {
        return (value >> 24) & 0xFF;
    }

    @Override
    public String toString() {
        return "Color {" + 
        "red=" + getRed() +
        ", green=" + getGreen() +
        ", blue=" + getBlue() + "}";
    }

    public static Color randomRGB(){
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
