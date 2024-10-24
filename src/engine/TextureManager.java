package engine;

import org.lwjgl.opengl.GL11;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextureManager {
    private static List<Integer> textureIDs = new ArrayList<>();

    public static void cleanup() {
        for (int textureID : textureIDs) {
            GL11.glDeleteTextures(textureID);
        }
        textureIDs.clear();
    }

    public static int loadTexture(String path) {
        int textureID = TextureUtils.loadTexture(path);
        if (textureID != -1) {
            textureIDs.add(textureID);
            
            String fileName = new File(path).getName();
            System.out.println("Loaded texture: " + fileName + " with ID: " + textureID);
        } else {
            System.err.println("Failed to load texture: " + path);
        }
        return textureID;
    }

    public static int getTexture(int block){
        return textureIDs.get(block);
    }
}
