package com.phucle.fadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.phucle.fadu.DupiMain;
import com.phucle.fadu.game.DupiAssets;
import com.phucle.fadu.game.WorldRenderer;
import com.phucle.fadu.game.WorldUpdater;
import com.phucle.fadu.screens.transitions.ScreenTransition;
import com.phucle.fadu.screens.transitions.ScreenTransitionFade;
import com.phucle.fadu.utils.MyDialog;

/*
 * Created by lhphuc on 10/15/2018.
 */
public class MainGameScreen extends AbstractGameScreen {
    private WorldUpdater worldUpdater;
    private WorldRenderer worldRenderer;
    private boolean paused;

    //for disable input while opening dialog
    public static boolean disableInput;
    private TextureAtlas textureAtlas;
    private com.phucle.fadu.utils.MyDialog dialog;
    private Skin dialogSkin;

    //for drawing game over stage on top of the main game screen
    private Stage stage;
    private Image screenBg;
    private Skin skin, skin2, skin3;
    private TextureAtlas atlas;
    private ImageButton btnTryAgain, btnMenu, btnRate;
    private Label.LabelStyle labelStyle;
    private Label labelScore, labelBest;
    private int score, bestScore;

    MainGameScreen(DirectedGame game) {
        super(game);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        disableInput = false;
        init();
        initGameOver();
    }

    private void init() {
        textureAtlas = DupiAssets.getEnvironment().getAtlasStatic();

        Window.WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(),
                Color.BLACK, new TextureRegionDrawable(textureAtlas.findRegion("window_video")));

        ImageButton.ImageButtonStyle closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("window-1-close-button"));

        ImageButton.ImageButtonStyle playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("btn_life"));

        dialogSkin = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        dialog = new MyDialog("", dialogSkin, "dialog");
        dialog.setStyle(windowStyle);

        ImageButton btnOpenVideo = new ImageButton(playButtonStyle);
        btnOpenVideo.getImage().scaleBy(DupiAssets.scaleUnit);
        btnOpenVideo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                DupiMain.iAdvertisement.showVideoRewardAd(MainGameScreen.this);
                disableInput = false;
            }
        });

        Button btnClose = new ImageButton(closeButtonStyle);
        btnClose.scaleBy(DupiAssets.scaleUnit);
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                worldUpdater.openGameOverView();
                disableInput = false;

                DupiMain.iAdvertisement.showInterstitialAd();
            }
        });
        dialog.button(btnOpenVideo);
        dialog.getButtonTable().padBottom(10 * DupiAssets.scaleUnit).padRight(btnOpenVideo.getWidth() * DupiAssets.scaleUnit);
        dialog.getTitleTable().add(btnClose);
    }

    private void initGameOver() {
        // temporary until we have asset manager in
        skin = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        skin2 = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        skin3 = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));

        atlas = DupiAssets.getEnvironment().getAtlasStatic();
        screenBg = new Image(atlas.findRegion("bg_over"));

        // create button Play Again
        btnTryAgain = new ImageButton(skin);
        btnTryAgain.setSize(DupiAssets.sizeUnit * 6, DupiAssets.sizeUnit * 3);
        btnTryAgain.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_try_again_text"));
        btnTryAgain.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_try_again_text"));
        btnTryAgain.setPosition(DupiAssets.screenHeight / 2 - btnTryAgain.getWidth() / 2, DupiAssets.screenWidth / 2 - btnTryAgain.getHeight());

        btnTryAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetOverActors();
                worldUpdater.restartGame();
            }
        });

        // create button Go back to menu
        btnMenu = new ImageButton(skin2);
        btnMenu.setSize(DupiAssets.sizeUnit * 5, DupiAssets.sizeUnit * 2.5f);
        btnMenu.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_menu_text"));
        btnMenu.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_menu_text"));
        btnMenu.setPosition(DupiAssets.screenHeight / 2 - btnMenu.getWidth() / 2,
                DupiAssets.screenWidth / 2 - btnTryAgain.getHeight() * 2);

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenTransition transition = ScreenTransitionFade.init(0.05f);
                game.setScreen(new MenuScreen(game), transition);
            }

        });

        // create button Go back to menu
        btnRate = new ImageButton(skin3);
        btnRate.setSize(DupiAssets.sizeUnit * 5, DupiAssets.sizeUnit * 2.5f);
        btnRate.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_rate_text"));
        btnRate.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_rate_text"));
        btnRate.setPosition(DupiAssets.screenHeight / 2 - btnRate.getWidth() / 2,
                DupiAssets.screenWidth / 2 - btnTryAgain.getHeight() * 3);

        btnRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.phucle.dupi");
            }

        });

        // Display score
        labelStyle = new Label.LabelStyle();
        labelStyle.font = DupiAssets.getInstance().getFontImpact();

        score = com.phucle.fadu.utils.MySettings.getInstance().getPrefs().getInteger(com.phucle.fadu.utils.MySettings.STAGE);
        bestScore = com.phucle.fadu.utils.MySettings.getInstance().getPrefs().getInteger(com.phucle.fadu.utils.MySettings.MAX_SCORE);

        labelScore = new Label("Score  " + score, labelStyle);
        labelScore.setFontScale(DupiAssets.scaleUnit);
        labelScore.setColor(Color.YELLOW);
        labelScore.setPosition(DupiAssets.screenHeight / 2 - labelScore.getWidth() * DupiAssets.scaleUnit / 2,
                DupiAssets.screenWidth - labelScore.getHeight() * 1.5f * DupiAssets.scaleUnit);

        labelBest = new Label("Best  " + bestScore, labelStyle);
        labelBest.setFontScale(com.phucle.fadu.game.DupiAssets.scaleUnit);
        labelBest.setColor(Color.YELLOW);
        labelBest.setPosition(com.phucle.fadu.game.DupiAssets.screenHeight / 2 - labelScore.getWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit / 2,
                com.phucle.fadu.game.DupiAssets.screenWidth - labelScore.getHeight() * 2.5f * com.phucle.fadu.game.DupiAssets.scaleUnit);
    }

    @Override
    public void render(float delta) {
        //print out current fps
        com.phucle.fadu.game.DupiAssets.getInstance().getFpsLogger().log();

        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed since last rendered frame.
            worldUpdater.update(delta);
        }

        //set the screen pure color to black
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the screen pure color to Cornflower Blue
        ///Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f,0xff / 255.0f);
        // Clears the screen
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();

        /**
         * Only draw game over stage on top of main game when game is over or dialog ad is opening
         */
        if (worldUpdater.isGameOver || worldUpdater.isOpenAdDialog) {
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);

        // Make the background fill the screen
        stage.getViewport().update(width, height, true);
        screenBg.setSize(width, height);
    }

    @Override
    public void show() {
//        MySettings.instance.load();
        worldUpdater = new WorldUpdater(this);
        worldRenderer = new WorldRenderer(worldUpdater);
        Gdx.input.setCatchBackKey(true);
//        AudioManager.getInstance().play("sounds/main_music.mp3");
    }

    public void addOverActors() {
        dialog.remove();

        stage.addActor(screenBg);
        stage.addActor(btnTryAgain);
        stage.addActor(btnMenu);
        stage.addActor(btnRate);
        stage.addActor(labelScore);
        stage.addActor(labelBest);

        // update new score
        score = com.phucle.fadu.utils.MySettings.getInstance().getPrefs().getInteger(com.phucle.fadu.utils.MySettings.STAGE);
        bestScore = com.phucle.fadu.utils.MySettings.getInstance().getPrefs().getInteger(com.phucle.fadu.utils.MySettings.MAX_SCORE);

        labelScore.setText("Score  " + score);
        labelBest.setText("Best  " + bestScore);
    }

    /**
     * remove game over actors to put the dialog
     */
    public void resetOverActors() {
        btnTryAgain.remove();
        btnMenu.remove();
        btnRate.remove();
        screenBg.remove();
        labelBest.remove();
        labelScore.remove();
    }

    @Override
    public void hide() {
        worldUpdater.resetScene();
        worldRenderer.dispose();
        dialog.clear();
        dialogSkin.dispose();

        skin.dispose();
        skin2.dispose();
        skin3.dispose();
        screenBg.clear();
        labelScore.clear();
        stage.dispose();
        btnMenu.clear();
        btnTryAgain.clear();
        btnRate.clear();
        labelBest.clear();

        Gdx.input.setCatchBackKey(false);
//        DupiMain.iAdvertisement.destroy();
//        AudioManager.getInstance().stopMusic();
    }

    @Override
    public void pause() {
        paused = true;
        DupiMain.iAdvertisement.pause();
//        AudioManager.getInstance().pause();
    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
        DupiMain.iAdvertisement.resume();
//        AudioManager.getInstance().resume();
    }

    public void openAdConfirmDialog() {
        disableInput = true;
        dialog.show(stage);
    }

    public void endGame() {
        worldUpdater.openGameOverView();
    }

    public void resumeCurrentGameState() {
        worldUpdater.resumeCurrentGame();
    }

}
