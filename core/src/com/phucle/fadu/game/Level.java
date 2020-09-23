package com.phucle.fadu.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.phucle.fadu.game.objects.Bike;
import com.phucle.fadu.game.objects.Cloud;
import com.phucle.fadu.game.objects.CloudFar;
import com.phucle.fadu.game.objects.Coin;
import com.phucle.fadu.game.objects.Duck;
import com.phucle.fadu.game.objects.Environment;
import com.phucle.fadu.game.objects.Fuel;
import com.phucle.fadu.game.objects.Land;
import com.phucle.fadu.game.objects.Moutain;
import com.phucle.fadu.game.objects.Shark;
import com.phucle.fadu.game.objects.Ship;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

/*
 * Created by lhphuc on 10/15/2018.
 */
public class Level {
    public Duck duck;
    private Moutain moutain;
    public List<Coin> coinList;
    public List<Fuel> fuelList;
    List<Land> landList;
    private List<Ship> shipList;
    private List<Cloud> cloudList;
    private List<CloudFar> cloudFarList;
    Shark shark;
    Bike bike;

    public Environment environment;
    private float nextShipSpawn, nextCoinSpawn, nextFuelSpawn, nextCloudSpawn, nextCloudFarSpawn, nextLandSpawn;

    private static Level instance;

    public static Level getInstance() {
        if (instance == null) {
            instance = new Level();
        }
        return instance;
    }

    private Level() {
    }

    /**
     * initialize all objects in game world here
     */
    public void init() {
        environment = com.phucle.fadu.game.DupiAssets.getEnvironment();
        //create a new duck with default position
        duck = new Duck(environment);
        duck.setAnimation();
        moutain = new Moutain(environment);
        coinList = new ArrayList<Coin>();
        fuelList = new ArrayList<Fuel>();
        landList = new ArrayList<Land>();
        cloudList = new ArrayList<Cloud>();
        cloudFarList = new ArrayList<CloudFar>();
        shipList = new ArrayList<Ship>();
        resetPosToDrawn();
        Land land = new Land(environment, 80 * com.phucle.fadu.game.DupiAssets.scaleUnit, 12, true);
        landList.add(land);
    }


    private void spawnShip() {
        Ship ship = new Ship(environment);
        shipList.add(ship);
    }

    private void spawnCoin() {
        Coin coin = new Coin(environment);
        coinList.add(coin);
    }

    private void spawnFuel() {
        Fuel fuel = new Fuel(environment);
        fuelList.add(fuel);
    }

    private void spawnLand(float yPos, float widthScale) {
        Land land = new Land(environment, yPos, widthScale, false);
        landList.add(land);
    }

    private void spawnCloud() {
        Cloud cloud = new Cloud(environment);
        cloudList.add(cloud);
    }

    private void spawnCloudFar() {
        CloudFar cloud = new CloudFar(environment);
        cloudFarList.add(cloud);
    }

    void spawnShark() {
        shark = new Shark(environment, duck.getPosition().x + com.phucle.fadu.game.DupiAssets.screenHeight / 1.9f, -100 * com.phucle.fadu.game.DupiAssets.scaleUnit);
    }

    void spawnBike() {
        bike = new Bike(environment, duck.getPosition().x + duck.width, -100 * com.phucle.fadu.game.DupiAssets.scaleUnit);
    }

    /**
     * update all game objects state
     *
     * @param deltaTime
     */
    public void update(float deltaTime) {

        if (duck.getDistanceMove() > nextShipSpawn) {
            spawnShip();
            nextShipSpawn = duck.getDistanceMove() + random(4000, 7000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        if (duck.getDistanceMove() > nextCoinSpawn) {
            spawnCoin();
            nextCoinSpawn = duck.getDistanceMove() + random(500, 2000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        if (duck.getDistanceMove() > nextFuelSpawn) {
            spawnFuel();
            nextFuelSpawn = (int) (duck.getDistanceMove() + random(5000, 7000) * com.phucle.fadu.game.DupiAssets.scaleUnit);
        }

        if (duck.getDistanceMove() > nextLandSpawn) {
            float yPos;
            float withScale, randHeight;
            if (com.phucle.fadu.game.WorldUpdater.distanceMove <= 200) {
                withScale = MathUtils.random(3f, 4f);
                randHeight = MathUtils.random(1f, 3f);
            } else if (com.phucle.fadu.game.WorldUpdater.distanceMove <= 500) {
                withScale = MathUtils.random(2.5f, 4f);
                randHeight = MathUtils.random(1f, 4f);
            } else if (com.phucle.fadu.game.WorldUpdater.distanceMove <= 1000) {
                withScale = MathUtils.random(2f, 3.5f);
                randHeight = MathUtils.random(1f, 6f);
            } else if (WorldUpdater.distanceMove <= 2000) {
                withScale = MathUtils.random(1.5f, 2.5f);
                randHeight = MathUtils.random(1f, 6f);
            } else {
                withScale = MathUtils.random(1.0f, 2.0f);
                randHeight = MathUtils.random(1f, 6f);
            }

            float seaHeight = 80 * com.phucle.fadu.game.DupiAssets.scaleUnit;

            yPos = seaHeight * randHeight;

            spawnLand(yPos, withScale);

            nextLandSpawn = duck.getDistanceMove() + com.phucle.fadu.game.DupiAssets.screenHeight - com.phucle.fadu.game.DupiAssets.screenHeight / 2f;
        }

        if (duck.getDistanceMove() > nextCloudSpawn) {
            spawnCloud();
            nextCloudSpawn = duck.getDistanceMove() + random(100, 2000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        if (duck.getDistanceMove() > nextCloudFarSpawn) {
            spawnCloudFar();
            nextCloudFarSpawn = duck.getDistanceMove() + random(3000, 10000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        moutain.update(deltaTime);


        for (Ship ship : shipList) {
            ship.update(deltaTime);
        }

        duck.update(deltaTime);
        environment.update(deltaTime);


        for (Coin coin : coinList) {
            coin.update(deltaTime);
        }

        for (Fuel fuel : fuelList) {
            fuel.update(deltaTime);
        }

        for (Land land : landList) {
            land.update(deltaTime);
        }

        for (Cloud cloud : cloudList) {
            cloud.update(deltaTime);
        }

        for (CloudFar cloud : cloudFarList) {
            cloud.update(deltaTime);
        }

        if (shark != null) {
            shark.update(deltaTime);
        }

        if (bike != null) {
            bike.update(deltaTime);
        }

        /**
         * remove all objects when they are out of scene
         */
        for (int i = 0; i < coinList.size(); i++) {
            if (coinList.get(i).collected) {
                coinList.remove(i);
            }
        }

        for (int i = 0; i < shipList.size(); i++) {
            if (!shipList.get(i).isInScene) {
                shipList.remove(i);
            }
        }

        for (int i = 0; i < cloudList.size(); i++) {
            if (!cloudList.get(i).isCloudInScene()) {
                cloudList.remove(i);
            }
        }

        for (int i = 0; i < fuelList.size(); i++) {
            if (fuelList.get(i).collected) {
                fuelList.remove(i);
            }
        }

        for (int i = 0; i < landList.size(); i++) {
            if (!landList.get(i).isVisible) {
                landList.remove(i);
            }
        }

        if (shark != null && !shark.isInScene) {
            shark = null;
        }

        if (bike != null && !bike.isInScene) {
            bike = null;
        }
    }

    void resetPosToDrawn() {
        //first tree in scene
        nextShipSpawn = random(2000, 4000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        nextCoinSpawn = random(500, 2000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        nextFuelSpawn = random(5000, 7000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        nextLandSpawn = duck.getDistanceMove() + 500 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        nextCloudSpawn = random(200, 2000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        nextCloudFarSpawn = random(7000) * DupiAssets.scaleUnit;
    }


    /**
     * render all game objects to the screen
     * order drawing
     *
     * @param batch
     */
    public void render(SpriteBatch batch) {

        moutain.render(batch);

        for (CloudFar cloudFar : cloudFarList) {
            cloudFar.render(batch);
        }

        for (Ship ship : shipList) {
            ship.render(batch);
        }

        for (Land land : landList) {
            land.render(batch);
        }

        duck.render(batch);

        if (shark != null) {
            shark.render(batch);
        }

        if (bike != null) {
            bike.render(batch);
        }

        // draw sea only
        environment.render(batch);

        for (Fuel fuel : fuelList) {
            fuel.render(batch);
        }

        for (Coin coin : coinList) {
            coin.render(batch);
        }

        for (Cloud cloud : cloudList) {
            cloud.render(batch);
        }
    }

    /**
     * reset all game objects when game is over
     */
    void resetGameWorld() {
        coinList.clear();
        shipList.clear();
        fuelList.clear();
        cloudList.clear();
        cloudFarList.clear();
        landList.clear();

        if (bike != null) {
            bike = null;
        }

        if (shark != null) {
            shark = null;
        }
    }
}
