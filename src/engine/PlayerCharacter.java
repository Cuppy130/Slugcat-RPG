package engine;

import engine.entity.Player;

public class PlayerCharacter {
    public Model model;
    public PlayerCharacter(){
        float scale = Global.scale;
        model = new Model(new float[]{
            -scale / 2, -scale / 2,
            scale / 2, -scale / 2,
            -scale / 2, scale / 2,
            scale / 2, scale / 2
        }, new float[]{
            0, 0, 1, 0, 0, 1, 1, 1
        }, 1);
    }

    public int left = TextureUtils.loadTexture(Global.ASSETS + "/playerCharacter/left.png");
    public int right = TextureUtils.loadTexture(Global.ASSETS + "/playerCharacter/right.png");
    public int up = TextureUtils.loadTexture(Global.ASSETS + "/playerCharacter/up.png");
    public int down = TextureUtils.loadTexture(Global.ASSETS + "/playerCharacter/down.png");

    public void draw(Player player){
        if(player.direction == 0){
            model.t = left;
        }
        if(player.direction == 2){
            model.t = right;
        }
        if(player.direction == 1 || player.direction == 0.5){
            model.t = up;
        }
        if(player.direction == 3 || player.direction == 3.5){
            model.t = down;
        }
        model.render(player.color);
    }
}
