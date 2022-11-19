package com.thank267.game.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.thank267.game.listeners.MyContactListner;

import java.util.Iterator;

public class PhysX {
    public final MyContactListner contactListner;
    public static final float PPM = 100;
    final World world;
    private final Box2DDebugRenderer debugRenderer;

    public PhysX() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        contactListner = new MyContactListner();
        world.setContactListener(contactListner);
    }

    public void destroyBody(Body body){world.destroyBody(body);}
    public Array<Body> getBodys(String name) {
        Array<Body> tmp = new Array<>();
        world.getBodies(tmp);
        Iterator<Body> it = tmp.iterator();
        while (it.hasNext()){
            Body body = it.next();
            if (!body.getUserData().equals(name)) it.remove();
        }
        return tmp;
    }
    public Body addObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if (type.equals("StaticBody")) def.type = BodyDef.BodyType.StaticBody;
        if (type.equals("DynamicBody")) def.type = BodyDef.BodyType.DynamicBody;

        def.position.set((rect.x + rect.width/2)/PPM, (rect.y + rect.height/2)/PPM);
        def.gravityScale = (float) object.getProperties().get("gravityScale");

        polygonShape.setAsBox(rect.width/2/PPM, rect.height/2/PPM);

        fdef.shape = polygonShape;
        fdef.friction = (float) object.getProperties().get("friction");
        fdef.density = (float) object.getProperties().get("density");;
        fdef.restitution = (float) object.getProperties().get("restitution");

        String name = "";
        if (object.getName() != null) name = object.getName();
        Body body;
        body = world.createBody(def);
        body.setUserData(name);
        body.createFixture(fdef).setUserData(name);

        if (name.equals("Hero")) {
            polygonShape.setAsBox(rect.width/3/PPM, rect.height/10/PPM, new Vector2(0, -rect.width/2), 0);
            body.createFixture(fdef).setUserData("legs");
            body.getFixtureList().get(1).setSensor(true);
        }

        polygonShape.dispose();
        return body;
    }
    public void addDmgObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        def.type = BodyDef.BodyType.StaticBody;
        def.position.set((rect.x + rect.width/2)/PPM, (rect.y + rect.height/2)/PPM);
        polygonShape.setAsBox(rect.width/2/PPM, rect.height/2/PPM);
        fdef.shape = polygonShape;


        String name = "damage";
        Body body;
        body = world.createBody(def);
        body.setUserData(name);
        body.createFixture(fdef).setUserData(name);
        body.getFixtureList().get(0).setSensor(true);
        polygonShape.dispose();
    }

    public Body addBullet(float x, float y) {
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        polygonShape.setAsBox(2/PPM, 2/PPM);
        fdef.shape = polygonShape;
        Body body;
        body = world.createBody(def);
        body.setUserData("bullet");
        body.createFixture(fdef).setUserData("bullet");
        body.getFixtureList().get(0).setSensor(true);
        polygonShape.dispose();
        return body;
    }
    public void debugDraw(OrthographicCamera camera){debugRenderer.render(world, camera.combined);}
    public void step(){world.step(1/60f, 3, 3);}

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }
}
