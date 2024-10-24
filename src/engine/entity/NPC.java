package engine.entity;

import gui.Color;

public class NPC extends Entity {
    public static final long serialVersionUID = 1L;
    public Color color;
    public String name;
    public Interaction interactions;
    public boolean important;
    public NPC(String name, Interaction interactions, boolean important, Color color){
        this.name = name;
        this.interactions = interactions;
        this.important = important;
        this.color = color;
    }
}
