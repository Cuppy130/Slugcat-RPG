package engine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import engine.entity.Player;
import gui.Color;

import java.io.*;
import java.util.UUID;

public class SaveFile {
    public static void packData(Player player, int saveFile) {
        Path saveFilePath = Paths.get(Global.PROFILES, "profile" + saveFile + ".dat");
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(saveFilePath)))) {
            dos.writeUTF(player.name);
            dos.writeUTF(player.UUID.toString());
            dos.writeLong(pack(player));
            dos.writeInt(player.color.value);
        } catch (IOException e) {
            System.err.println("Error writing save file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Player unpackData(int saveFile) {
        Path saveFilePath = Paths.get(Global.PROFILES, "profile" + saveFile + ".dat");
        Player player = new Player();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(Files.newInputStream(saveFilePath)))) {
            String name = dis.readUTF();
            String uuid = dis.readUTF();
            player = unpack(dis.readLong());
            player.name = name;
            player.UUID = UUID.fromString(uuid);
            player.color = new Color(dis.readInt());
        } catch (IOException e) {
            System.err.println("Error reading save file: " + e.getMessage());
            e.printStackTrace();
        }
        return player;
    }
    public static Long pack(Player player) {
        LongBuffer buffer = new LongBuffer();
        buffer.writeBool(player.hasDied, player.completedTutorial);
        buffer.writeInt8(player.health, player.chunkX, player.chunkY, (int) clamp(player.x, -128, 127), (int) clamp(player.y, -128, 127), player.level); // Clamp x and y to fit into 8-bit range
        buffer.writeInt4(player.mana);
        return buffer.value();
    }
    public static Player unpack(Long hash) {
        Player player = new Player();
        LongBuffer buffer = new LongBuffer(hash);

        player.hasDied = buffer.readBool();
        player.completedTutorial = buffer.readBool();
        player.health = buffer.readInt8();
        player.chunkX = buffer.readInt8();
        player.chunkY = buffer.readInt8();
        player.x = buffer.readInt8(); // Read x as int8 (8 bits)
        player.y = buffer.readInt8(); // Read y as int8 (8 bits)
        player.level = buffer.readInt8();
        player.mana = buffer.readInt4();
        return player;
    }
    private static float clamp(float value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
