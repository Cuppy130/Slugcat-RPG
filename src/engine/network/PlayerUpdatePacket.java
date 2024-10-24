package engine.network;

import engine.entity.Player;

public class PlayerUpdatePacket extends PlayerJoinPacket {
    private static final long serialVersionUID = 1L;

    public PlayerUpdatePacket(Player player){
        super(player);
    }

    public void set(Player player) {
        player.color = color;
        player.UUID = UUID;
        player.direction = direction;
        player.maxHealth = maxHealth;
        player.health = health;
        player.chunkX = cx;
        player.chunkY = cy;
        player.x = x;
        player.y = y;
        player.isMoving = isMoving;
        player.level = level;
        player.name = name;

    }
}
