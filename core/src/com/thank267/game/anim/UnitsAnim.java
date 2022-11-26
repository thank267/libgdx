package com.thank267.game.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class UnitsAnim {
    Texture img;
    Animation<TextureRegion> anm;
    private float time;

    public UnitsAnim(String name, int row, int col, float fps, Animation.PlayMode playMode){
        time = 0;
        img = new Texture(name);
        TextureRegion reg1 = new TextureRegion(img);
        TextureRegion[][] regions = reg1.split(img.getWidth()/col, img.getHeight()/row);
        TextureRegion[] tmp = new TextureRegion[regions.length*regions[0].length];
        int cnt = 0;
        for (TextureRegion[] region : regions) {for (TextureRegion reg: region) tmp[cnt++] = reg;}
        anm = new Animation<>(1/fps, tmp);
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw() {return anm.getKeyFrame(time);}
    public void setTime(float dT){time += dT;}

    public void dispose(){
        this.img.dispose();
    }

}
