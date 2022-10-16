package com.hank267.game.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.stream.Stream;

public class FirstAnim {

	Texture img;
	Animation<TextureRegion> anm;
	private float time;

	public FirstAnim(String name, int row, int col, float fps, Animation.PlayMode playMode){
		time = 0;
		img = new Texture(name);
		TextureRegion reg1 = new TextureRegion(img);
		TextureRegion[][] regions = reg1.split(img.getWidth()/col, img.getHeight()/row);
//		TextureRegion[] tmp = new TextureRegion[regions.length*regions[0].length];
//		int cnt = 0;
//		for (int i = 0; i < regions.length; i++) {
//			for (int j = 0; j < regions[0].length; j++) {
//				tmp[cnt++] = regions[i][j];
//			}
//		}
		TextureRegion[] tmp = Stream.of(regions).flatMap(Stream::of).toArray(TextureRegion[]::new);
		anm = new Animation<>(1/fps, tmp);
		anm.setPlayMode(playMode);
	}

	public TextureRegion draw() {return anm.getKeyFrame(time);}
	public void setTime(float dT){time += dT;}

	public void dispose(){
		this.img.dispose();
	}
}
