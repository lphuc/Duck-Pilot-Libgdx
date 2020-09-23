package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/20/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.phucle.fadu.game.DupiAssets;

import static com.badlogic.gdx.math.MathUtils.random;

public class Ship extends GameObject {
    private TextureRegion shipTexture;
    private Environment environment;
    public boolean isInScene;
    private float offsetX;
    private float offsetY;
    private float rotation;


    public Ship(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isInScene = true;

        shipTexture = environment.getAtlasStatic().findRegion("ship");
        //select meteor texture for the first time

        float randSize = random(0.8f, 2.0f);

        width = shipTexture.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize;
        height = shipTexture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize;

        offsetX = width / 2;
        offsetY = height / 2;

        rotation = random(-20, 20);

        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + random(300, 1000);

        if (rotation > -5 && rotation < 5) {
            position.y = random(20, 50) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        } else {
            position.y = random(15, 30) * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }


    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();

        if (position.x < -width - 50 * DupiAssets.scaleUnit) {
            isInScene = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isInScene) return;
        batch.draw(shipTexture, position.x, position.y,
                offsetX, offsetY, width, height,
                1f, 1f, rotation);
    }
}
