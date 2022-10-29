package com.thank267.game.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FirstAnim {

	TextureAtlas atlas;
	Animation<TextureAtlas.AtlasRegion> anm;
	private float time;
	private Sound sound;
	private boolean loop;
	private float d;

	public FirstAnim(String atlas, String name, float fps, boolean playMode, String sound){
		if (playMode) loop = true;
		this.sound = Gdx.audio.newSound(Gdx.files.internal(sound));
		this.sound.play();
		time = 0;
		this.atlas = new TextureAtlas(atlas);
		anm = new Animation<>(1/fps, this.atlas.findRegions(name));
		anm.setPlayMode(Animation.PlayMode.NORMAL);
		d = anm.getAnimationDuration()/2;
	}

	public TextureRegion draw() {return anm.getKeyFrame(time);}
	public void setTime(float dT){
		time += dT;
		if (time > d && time < anm.getAnimationDuration()) {
			sound.play();
			d *= 2;
		} else if (time >= anm.getAnimationDuration() && loop) {
			time =0;
			d = anm.getAnimationDuration()/2;
			sound.play();
		}}

	public void dispose(){
		this.atlas.dispose();
	}
}
