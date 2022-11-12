package com.thank267.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thank267.game.anim.FirstAnim;
import com.thank267.game.enums.Actions;
import com.thank267.game.input.Thank267InputProcessor;
import com.thank267.game.physics.PhysX;

import java.util.HashMap;


public class GameScreen implements Screen {
    Game game;
    private SpriteBatch batch;
    private Texture img;
    private HashMap<Actions, FirstAnim> manAssetss;
    private Music music;
    private Sound sound;
    private Thank267InputProcessor thank267InputProcessor;
    private OrthographicCamera camera;
    private PhysX physX;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Actions actions;
    private int[] front, tL;


    public GameScreen(Game game){
        this.game = game;

        map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        front = new int[1];
        front[0] = map.getLayers().getIndex("front");
        tL = new int[1];
        tL[0] = map.getLayers().getIndex("t0");

        physX = new PhysX();

        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            System.out.println(i);
            physX.addObject(objects.get(i));
        }
        body = physX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);

        thank267InputProcessor = new Thank267InputProcessor();
        Gdx.input.setInputProcessor(thank267InputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("01. Tandava.mp3"));
        music.setVolume(0.25f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("Mouse-Click-03-c-FesliyanStudios.com.mp3"));

        batch = new SpriteBatch();

        manAssetss = new HashMap<>();
        manAssetss.put(Actions.STAND, new FirstAnim("atlas/digger.atlas", "stand", 25, true,"mixkit-striking-jump-rope-2100.mp3"));
        manAssetss.put(Actions.RUN, new FirstAnim("atlas/digger.atlas", "run", 25, true,"mixkit-striking-jump-rope-2100.mp3"));
        actions = Actions.STAND;

        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * physX.PPM;
        camera.position.y = body.getPosition().y * physX.PPM;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {sound.play(100, 1, 0);}

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        body.applyForceToCenter(thank267InputProcessor.getVector(), true);

        if (body.getLinearVelocity().len() < 0.6f) actions = Actions.STAND;
        else if (Math.abs(body.getLinearVelocity().x) > 0.6f) {actions = Actions.RUN;}

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {manAssetss.get(actions).draw().flip(true, false);}
        if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {manAssetss.get(actions).draw().flip(true, false);}

        float x = body.getPosition().x * physX.PPM - 2.5f/camera.zoom;
        float y = body.getPosition().y * physX.PPM - 2.5f/camera.zoom;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(manAssetss.get(actions).draw(), x, y);
        batch.end();

        mapRenderer.render(front);

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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
