package com.thank267.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thank267.game.anim.CoinsAnim;
import com.thank267.game.input.Thank267InputProcessor;
import com.thank267.game.listeners.MyContactListner;
import com.thank267.game.persons.Man;
import com.thank267.game.physics.PhysX;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    Game game;
    private SpriteBatch batch;
    private Music music;
    private Sound sound;
    private Thank267InputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private PhysX physX;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] front, tL;
    private final Man man;
    private final CoinsAnim coinAnm;
    public static List<Body> bodyToDelete;

    public GameScreen(Game game){
        bodyToDelete = new ArrayList<>();
        coinAnm = new CoinsAnim("Full Coinss.png",1,8, 12, Animation.PlayMode.LOOP);
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
            physX.addObject(objects.get(i));
        }
        body = physX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
        man = new Man(body);

        myInputProcessor = new Thank267InputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("mixkit-striking-jump-rope-2100.mp3"));
        music.setVolume(0.02f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("Mouse-Click-03-c-FesliyanStudios.com.mp3"));

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.zoom = 1f;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * physX.PPM;
        camera.position.y = body.getPosition().y * physX.PPM;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        man.setTime(delta);
        Vector2 vector = myInputProcessor.getVector();
        if (MyContactListner.cnt < 1) vector.set(vector.x, 0);
        body.applyForceToCenter(vector, true);
        man.setFPS(body.getLinearVelocity(), true);

        Rectangle tmp = man.getRect(camera, man.getFrame());
        ((PolygonShape)body.getFixtureList().get(0).getShape()).setAsBox(tmp.width/2, tmp.height/2);
        ((PolygonShape)body.getFixtureList().get(1).getShape()).setAsBox(
                tmp.width/3,
                tmp.height/10,
                new Vector2(0, -tmp.height/2),0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(man.getFrame(), tmp.x,tmp.y, tmp.width * PhysX.PPM, tmp.height * PhysX.PPM);

        Array<Body> bodys = physX.getBodys("coin");
        coinAnm.setTime(delta);
        TextureRegion tr = coinAnm.draw();
        float dScale = 2f;
        for (Body bd: bodys) {
            float cx = bd.getPosition().x * PhysX.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * PhysX.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / PhysX.PPM / dScale;
            float cH = tr.getRegionHeight() / PhysX.PPM / dScale;
            ((PolygonShape)bd.getFixtureList().get(0).getShape()).setAsBox(cW/2, cH/2);
            batch.draw(tr, cx,cy, cW * PhysX.PPM, cH * PhysX.PPM);
        }
        batch.end();

        mapRenderer.render(front);

        for (Body bd: bodyToDelete) {physX.destroyBody(bd);}
        bodyToDelete.clear();

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
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
