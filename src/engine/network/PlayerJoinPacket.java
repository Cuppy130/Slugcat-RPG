package engine.network;

import java.io.Serializable;
import engine.entity.Player;
import gui.Color;
import java.util.UUID;

public class PlayerJoinPacket implements Serializable  {
    private static final long serialVersionUID = 1L;
    public float direction;
    public boolean isMoving;
    public int health;
    public int maxHealth;
    public String name;
    public int level;
    public int id;
    public Color color;
    public UUID UUID;
    
    public float x, y;
    public int cx, cy;

    public float speed;

    public PlayerJoinPacket(Player player){
        direction = player.direction;
        isMoving = player.isMoving;
        health = player.health;
        level = player.level;
        name = player.name;
        maxHealth = player.maxHealth;
        id = player.id;
        color = player.color;
        UUID = player.UUID;
        
        x = player.x; y = player.y;
        cx = player.chunkX; cy = player.chunkY;

        speed = player.speed;

    }

    public Player get(){
        Player player = new Player(name, level);
        player.direction = direction;
        player.isMoving = isMoving;
        player.health = health;
        player.maxHealth = maxHealth;
        player.id = id;
        player.color = color;
        player.UUID = UUID;
        player.x = x; player.y = y;
        player.chunkX = cx; player.chunkY = cy;
        player.speed = speed;
        return player;
    }

    @Override
    public String toString() {
        return "PlayerJoinPacket {" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", id=" + id +
                ", health=" + health +
                "/" + maxHealth +
                ", direction=" + direction +
                ", isMoving=" + isMoving +
                ", color=" + color +
                ", UUID=" + UUID +
                ", position=(" + x + ", " + y + ")" +
                ", chunk=(" + cx + ", " + cy + ")" +
                ", speed=" + speed +
                '}';
    }

}
