package gui;

import javax.imageio.ImageIO;

import engine.Global;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class FontManager {
    private int textureID;
    private int fontWidth = 16;  // Number of characters per row in the bitmap
    private int fontHeight = 16; // Number of characters per column in the bitmap

    public FontManager() {
        try {
            BufferedImage image = ImageIO.read(new File(Global.ASSETS + "/fonts/andy.png"));
            
            ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4); // Assuming RGBA format
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = image.getRGB(x, y);
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }
            buffer.flip();

            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load font", e);
        }
    }

    public int getTextureID() {
        return textureID;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void write(float x, float y, String text){
        write(x, y, text, 16, new Color(255, 255, 255, 255));
    }

    public void write(float x, float y, String text, Color color){
        write(x, y, text, 16, color);
    }

    public void write(float x, float y, String text, float fontSize){
        write(x, y, text, fontSize, new Color(255, 255, 255, 255));
    }

    public void write(float x, float y, String text, float fontSize, Color color) {

        glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        glBindTexture(GL_TEXTURE_2D, textureID);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int row = (c % fontWidth);
            int col = (c / fontWidth);
            float u = (float) row / fontWidth;
            float v = (float) col / fontHeight;
            float uWidth = 1.0f / fontWidth;
            float vHeight = 1.0f / fontHeight;
            glBegin(GL_QUADS);
            glTexCoord2f(u, v);                     glVertex2f((x + i * fontSize), y);
            glTexCoord2f(u + uWidth, v);            glVertex2f((x + (i + 1) * fontSize), y);
            glTexCoord2f(u + uWidth, v + vHeight);  glVertex2f((x + (i + 1) * fontSize), y + fontSize);
            glTexCoord2f(u, v + vHeight);           glVertex2f((x + i * fontSize), y + fontSize);

            glEnd();
        }
    }

}
