package com.thank267.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thank267.game.anim.FirstAnim;
import com.thank267.game.input.Thank267InputProcessor;

public class Thank267GdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private FirstAnim stand, run, tmpA;
	private Sound sound;
	private Music music;
	private Thank267InputProcessor thank267InputProcessor;
	private float x,y;
	private int dir = 0, step =1;

	@Override
	public void create () {
		batch = new SpriteBatch();

		run = new FirstAnim("atlas/digger.atlas", "run", 25, Animation.PlayMode.LOOP);
		stand = new FirstAnim("atlas/digger.atlas", "stand", 25, Animation.PlayMode.LOOP);
		sound = Gdx.audio.newSound(Gdx.files.internal("Mouse-Click-03-c-FesliyanStudios.com.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("01. Tandava.mp3"));

		thank267InputProcessor = new Thank267InputProcessor();
		Gdx.input.setInputProcessor(thank267InputProcessor);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		tmpA = stand;
		dir = 0;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {sound.play(10, 1, 0);}

		if (thank267InputProcessor.getOutString().contains("A")) {
			dir = -1;
			tmpA = run;
		}
		if (thank267InputProcessor.getOutString().contains("D")) {
			dir = 1;
			tmpA = run;
		}
		if (thank267InputProcessor.getOutString().contains("W")) y++;
		if (thank267InputProcessor.getOutString().contains("S")) y--;


		if (dir == -1) x-=step;
		if (dir == 1) x+=step;

		if (!tmpA.draw().isFlipX() & dir == -1) {tmpA.draw().flip(true, false);}
		if (!stand.draw().isFlipX() & dir == -1) {stand.draw().flip(true, false);}
		if (tmpA.draw().isFlipX() & dir == 1) tmpA.draw().flip(true, false);
		if (stand.draw().isFlipX() & dir == 1) stand.draw().flip(true, false);

		tmpA.setTime(Gdx.graphics.getDeltaTime());

		music.setVolume(0.5f);
		music.setLooping(true);
		music.play();

		batch.begin();
		batch.draw(tmpA.draw(), x, y);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stand.dispose();
		run.dispose();
		music.dispose();
		sound.dispose();
	}
}
