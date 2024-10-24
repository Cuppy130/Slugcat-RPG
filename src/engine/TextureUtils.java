package engine;

import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;

public class TextureUtils {
    public static int loadTexture(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("Texture file not found: " + path);
                return -1;
            }

            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            // Create a byte buffer for the texture data
            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }
            buffer.flip(); // Flip the buffer to prepare it for OpenGL

            // Generate and bind a texture ID
            int textureID = GL11.glGenTextures();
            if (textureID == 0) {
                System.err.println("Failed to generate texture ID for " + path);
                return -1;
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // Set texture parameters
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            // Upload the texture data to OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Optionally generate mipmaps
            // GL11.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            // Unbind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            // Return the generated texture ID
            return textureID;
        } catch (Exception e) {
            System.err.println("Failed to load texture from " + path);
            e.printStackTrace();
            return -1;
        }
    }
}
