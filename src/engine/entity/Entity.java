package engine.entity;
import java.io.Serializable;
import java.util.UUID;
public class Entity implements Serializable {
    public UUID UUID;
    public float x = 0;
    public float y = 0;
    public int health = 100;
    public int maxHealth = 100;
    public boolean canAttack = false;
    public Entity(){
        this.UUID = UUID.randomUUID();
    }
}
