package com.thank267.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.thank267.game.persons.Man;

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
            case "LEFT": outForce.add(-0.3f, 0); break;
            case  "RIGHT": outForce.add(0.3f, 0); break;
            case  "UP": outForce.add(0, 5f); break;
            case  "SPACE": Man.isFire = true; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "LEFT": outForce.set(0, outForce.y); break;
            case  "RIGHT": outForce.set(0, outForce.y); break;
            case  "UP": outForce.set(outForce.x, 0); break;
            case  "SPACE": Man.isFire = false; break;

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
