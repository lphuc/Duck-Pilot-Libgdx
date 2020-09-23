package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/24/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.phucle.fadu.game.DupiAssets;

public class Land extends GameObject implements IContract.MoveAble {
    private TextureRegion landTexture;
    private com.phucle.fadu.game.objects.Environment environment;
    public boolean isVisible;
    public boolean landAble;
    private boolean isFirstLand;

    private float yPos, widthScale;

    //check whether the bike is landed on the ground
    public boolean isLanded;
    private boolean moveUp;

    private float rangeMove;

    private float randVecY;

    public Land(Environment environment, float yPos, float widthScale, boolean isFirstLand) {
        this.yPos = yPos;
        this.widthScale = widthScale;
        this.environment = environment;
        this.isFirstLand = isFirstLand;
        init();
    }

    private void init() {
        isVisible = true;
        landAble = false;
        isLanded = false;

        landTexture = environment.getAtlasStatic().findRegion("land");

        width = landTexture.getRegionWidth() * widthScale * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = landTexture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        if (isFirstLand) {
            landAble = true;
            position.x = 0;
            position.y = 80 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        } else {
            position.x = com.phucle.fadu.game.DupiAssets.screenHeight + 10;
            position.y = yPos;
        }

        int randUpDown = MathUtils.random(0, 1);
        randVecY = MathUtils.random(2f, 3f);

        moveUp = randUpDown == 0;

        rangeMove = MathUtils.random(10f, 60f) * com.phucle.fadu.game.DupiAssets.scaleUnit;

        //random velocity of each meteor rock
        velocity.set(new Vector2(0, 0));
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();
        bounds.set(position.x, position.y, width, height);

        // add velocity for moving up & down to the bricks
//        if (!isFirstLand) {
//            if (moveUp) {
//                velocity.add(0, randVecY * DupiAssets.scaleUnit);
//            } else {
//                velocity.add(0, -randVecY * DupiAssets.scaleUnit);
//            }
//        }

        if (moveUp && velocity.y > 0) {
            if (position.y >= yPos + rangeMove) {
                moveUp = false;
            }
        } else if (!moveUp && velocity.y < 0) {
            if (position.y <= yPos - rangeMove || position.y + height <= 0) {
                moveUp = true;
            }
        }


        if (position.x < -width - 20 * DupiAssets.scaleUnit) {
            isVisible = false;
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible) return;
        batch.draw(landTexture, position.x, position.y, width, height);
    }
}
