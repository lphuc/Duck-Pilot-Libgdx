package com.phucle.fadu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.phucle.fadu.game.objects.Coin;
import com.phucle.fadu.game.objects.Duck;
import com.phucle.fadu.game.objects.Fuel;
import com.phucle.fadu.game.objects.Land;
import com.phucle.fadu.DupiMain;
import com.phucle.fadu.screens.AbstractGameScreen;
import com.phucle.fadu.screens.MainGameScreen;
import com.phucle.fadu.utils.AudioManager;
import com.phucle.fadu.utils.Consts;

/**
 * Created by Phuc Engineer on 10/14/2018.
 * Update the game object coordinator here
 */
public class WorldUpdater {
    public static int distanceMove;
    private Level level;

    public int screenWidth, screenHeight;
    public Duck duck;
    private com.phucle.fadu.game.InputController controller;
    private int maxScore;

    //worldUpdater
    public int visualGold, gold, fuelSpent;
    public float spentScale, fuelLeft;
    private static final float FUEL_FULL = 300 * com.phucle.fadu.game.DupiAssets.scaleUnit;
    private TextureRegion lifeOff, lifeOn;

    private com.phucle.fadu.screens.MainGameScreen gameScreen;
    int speed;

    // TODO: 12/8/2018 Timer count down for each station
    private long timeLeft;
    private long startTime;
    private long timePassed;

    GameState gameState;

    private boolean isUsedAd;
    public boolean isGameOver;
    public boolean isOpenAdDialog;
    private boolean playHitSound;
    private boolean isCheckedOver;

    public World b2world;

    public enum GameState {
        INIT, ACTION, GAME_OVER, PAUSE
    }

    public WorldUpdater(MainGameScreen screen) {
        gameScreen = screen;
        initGame();
    }

    /**
     * it's helpful when an initialization code is available in a separate method,
     * Whenever we need to reset an object in the game, do not have to completely rebuild it,
     * thus save a lot of performance
     */
    private void initGame() {
        Level.getInstance().init();
        initData();
        controller = new InputController(this);
        initGUI();
    }

    /**
     * contains the game logic and will be called several hundred times per second.
     * it requires a delta time so that it can apply updates to the game world
     * according to the fraction of time that has passed since the last rendered frame.
     *
     * @param deltaTime
     */
    public void update(float deltaTime) {
        controller.checkUserInput();

        if (gameState == GameState.INIT || gameState == GameState.GAME_OVER || gameState == GameState.PAUSE) {
            startTime = TimeUtils.millis();
            return;
        }

        timePassed = TimeUtils.timeSinceMillis(startTime) / 1000;
        timeLeft = 30 - timePassed;

        // Spawn shark jumping up when duck is down
        if (duck.getPosition().y + duck.height / 2 < 0 && duck.getPosition().y + duck.height >= 0) {
            if (level.shark == null) {
                level.spawnShark();
            }
        }

        // Spawn bike flying up when shark is down
        if (level.shark != null && level.shark.getPosition().y + level.shark.height < 0) {
            if (level.bike == null) {
                level.spawnBike();
            }
        }

        if (duck.getPosition().y + duck.height * 20 < 0 || fuelLeft <= 0) {
            if (fuelLeft <= 0) {
                duck.getVelocity().set(0, 0);
            }

            // only call this method once
            if (!isCheckedOver) {
                checkEndGameOrNot();
            }
            isCheckedOver = true;
        }

        // check if land is able to ground or not
        for (Land land : level.landList) {
            if (duck.getPosition().x + duck.width >= land.getPosition().x
                    && duck.getPosition().x <= land.getPosition().x + land.width
                    && duck.getPosition().y >= land.getPosition().y + land.height - 2 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
                land.landAble = true;
            }
        }

        //get new state of the duck
        level.update(deltaTime);
        checkCollision();
        updateGUI(deltaTime);

        speed = (int) ((duck.getVelocity().x / 10) / com.phucle.fadu.game.DupiAssets.scaleUnit);

        if (visualGold < gold) {
            visualGold++;
        }
    }

    private void initData() {
        isGameOver = false;
        isOpenAdDialog = false;
        level = Level.getInstance();
        duck = level.duck;

        playHitSound = false;

        /*While in the INIT state, we need to skip the logic in the updateScene method but render
        an indicator that lets the player know that he or she needs to tap to start the game play*/
        gameState = GameState.INIT;
        fuelLeft = 300 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        timeLeft = 30;
        fuelSpent = 0;
        timePassed = 0;
        screenWidth = com.phucle.fadu.game.DupiAssets.screenWidth;
        screenHeight = com.phucle.fadu.game.DupiAssets.screenHeight;
        spentScale = 0;
        isUsedAd = false;
        gold = 0;
        visualGold = gold;
        isCheckedOver = false;

        maxScore = com.phucle.fadu.utils.MySettings.getInstance().getPrefs().getInteger(com.phucle.fadu.utils.MySettings.MAX_SCORE);
    }

    private void initGUI() {
        distanceMove = 0;
        lifeOff = com.phucle.fadu.game.DupiAssets.getEnvironment().getAtlasStatic().findRegion(com.phucle.fadu.utils.Consts.GAME_RESOURCE.LIFE_OFF);
        lifeOn = com.phucle.fadu.game.DupiAssets.getEnvironment().getAtlasStatic().findRegion(Consts.GAME_RESOURCE.LIFE_ON);
    }

    /**
     * reset the MainGameGameScreen right after the duck is crashed
     */
    public void resetScene() {
        level.resetPosToDrawn();
        fuelLeft = 300 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        fuelSpent = 0;
        distanceMove = 0;
        timeLeft = 30;
        timePassed = 0;
        gold = 0;
        visualGold = gold;
        playHitSound = false;
        isCheckedOver = false;
    }

    Level getLevel() {
        return level;
    }

    private void planeCollideWithCoin(Coin coin) {
        coin.collected = true;
        com.phucle.fadu.utils.AudioManager.getInstance().play(com.phucle.fadu.game.DupiAssets.getInstance().getPickCoinSound(), (float) 0.4);
        gold = gold + 1;
    }

    private void planeCollideWithFuel(Fuel fuel) {
        if (gold > 0) {
            fuel.collected = true;
            com.phucle.fadu.utils.AudioManager.getInstance().play(com.phucle.fadu.game.DupiAssets.getInstance().getFuelSound(), 1.5f);

            if (spentScale - gold * 20 > 0) {
                spentScale -= gold * 20;
            } else {
                spentScale = 0;
            }
            gold = 0;
            visualGold = gold;
        }
    }

    /**
     * check collision between all objects
     */
    private void checkCollision() {
        // collision between duck & coin
        for (Coin coin : level.coinList) {
            if (duck.getBounds().overlaps(coin.getBounds())) {
                if (coin.collected) return;
                planeCollideWithCoin(coin);
            }
        }

        // collision between duck & land
        for (Land land : level.landList) {
            if (land.landAble) {
                if (duck.getPosition().y >= land.getPosition().y + land.height) {
                    duck.getGravity().set(0, Duck.GRAVITY_Y * com.phucle.fadu.game.DupiAssets.scaleUnit);
                    duck.grounded = false;
                } else if (duck.getPosition().x + duck.width - 63 * com.phucle.fadu.game.DupiAssets.scaleUnit >= land.getPosition().x
                        && duck.getPosition().x + 18 * com.phucle.fadu.game.DupiAssets.scaleUnit <= land.getPosition().x + land.width && !duck.isCollided) {
                    duck.getPosition().y = land.getPosition().y + land.height;
                    duck.getGravity().y = 0;
                    duck.getVelocity().y = 0;
                    duck.grounded = true;
                    land.isLanded = true;
                } else {
                    duck.getGravity().set(0, Duck.GRAVITY_Y * com.phucle.fadu.game.DupiAssets.scaleUnit);
                    duck.grounded = false;
                }
            } else {
                if (duck.getBounds().overlaps(land.getBounds())) {
                    duck.isCollided = true;

                    if (!playHitSound) {
                        AudioManager.getInstance().play(com.phucle.fadu.game.DupiAssets.getInstance().getHitSound(), 1);
                        Gdx.input.vibrate(100);
                        playHitSound = true;
                    }
                }
                duck.getGravity().set(0, Duck.GRAVITY_Y * com.phucle.fadu.game.DupiAssets.scaleUnit);
            }

        }


        // collision between duck & fuel
        for (Fuel fuel : level.fuelList) {
            if (duck.getBounds().overlaps(fuel.getBounds())) {
                if (fuel.collected) return;
                planeCollideWithFuel(fuel);
            }
        }

    }

    private void updateGUI(float deltaTime) {
        fuelLeft = FUEL_FULL - fuelSpent;

        distanceMove = (int) (duck.getDistanceMove() / (com.phucle.fadu.game.DupiAssets.scaleUnit * 20));

//            startTime = TimeUtils.millis();

        if (duck.getPosition().y > screenWidth) {
            spentScale = spentScale + 50 * deltaTime;
        } else {
            spentScale = spentScale + 4 * deltaTime * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        if (fuelSpent <= 300 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
            fuelSpent = (int) ((200 * spentScale / 100) * com.phucle.fadu.game.DupiAssets.scaleUnit);
        }

    }

    /**
     * get called when the duck crashed
     */
    public void openGameOverView() {
        if (gameState != GameState.GAME_OVER) {
//            gameState = GameState.GAME_OVER;

            com.phucle.fadu.utils.MySettings.getInstance().getPrefs().putInteger(com.phucle.fadu.utils.MySettings.STAGE, distanceMove).flush();
            if (maxScore < distanceMove) {
                com.phucle.fadu.utils.MySettings.getInstance().getPrefs().putInteger(com.phucle.fadu.utils.MySettings.MAX_SCORE, distanceMove).flush();
            }

            gameScreen.addOverActors();
            isGameOver = true;
            /**
             *  Submit score to LeaderBoard
             */
            if (com.phucle.fadu.DupiMain.iGooglePlayService.checkIsSignIn()) {
                com.phucle.fadu.DupiMain.iGooglePlayService.submitScore(distanceMove);
            }
        }
    }

    /**
     * after user close advertisement, resume game
     */
    public void resumeCurrentGame() {
        level.shark = null;
        level.bike = null;
        isGameOver = false;
        isOpenAdDialog = false;
        gameState = GameState.INIT;
        duck.reset();
        level.landList.clear();
        level.landList.add(new Land(level.environment, 80 * com.phucle.fadu.game.DupiAssets.scaleUnit, 12, true));
        fuelLeft = 300 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        fuelSpent = 0;
        spentScale = 0;
        startTime = TimeUtils.millis();
        timeLeft = 30;
        timePassed = 0;
        duck.getPosition().y = DupiAssets.screenWidth - duck.height;
        duck.isCollided = false;
        isUsedAd = true; // chi duoc dung quyen cuu thuong 1 lan
        playHitSound = false;
        isCheckedOver = false;
    }


    /**
     * When user clicks on "Try Again" button, restart game
     */
    public void restartGame() {
        isUsedAd = false;
        duck.reset();
        level.resetGameWorld();
        initGame();
    }

    /**
     * check if ad is loaded and only show ad if it meets these below conditions
     * rate to show = 50%
     * isAdvLoaded -> whether video was loaded successfully from AndroidLauncher
     * isUsedAd -> whether user opened advertisement already
     */
    private void checkEndGameOrNot() {
        int rateToShow = MathUtils.random(1, 3);
        if (AbstractGameScreen.networkConnected && rateToShow == 1 && com.phucle.fadu.DupiMain.isAdvLoaded && !isUsedAd && distanceMove > 500) {
            gameState = GameState.PAUSE;
            isOpenAdDialog = true;

            //clear game over actors first
            gameScreen.resetOverActors();
            gameScreen.openAdConfirmDialog();
        } else {
            if (rateToShow == 1) {
                //enable/disable showing ad
                DupiMain.iAdvertisement.showInterstitialAd();
            }
            openGameOverView();
        }
    }

}