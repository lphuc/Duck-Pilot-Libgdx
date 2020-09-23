package com.phucle.fadu.screens;

import com.badlogic.gdx.Screen;
import com.phucle.fadu.game.DupiAssets;

/**
 * Created by Phuc Engineer on 10/8/2018.
 * <p>
 * The application life cycle in Libgdx is a set of distinct system states
 * They are: create() -  resize() - render() - pause() - resume() - dispose()
 */
public abstract class AbstractGameScreen implements Screen {
    protected com.phucle.fadu.screens.DirectedGame game;
    public static boolean networkConnected;

    AbstractGameScreen(DirectedGame game) {
        this.game = game;
    }

    public abstract void render(float delta);

    public abstract void resize(int width, int height);

    public abstract void show();

    public abstract void hide();

    public abstract void pause();

    public void resume() {
//        DupiAssets.getInstance().init();
    }


    public void dispose() {
        DupiAssets.getInstance().dispose();
    }

}
