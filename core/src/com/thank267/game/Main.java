package com.thank267.game;

import com.badlogic.gdx.Game;
import com.thank267.game.screens.MenuScreen;

public class Main extends Game {

    @Override
    public void create() {setScreen(new MenuScreen(this));}

    @Override
    public void render() {super.render();}

}
