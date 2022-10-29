package com.thank267.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.thank267.game.input.Thank267InputProcessor;
import com.thank267.game.physics.PhysX;


public class Thank267GdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private FirstAnim stand, run, tmpA;
	private Sound sound;
	private Music music;
	private PhysX physX;
	private Body body;
	private TiledMap map;
	private Thank267InputProcessor thank267InputProcessor;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private float x,y;
	private int dir = 0, step = 1;


	@Override
	public void create () {

		map = new TmxMapLoader().load("map/map.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		physX = new PhysX();

		BodyDef def = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		def.type = BodyDef.BodyType.StaticBody;
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.restitution = 1;

		MapLayer env = map.getLayers().get("env");
		Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width / 2;
			float h = rect.get(i).getRectangle().height / 2;
			def.position.set(x, y);
			shape.setAsBox(w, h);
			physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");
		}

		def.type = BodyDef.BodyType.DynamicBody;
		env = map.getLayers().get("dyn");
		rect =  env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width / 2;
			float h = rect.get(i).getRectangle().height / 2;
			def.position.set(x, y);
			shape.setAsBox(w, h);
			fdef.density = (float) rect.get(i).getProperties().get("density");;
			fdef.friction = 0;
			fdef.restitution = 1;

			physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");

		}

		env = map.getLayers().get("hero");
		RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("Hero");
		float x = hero.getRectangle().x + hero.getRectangle().width / 2;
		float y = hero.getRectangle().y + hero.getRectangle().height / 2;
		float w = hero.getRectangle().width / 2;
		float h = hero.getRectangle().height / 2;
		def.position.set(x, y);
		shape.setAsBox(w, h);
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.restitution = 1;
		body = physX.world.createBody(def);
		body.createFixture(fdef).setUserData("Kubik");

		shape.dispose();

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
		ScreenUtils.clear(0, 0, 0, 1);

		camera.position.x = body.getPosition().x;
		camera.position.y = body.getPosition().y;
		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		tmpA = stand;
		dir = 0;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {sound.play(100, 1, 0);}

		if (thank267InputProcessor.getOutString().contains("A")) {
			dir = -1;
			tmpA = run;
			body.applyForceToCenter(new Vector2(-10000, 0f), true);
		}
		if (thank267InputProcessor.getOutString().contains("D")) {
			dir = 1;
			tmpA = run;
			body.applyForceToCenter(new Vector2(10000, 0f), true);
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

		float x = body.getPosition().x;
		float y = body.getPosition().y;

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(tmpA.draw(), x, y);
		batch.end();

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
		stand.dispose();
		run.dispose();
		music.dispose();
		sound.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}
