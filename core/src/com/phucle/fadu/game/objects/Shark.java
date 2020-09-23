package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 02/18/2019.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.phucle.fadu.game.DupiAssets;

public class Shark extends GameObject {
    private TextureRegion texture;
    public boolean isInScene;
    public float rotation;
    private boolean justJump;
    private float randSize;

    public Shark(Environment environment, float xPos, float yPos) {
        isInScene = true;
        texture = environment.getAtlasStatic().findRegion("shark");

        randSize = MathUtils.random(0.5f, 1.5f);

        width = texture.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize;
        height = texture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize;

        position.y = yPos;
        position.x = xPos;

        rotation = 1;

        velocity.set(-250 * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize, 280 * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize);
        gravity.set(0, -50 * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize);
        justJump = true;
    }


    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);

        if (position.y + height <= randSize * com.phucle.fadu.game.DupiAssets.screenWidth / 3 && justJump) {
            velocity.add(-60 * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize, 60 * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize);
        } else {
            velocity.add(gravity);
            justJump = false;
        }

        if (position.y + height * 2 < 0) {
            isInScene = false;
        }

        if (velocity.y < 0) {
            rotation += 2 * DupiAssets.scaleUnit * randSize;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isInScene) return;

        float offsetX = width / 2;
        float offsetY = height / 2;

        batch.draw(texture, position.x, position.y, offsetX, offsetY, width, height, 1f, 1f, rotation);
    }
}
