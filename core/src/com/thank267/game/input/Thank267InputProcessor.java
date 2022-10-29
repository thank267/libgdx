package com.thank267.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Thank267InputProcessor implements InputProcessor {
    private Vector2 outForce;

    public Thank267InputProcessor(){
        outForce = new Vector2();
    }

    public Vector2 getVector()  {return outForce;}

    @Override
    public boolean keyDown(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "LEFT": outForce.add(-0.25f, 0); break;
            case  "RIGHT": outForce.add(0.25f, 0); break;
            case  "DOWN": outForce.add(0, -1.5f); break;
            case  "SPACE": outForce.add(0, 10.5f); break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "LEFT": outForce.set(0, outForce.y); break;
            case  "RIGHT": outForce.set(0, outForce.y); break;
            case  "DOWN": outForce.set(outForce.x, 0); break;
            case  "SPACE": outForce.set(outForce.x, 0); break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
