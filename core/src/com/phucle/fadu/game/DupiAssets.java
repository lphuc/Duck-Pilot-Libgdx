package com.phucle.fadu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Disposable;
import com.phucle.fadu.game.objects.Environment;

/*
 * Created by lhphuc on 10/15/2018.
 */
public class DupiAssets implements Disposable, AssetErrorListener {
    private AssetManager assetManager;
    private static DupiAssets instance;
    private FPSLogger fpsLogger;
    private static Environment environment;
    private BitmapFont font40, font40Small, font32, font38, fontArial, fontImpact, fontTime;
    public static int screenHeight = Gdx.graphics.getWidth();
    public static int screenWidth = Gdx.graphics.getHeight();


    // The general unit for measuring all object size, this will help remains same size in different screen size
    public static float sizeUnit;

    // mutiply with all game objects to scale their size across screens density
    public static float scaleUnit;


    private DupiAssets() {

    }

    public static DupiAssets getInstance() {
        if (instance == null) {
            instance = new DupiAssets();
        }
        return instance;
    }


    public void init() {
        if (assetManager == null) {
            assetManager = new AssetManager();
        }

        assetManager.setErrorListener(this);
        sizeUnit = (float) screenWidth / 20;

        // take width = 720 & height = 1280 standard screen size for scaling on other devices
        scaleUnit = ((float) screenWidth / 720 + (float) screenHeight / 1280) / 2;

        //load all game resources here

        fpsLogger = new FPSLogger();

        font40 = new BitmapFont(Gdx.files.internal("fonts/font-40.fnt"), Gdx.files.internal("fonts/font-40.png"), false);
        font40.setColor(Color.GOLDENROD);

        fontTime = new BitmapFont(Gdx.files.internal("fonts/font-time.fnt"), Gdx.files.internal("fonts/font-time.png"), false);
        fontTime.setColor(Color.WHITE);

        font40Small = new BitmapFont(Gdx.files.internal("fonts/impact-40.fnt"), Gdx.files.internal("fonts/impact-40.png"), false);
        font40Small.setColor(Color.CHARTREUSE);

        font38 = new BitmapFont(Gdx.files.internal("fonts/font-38.fnt"), Gdx.files.internal("fonts/font-38.png"), false);
        font38.setColor(Color.WHITE);

        font32 = new BitmapFont(Gdx.files.internal("fonts/font-32.fnt"), Gdx.files.internal("fonts/font-32.png"), false);
        font32.setColor(Color.TEAL);

        fontArial = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"), Gdx.files.internal("fonts/arial.png"), false);
        fontImpact = new BitmapFont(Gdx.files.internal("fonts/impact-40.fnt"), Gdx.files.internal("fonts/impact-40.png"), false);

        // load texture atlas
        assetManager.load("sounds/hit.mp3", Sound.class);
        assetManager.load("sounds/fuel.ogg", Sound.class);
        assetManager.load("sounds/pickup_coin.ogg", Sound.class);
        assetManager.load("sounds/jump5.wav", Sound.class);
        assetManager.load("particles/boost", ParticleEffect.class);
        assetManager.load("particles/smoke3", ParticleEffect.class);
        assetManager.load("particles/explosion", ParticleEffect.class);

        /*Wait until they are finished loading
        DupiAssets are loaded asynchronously, we need to complete the loading
        before we move on with our code*/
        assetManager.finishLoading();
        environment = new Environment();
    }


    public Sound getPickCoinSound() {
        return assetManager.get("sounds/pickup_coin.ogg", Sound.class);
    }

    public Sound getHitSound() {
        return assetManager.get("sounds/hit.mp3", Sound.class);
    }

    public Sound getFuelSound() {
        return assetManager.get("sounds/fuel.ogg", Sound.class);
    }

    public Sound getJumpSound() {
        return assetManager.get("sounds/jump5.wav", Sound.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }


    public static Environment getEnvironment() {
        return environment;
    }

    public BitmapFont getFont40() {
        return font40;
    }

    public BitmapFont getFont38() {
        return font38;
    }

    public BitmapFont getFont32() {
        return font32;
    }

    public BitmapFont getFontArial() {
        return fontArial;
    }

    public BitmapFont getFontImpact() {
        return fontImpact;
    }

    public BitmapFont getFontTime() {
        return fontTime;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        font32.dispose();
        font38.dispose();
        font40.dispose();
        font40Small.dispose();
        fontTime.dispose();
        fontImpact.dispose();
        fontArial.dispose();
    }

    public FPSLogger getFpsLogger() {
        return fpsLogger;
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error("DUPI ASSET", "Couldn't load asset '" + asset.fileName + "'",
                (Exception) throwable);
    }


}
