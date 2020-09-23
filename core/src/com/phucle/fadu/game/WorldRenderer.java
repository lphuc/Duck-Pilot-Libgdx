package com.phucle.fadu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.phucle.fadu.utils.Consts;
import com.phucle.fadu.utils.MyCamera;

/*
 * Created by Phuc Engineer on 10/14/2018.
 */
public class WorldRenderer implements Disposable {

    //OrthographicCamera is suitable for 2D projection
    private OrthographicCamera camera;

    // draw objects with respect to the camera's current settings (position, zoom,..) to the screen
    private SpriteBatch batch;
    private com.phucle.fadu.game.WorldUpdater worldUpdater;
    private BitmapFont font40, font38, fontArial, fontTime;
    private TextureRegion lifeOff, lifeOn;
    private TextureRegion iconReady, iconCoinPackage;

    public WorldRenderer(com.phucle.fadu.game.WorldUpdater worldUpdater) {
        this.worldUpdater = worldUpdater;
        init();
        initTutorial();
    }

    private void init() {
        batch = new SpriteBatch();
        camera = MyCamera.getCamera();

        font40 = com.phucle.fadu.game.DupiAssets.getInstance().getFont40();
        font40.getData().setScale(com.phucle.fadu.game.DupiAssets.scaleUnit);
        fontTime = com.phucle.fadu.game.DupiAssets.getInstance().getFontTime();
        fontTime.getData().setScale(com.phucle.fadu.game.DupiAssets.scaleUnit / 2.0f);
        font38 = com.phucle.fadu.game.DupiAssets.getInstance().getFont38();
        font38.getData().setScale(1.2f);
        fontArial = com.phucle.fadu.game.DupiAssets.getInstance().getFontArial();
        fontArial.getData().setScale(com.phucle.fadu.game.DupiAssets.scaleUnit / 1.5f);

        lifeOff = com.phucle.fadu.game.DupiAssets.getEnvironment().getAtlasStatic().findRegion(com.phucle.fadu.utils.Consts.GAME_RESOURCE.LIFE_OFF);
        lifeOn = com.phucle.fadu.game.DupiAssets.getEnvironment().getAtlasStatic().findRegion(Consts.GAME_RESOURCE.LIFE_ON);

    }

    private void initTutorial() {
        TextureAtlas atlas = com.phucle.fadu.game.DupiAssets.getEnvironment().getAtlasStatic();
        iconReady = new TextureRegion(atlas.findRegion("ready"));
        iconCoinPackage = new TextureRegion(atlas.findRegion("coin_package"));
    }

    /**
     * contain the logic to define in which order the game objects are drawn over others.
     */
    public void render() {
        renderWorld(batch);
    }

    private void renderWorld(SpriteBatch batch) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined); // for camera size to take effect

        //begin drawing process
        batch.begin();

        //disable adding translucent pixels for the first (background) texture for better performance
        batch.disableBlending();
        batch.draw(com.phucle.fadu.game.DupiAssets.getEnvironment().getBgRegion(), 0, 0, com.phucle.fadu.game.DupiAssets.screenHeight, com.phucle.fadu.game.DupiAssets.screenWidth);

        //enable adding translucent pixels for other textures to be overlaid the background texture
        batch.enableBlending();

        //draw all game objects
        worldUpdater.getLevel().render(batch);

        //don't draw GUI when game is over
        if (!worldUpdater.isGameOver) {
            renderGUI(batch);
        }

        //end of drawing
        batch.end();
    }

    /**
     * render score & km
     *
     * @param batch
     */
    private void renderGUI(SpriteBatch batch) {
        //render tutorial when launch game but game is not started yet
        if (worldUpdater.gameState == com.phucle.fadu.game.WorldUpdater.GameState.INIT) {
            batch.draw(iconReady, Gdx.graphics.getWidth() / 2 - com.phucle.fadu.game.DupiAssets.scaleUnit * iconReady.getRegionWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - com.phucle.fadu.game.DupiAssets.scaleUnit * iconReady.getRegionHeight(),
                    com.phucle.fadu.game.DupiAssets.scaleUnit * iconReady.getRegionWidth(), com.phucle.fadu.game.DupiAssets.scaleUnit * iconReady.getRegionHeight());
        }

        font40.draw(batch, "" + WorldUpdater.distanceMove,
                com.phucle.fadu.game.DupiAssets.screenHeight / 2 - 50 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                com.phucle.fadu.game.DupiAssets.screenWidth - 10 * com.phucle.fadu.game.DupiAssets.scaleUnit);

        fontArial.draw(batch, "" + worldUpdater.speed + " km/h",
                50 * com.phucle.fadu.game.DupiAssets.scaleUnit, com.phucle.fadu.game.DupiAssets.screenWidth - 20 * com.phucle.fadu.game.DupiAssets.scaleUnit);

        // draw fuel left
        batch.draw(lifeOff,
                5,
                com.phucle.fadu.game.DupiAssets.getEnvironment().groundHeight + 20 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                38 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                300 * com.phucle.fadu.game.DupiAssets.scaleUnit);

        if (worldUpdater.fuelLeft >= 0) {
            batch.draw(lifeOn,
                    5,
                    com.phucle.fadu.game.DupiAssets.getEnvironment().groundHeight + 20 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                    38 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                    worldUpdater.fuelLeft);
        }

        float offsetX = com.phucle.fadu.game.DupiAssets.scaleUnit * iconCoinPackage.getRegionWidth() / 2;
        float offsetY = com.phucle.fadu.game.DupiAssets.scaleUnit * iconCoinPackage.getRegionHeight() / 2;
        if (worldUpdater.visualGold < worldUpdater.gold) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }

        batch.draw(iconCoinPackage, com.phucle.fadu.game.DupiAssets.screenHeight - 110 * com.phucle.fadu.game.DupiAssets.scaleUnit - iconCoinPackage.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit,
                com.phucle.fadu.game.DupiAssets.screenWidth - 120 * com.phucle.fadu.game.DupiAssets.scaleUnit, offsetX, offsetY,
                com.phucle.fadu.game.DupiAssets.scaleUnit * iconCoinPackage.getRegionWidth(),
                com.phucle.fadu.game.DupiAssets.scaleUnit * iconCoinPackage.getRegionHeight(), 1f, 1f, 0);

        fontTime.draw(batch, "" + worldUpdater.visualGold,
                com.phucle.fadu.game.DupiAssets.screenHeight - 100 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                com.phucle.fadu.game.DupiAssets.screenWidth - 120 * com.phucle.fadu.game.DupiAssets.scaleUnit +
                        com.phucle.fadu.game.DupiAssets.scaleUnit * iconCoinPackage.getRegionHeight() / 2 + 5 * DupiAssets.scaleUnit);

    }

    /**
     * whenever the screen size is changed, resize() will spring into action and initiate
     * the required steps to accommodate the new situation
     *
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
//        camera.viewportWidth = (VIEWPORT_HEIGHT / (float)height) * (float)width;
        camera.update();
    }

    /**
     * always call this method to free the allocated memory when it is no longer needed.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

}
