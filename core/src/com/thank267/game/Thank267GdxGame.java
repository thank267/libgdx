package com.thank267.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thank267.game.anim.FirstAnim;
import com.thank267.game.enums.Actions;
import com.thank267.game.input.Thank267InputProcessor;
import com.thank267.game.physics.PhysX;

import java.util.HashMap;

public class Thank267GdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private FirstAnim stand, run, tmpA;
	private Sound sound;
	private Music music;
	private PhysX physX;
	private Body body;
	private TiledMap map;
	private HashMap<Actions, FirstAnim> manAssetss;
	private Thank267InputProcessor thank267InputProcessor;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Actions actions;

	@Override
	public void create () {

		physX = new PhysX();

		Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
		objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
		for (int i = 0; i < objects.size; i++) {
			physX.addObject(objects.get(i));
		}
		body = physX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
		body.setFixedRotation(true);

		batch = new SpriteBatch();

		run = new FirstAnim("atlas/digger.atlas", "run", 25, true,"mixkit-striking-jump-rope-2100.mp3");
		stand = new FirstAnim("atlas/digger.atlas", "stand", 25, true,"mixkit-striking-jump-rope-2100.mp3");
		sound = Gdx.audio.newSound(Gdx.files.internal("Mouse-Click-03-c-FesliyanStudios.com.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("01. Tandava.mp3"));
		tmpA = stand;

		thank267InputProcessor = new Thank267InputProcessor();
		Gdx.input.setInputProcessor(thank267InputProcessor);

		camera = new OrthographicCamera();

	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.BLACK);

		camera.position.x = body.getPosition().x;
		camera.position.y = body.getPosition().y;
		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
		body.applyForceToCenter(thank267InputProcessor.getVector(), true);

		if (body.getLinearVelocity().len() < 0.6f) actions = Actions.STAND;
		else if (Math.abs(body.getLinearVelocity().x) > 0.6f) {actions = Actions.RUN;}

		manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
		if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {manAssetss.get(actions).draw().flip(true, false);}
		if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {manAssetss.get(actions).draw().flip(true, false);}

		float x = body.getPosition().x - 2.5f/camera.zoom;
		float y = body.getPosition().y - 2.5f/camera.zoom;

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(manAssetss.get(actions).draw(), x, y);
		batch.end();

		Gdx.graphics.setTitle(String.valueOf(body.getLinearVelocity()));
		physX.step();
		physX.debugDraw(camera);
	}
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();
		sound.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}
