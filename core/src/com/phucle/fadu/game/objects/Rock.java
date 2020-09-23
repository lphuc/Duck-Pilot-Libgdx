package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/24/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.phucle.fadu.game.DupiAssets;

public class Rock extends GameObject implements IContract.MoveAble {
    @Deprecated
    private Array<TextureAtlas.AtlasRegion> rockTextures = new Array<TextureAtlas.AtlasRegion>();

    private TextureRegion meteorTexture;
    private Environment environment;
    public boolean isAlive;
    public boolean isCollided;
    public boolean isPassed;
    private float yPos;
    float rotation;

    public Rock(Environment environment, float yPos) {
        this.yPos = yPos;
        this.environment = environment;
        init();
    }

    private void init() {
        isAlive = true;
        isPassed = false;
        isCollided = false;
        rotation = 1;

        meteorTexture = environment.getAtlasStatic().findRegion("meteor_small");

        width = meteorTexture.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = meteorTexture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        // set random velocity
        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + 500;
        position.y = yPos;

        //random velocity of each meteor rock
        velocity.set(new Vector2(0, 0));

    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();

        bounds.set(position.x, position.y, width, height);

        if (position.x < -width - 20 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
            isAlive = false;
        }

        if (position.y <= environment.groundHeight - environment.groundHeight / 2) {
            isAlive = false;
        }

        if (isCollided) {
            //random velocity of each meteor rock
            float x = MathUtils.random(700, 1000) * com.phucle.fadu.game.DupiAssets.scaleUnit;
            float y = -800 * DupiAssets.scaleUnit;
            velocity.set(new Vector2(x, y));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isAlive) return;

        float offsetX = width / 2;
        float offsetY = height / 2;

        if (isCollided) {
            rotation -= 20;
        }

        batch.draw(meteorTexture, position.x, position.y,
                offsetX, offsetY, width, height,
                1f, 1f, rotation);
    }
}
