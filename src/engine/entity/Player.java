package engine.entity;

import gui.Color;

public class Player extends Entity {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public String name;
    public int level;
    public transient boolean hasDied = false;
    public transient boolean completedTutorial = false;
    public transient int mana = 0;
    public int chunkX = 0;
    public int chunkY = 0;
    public float speed = 0.025f;
    public transient boolean isOnScreen = true;
    public transient boolean enabled = false;
    public int id = -1;
    public Inventory inventory = new Inventory(8);
    public int facing = 0;
    public Color color;
    public float direction = 0;
    public transient float oldx;
    public transient float oldy;
    public boolean isMoving = false;
    public transient float maxSpeed = 5;

    public Player() {
        this("Player", 1);
        color = Color.randomRGB();
    }

    public Player(String name, int level) {
        super();
        this.name = name;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Player {" +
            "name='" + name + "'" +
            ", level=" + level +
            ", hasDied=" + hasDied +
            ", completedTutorial=" + completedTutorial +
            ", health=" + health +
            ", mana=" + mana +
            ", x=" + x +
            ", y=" + y +
            ", chunkX=" + chunkX +
            ", chunkY=" + chunkY +
            ", UUID=" + UUID.toString() +
            ", id=" + id +
            ", color=" + color +
        '}';
    }

    public void update(float delta) {
        oldx = x;
        oldy = y;
        if(isMoving){
            x -= Math.cos(direction * Math.PI / 2) * speed * delta * maxSpeed;
            y -= Math.sin(direction * Math.PI / 2) * speed * delta * maxSpeed;
        }
        if(x>16){
            x -= 16;
            chunkX++;
        }
        if(x<0){
            x += 16;
            chunkX--;
        }
        if(y>16){
            y -= 16;
            chunkY++;
        }
        if(y<0){
            y += 16;
            chunkY--;
        }
    }

    public void setPosition(float x2, float y2) {
        chunkX = (int) x2 / 16;
        chunkX = (int) y2 / 16;
        x = x2 % 16;
        y = y2 % 16;
    }
}
