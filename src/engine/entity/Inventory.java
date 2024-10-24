package engine.entity;

import java.io.Serializable;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    public byte[] manager;
    public int size;

    // Constructor
    public Inventory(int size) {
        this.size = size;
        manager = new byte[size * 4]; // Each item slot uses 4 bytes (2 for itemID, 2 for itemCount)
    }

    public void setItem(int itemSlot, int itemID, int itemCount) {
        int index = itemSlot * 4;
        manager[index] = (byte) (itemID >> 8);     // High byte of itemID
        manager[index + 1] = (byte) (itemID);      // Low byte of itemID
        manager[index + 2] = (byte) (itemCount >> 8);  // High byte of itemCount
        manager[index + 3] = (byte) (itemCount);       // Low byte of itemCount
    }

    public int getItemID(int itemSlot) {
        int index = itemSlot * 4;
        return ((manager[index] & 0xFF) << 8) | (manager[index + 1] & 0xFF);
    }

    public int getItemCount(int itemSlot) {
        int index = itemSlot * 4;
        return ((manager[index + 2] & 0xFF) << 8) | (manager[index + 3] & 0xFF);
    }
}
