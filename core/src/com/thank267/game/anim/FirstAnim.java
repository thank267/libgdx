package com.thank267.game.anim;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FirstAnim {

	TextureAtlas atlas;
	Animation<TextureAtlas.AtlasRegion> anm;
	private float time;

	public FirstAnim(String atlas, String name, float fps, Animation.PlayMode playMode){
		time = 0;
		this.atlas = new TextureAtlas(atlas);
		anm = new Animation<>(1/fps, this.atlas.findRegions(name));
		anm.setPlayMode(playMode);
	}

	public TextureRegion draw() {return anm.getKeyFrame(time);}
	public void setTime(float dT){time += dT;}

	public void dispose(){
		this.atlas.dispose();
	}
}
