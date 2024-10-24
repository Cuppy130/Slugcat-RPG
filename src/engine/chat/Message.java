package engine.chat;

import java.io.Serializable;

import engine.network.GameplayPacket;
import gui.Color;

public class Message implements Serializable {
    public String content = "[DEFAULT]";
    public String username = "[UNKNOWN]";
    public Color chatColor = new Color(0xFFFFFF);

    // Constructor to initialize the message with content and username
    public Message(String content, String username) {
        this.content = content;
        this.username = username;
    }

    // Method to parse incoming byte data into a Message object
    public static void parse(byte[] data) {
        if (data.length < 3 || data[1] != GameplayPacket.Chat) return;
        int messageLen = data[2];
        if (data.length < 3 + messageLen) return;
        StringBuilder content = new StringBuilder();
        for (int i = 3; i < 3 + messageLen; i++) {
            content.append((char) (data[i] & 0xFF));
        }
        int usernameStart = 3 + messageLen;
        if (usernameStart >= data.length) return; // Ensure no out-of-bounds
        int usernameLen = data[usernameStart];
        if (data.length < usernameStart + 1 + usernameLen) return;
        StringBuilder username = new StringBuilder();
        for (int i = usernameStart + 1; i < usernameStart + 1 + usernameLen; i++) {
            username.append((char) (data[i] & 0xFF));
        }
        Chat.messages.add(new Message(content.toString(), username.toString()));
    }

    @Override
    public String toString() {
        return username + ": " + content;
    }
}
