package com.hank267.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hank267.game.anim.FirstAnim;

public class Thank267GdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private FirstAnim anim;

	@Override
	public void create () {
		batch = new SpriteBatch();
		anim = new FirstAnim("pngegg.png", 5, 6, 15, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		anim.setTime(Gdx.graphics.getDeltaTime());

		float x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - (Gdx.input.getY() + anim.draw().getRegionHeight()/2);

		batch.begin();
		batch.draw(anim.draw(), x, y);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		anim.dispose();
	}
}
