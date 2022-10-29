package com.thank267.game.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysX {

    public final float PPM = 100;
    public World world;
    private final Box2DDebugRenderer debugRenderer;


    public PhysX() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    public void debugDraw(OrthographicCamera camera){debugRenderer.render(world, camera.combined);}
    public void step(){world.step(1/60f, 3, 3);}

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
        fdef.density = (float) object.getProperties().get("density");
        fdef.restitution = (float) object.getProperties().get("restitution");

        String name = "";
        if (object.getName() != null) name = object.getName();
        Body body;
        body = world.createBody(def);
        body.setUserData("body");
        body.createFixture(fdef).setUserData(name);
        polygonShape.dispose();
        return body;
    }

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }
}
