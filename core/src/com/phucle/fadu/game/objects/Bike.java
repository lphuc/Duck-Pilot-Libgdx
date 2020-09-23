package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 02/18/2019.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.phucle.fadu.game.DupiAssets;

public class Bike extends GameObject {
    private TextureRegion texture;
    public boolean isInScene;
    public float rotation;
    private boolean justJump;
    private float randRotation;

    public Bike(Environment environment, float xPos, float yPos) {

        isInScene = true;
        texture = environment.getAtlasStatic().findRegion("bike");
        width = texture.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = texture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        position.y = yPos;
        position.x = xPos;

        rotation = 1;
        randRotation = MathUtils.random(6, 12);

        velocity.set(-0, 500);
        gravity.set(0, -50 * com.phucle.fadu.game.DupiAssets.scaleUnit);
        justJump = true;
    }


    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);

        if (position.y + height <= com.phucle.fadu.game.DupiAssets.screenWidth / 3 && justJump) {
            velocity.add(0 * com.phucle.fadu.game.DupiAssets.scaleUnit, 50 * com.phucle.fadu.game.DupiAssets.scaleUnit);
        } else {
            velocity.add(gravity);
            justJump = false;
        }

        if (position.y + height * 2 < 0) {
            isInScene = false;
        }

        rotation += randRotation * DupiAssets.scaleUnit;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isInScene) return;

        float offsetX = width / 2;
        float offsetY = height / 2;

        batch.draw(texture, position.x, position.y, offsetX, offsetY, width, height, 1f, 1f, rotation);
    }
}
