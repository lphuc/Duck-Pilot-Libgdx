package com.phucle.fadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.phucle.fadu.DupiMain;
import com.phucle.fadu.game.DupiAssets;
import com.phucle.fadu.game.objects.Shark;
import com.phucle.fadu.screens.transitions.ScreenTransition;
import com.phucle.fadu.screens.transitions.ScreenTransitionFade;
import com.phucle.fadu.utils.NetworkChecker;
import com.phucle.fadu.utils.AudioManager;
import com.phucle.fadu.utils.MyCamera;

/*all common reusable objects will be created here
 * and can be accessed in the ScreenAdapter
 * objects via the passed reference*/
public class MenuScreen extends AbstractGameScreen {

    private Stage stage;
    private TextureRegion screenBg;
    private Image seaBg, title;
    private Skin skin, skin2, skin3;
    private ImageButton btnSignIn, btnNewGame, btnLeaderBoard;
    private TextureAtlas atlas;
    private SpriteBatch batch;
//    private List<Ship> shipList;
    private Shark shark;

    public MenuScreen(DirectedGame game) {
        super(game);
        // init network connection when start game
        networkConnected = false;
        new NetworkChecker().checkConnection();
        init();
    }

    private void init() {
//        shipList = new ArrayList<Ship>();
        batch = new SpriteBatch();
        DupiAssets.getInstance().init();
        title = new Image(new Texture("logo.png"));
        // make title bigger
        title.setScale(DupiAssets.scaleUnit);
        title.setPosition((float) DupiAssets.screenHeight / 2 - (title.getWidth() / 2) * DupiAssets.scaleUnit,
                (float) DupiAssets.screenWidth / 2 + DupiAssets.sizeUnit * 4);

        // Initialize the score where we will place everything
        stage = new Stage();

        //enable input from user for this screen
        Gdx.input.setInputProcessor(stage);

        atlas = DupiAssets.getEnvironment().getAtlasStatic();
        seaBg = new Image(atlas.findRegion("sea"));

        skin = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        skin2 = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        skin3 = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        //create button New Game
        btnNewGame = new ImageButton(skin);
        btnNewGame.setSize(DupiAssets.sizeUnit * 6, DupiAssets.sizeUnit * 2.8f);
        btnNewGame.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_play_text"));
        btnNewGame.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_play_text"));
        btnNewGame.setPosition(DupiAssets.screenHeight / 2 - btnNewGame.getWidth() / 2, DupiAssets.screenWidth / 2 - btnNewGame.getHeight() / 2);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Start game at menu screen
                ScreenTransition transition = ScreenTransitionFade.init(1);
                game.setScreen(new MainGameScreen(game), transition);
            }
        });

        btnLeaderBoard = new ImageButton(skin2);
        btnLeaderBoard.setSize(DupiAssets.sizeUnit * 5, DupiAssets.sizeUnit * 2.5f);
        btnLeaderBoard.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_rank_text"));
        btnLeaderBoard.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_rank_text"));
        btnLeaderBoard.setPosition(DupiAssets.screenHeight / 2 - btnLeaderBoard.getWidth() / 2,
                DupiAssets.screenWidth / 2 - btnNewGame.getHeight() * 3 / 2 - 10 * DupiAssets.scaleUnit);
        btnLeaderBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    if (DupiMain.iGooglePlayService != null && DupiMain.iGooglePlayService.checkIsSignIn())
                        DupiMain.iGooglePlayService.getLeaderBoard();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSignIn = new ImageButton(skin3);
        btnSignIn.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_sign_in_text"));
        btnSignIn.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_sign_in_text"));

        if (DupiMain.iGooglePlayService.checkIsSignIn()) {
            btnSignIn.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_sign_out_text"));
            btnSignIn.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_sign_out_text"));
        }

        btnSignIn.setSize(DupiAssets.sizeUnit * 5, DupiAssets.sizeUnit * 2.5f);
        btnSignIn.setPosition(DupiAssets.screenHeight / 2 - btnSignIn.getWidth() / 2,
                DupiAssets.screenWidth / 2 - btnNewGame.getHeight() * 5 / 2 - 20 * DupiAssets.scaleUnit);

        btnSignIn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (networkConnected) {
                    if (!DupiMain.iGooglePlayService.checkIsSignIn()) {
                        DupiMain.iGooglePlayService.login();
                        btnSignIn.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_sign_out_text"));
                        btnSignIn.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_sign_out_text"));

                    } else {
                        DupiMain.iGooglePlayService.logout();
                        btnSignIn.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion("btn_sign_in_text"));
                        btnSignIn.getStyle().imageDown = new TextureRegionDrawable(atlas.findRegion("btn_sign_in_text"));
                    }
                }
            }
        });
    }


    @Override
    public void show() {

            /*Wait until they are finished loading
        Assets are loaded asynchronously, we need to complete the loading
        before we move on with our code*/
        DupiAssets.getInstance().getAssetManager().finishLoading();
        screenBg = atlas.findRegion("menu_bg");

        // Add all the actors to the score
        stage.addActor(seaBg);
        stage.addActor(title);
        stage.addActor(btnNewGame);
        stage.addActor(btnLeaderBoard);
        stage.addActor(btnSignIn);
        com.phucle.fadu.utils.AudioManager.getInstance().play("sounds/menu_music.mp3");

//        int numShip = MathUtils.random(1, 2);
//
//        for (int i = 0; i <= numShip; i++) {
//            Ship ship = new Ship(DupiAssets.getEnvironment());
//            ship.getPosition().x = MathUtils.random(-ship.width / 2, DupiAssets.screenHeight - ship.width / 2);
//            shipList.add(ship);
//        }

        shark = new Shark(DupiAssets.getEnvironment(), MathUtils.random(0, DupiAssets.screenHeight - 317 * DupiAssets.scaleUnit),
                -100 * DupiAssets.scaleUnit);
    }

    @Override
    public void resize(int width, int height) {
        // Scale the viewport to fit the screen
        //Vector2 scaledView = Scaling.fit.apply(800, 480, width, height);
        //score.getViewport().update((int) scaledView.x, (int) scaledView.y, true);

        // Make the background fill the screen
        seaBg.setSize(width, 80 * DupiAssets.scaleUnit);

    }

    @Override
    public void render(float delta) {

        // update before drawing moving ship


        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Show the loading screen

        com.phucle.fadu.utils.MyCamera.getCamera().update();
        batch.setProjectionMatrix(MyCamera.getCamera().combined); // for camera size to take effect

        //begin drawing process
        batch.begin();

        //disable adding translucent pixels for the first (background) texture for better performance
        batch.disableBlending();
        batch.draw(screenBg, 0, 0, DupiAssets.screenHeight, DupiAssets.screenWidth);

        //enable adding translucent pixels for other textures to be overlaid the background texture
        batch.enableBlending();

//        for (Ship ship : shipList) {
//            ship.render(batch);
//        }

        shark.render(batch);

        //end of drawing
        batch.end();

        stage.act();
        stage.draw();

    }


    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
        com.phucle.fadu.utils.AudioManager.getInstance().stopMusic();
        garbageCollect();
    }

    @Override
    public void pause() {
        com.phucle.fadu.utils.AudioManager.getInstance().pause();
    }

    @Override
    public void resume() {
        super.resume();
        AudioManager.getInstance().resume();
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    private void garbageCollect() {
        skin.dispose();
        skin2.dispose();
        skin3.dispose();
        stage.dispose();
        title.clear();
        seaBg.clear();
        batch.dispose();
        btnLeaderBoard.clear();
        btnNewGame.clear();
        btnSignIn.clear();
//        shipList.clear();
        shark = null;
    }

}
