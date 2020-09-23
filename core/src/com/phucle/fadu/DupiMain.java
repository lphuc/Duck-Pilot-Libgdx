package com.phucle.fadu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.phucle.fadu.screens.DirectedGame;
import com.phucle.fadu.screens.MenuScreen;
import com.phucle.fadu.screens.transitions.ScreenTransition;
import com.phucle.fadu.screens.transitions.ScreenTransitionFade;

/**
 * Created on 3/18/2018.
 * Entry point of game
 * <p>
 * all game scenes should extend this class so that we can easily define the common actions that we want to be applied for all screens
 * This class will set screen for all scenes like menu, main, etc..
 * each scene such as menu, game and  game over needs to extend
 * the ScreenAdapter class that implements the Screen interface
 */
public class DupiMain extends DirectedGame {
    public static com.phucle.fadu.IGooglePlayService iGooglePlayService;
    public static com.phucle.fadu.IAdvertisement iAdvertisement;
    public static boolean isAdvLoaded;

    public DupiMain(IGooglePlayService iGooglePlayService, IAdvertisement iAdvertisement) {
        DupiMain.iGooglePlayService = iGooglePlayService;
        DupiMain.iAdvertisement = iAdvertisement;
        isAdvLoaded = false;
    }

    /**
     * when the game starts, it will always begins with create()
     * this is where the initialization should happen, such as loading asset into memory
     * and creating an initial state of the game world
     */
    @Override
    public void create() {

        /**Set Libgdx log level, remember change to LOG_INFO before publishing game */
        Gdx.app.setLogLevel(Application.LOG_DEBUG);


        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(0.05f);
        setScreen(new MenuScreen(this), transition);
    }
}
