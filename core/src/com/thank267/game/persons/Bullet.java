package com.thank267.game.persons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thank267.game.anim.UnitsAnim;
import com.thank267.game.physics.PhysX;

public class Bullet {
    private final Body body;
    private final static float SPD = 10;
    private float time;
    private UnitsAnim fireAnim;

    public Bullet(PhysX physX, float x, float y, int dir, UnitsAnim fireAnim){
        body = physX.addBullet(x, y);
        body.setBullet(true);
        body.setLinearVelocity(new Vector2(SPD*dir, 0));
        body.setGravityScale(0);
        time = 50;
        this.fireAnim = fireAnim;
    }

    public Body update(float dTime){
        time -= dTime;
        fireAnim.setTime(dTime);
        return (time<=0)?body:null;
    }

    public void draw(SpriteBatch batch) {
        if (body.getFixtureList().size > 0) {
            TextureRegion tr = fireAnim.draw();
            float dScale = 15f;

            float cx = body.getPosition().x * PhysX.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = body.getPosition().y * PhysX.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / PhysX.PPM / dScale;
            float cH = tr.getRegionHeight() / PhysX.PPM / dScale;
            System.out.println(body.getFixtureList().size);
            ((PolygonShape)body.getFixtureList().get(0).getShape()).setAsBox(cW/2, cH/2);
            batch.draw(tr, cx,cy, cW * PhysX.PPM, cH * PhysX.PPM);
        }
    }

    public void dispose () {
        fireAnim.dispose();
    }

}
