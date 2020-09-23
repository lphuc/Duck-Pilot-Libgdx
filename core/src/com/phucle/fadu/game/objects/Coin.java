package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/24/2017.
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.phucle.fadu.game.DupiAssets;

public class Coin extends GameObject implements IContract.CollideAble {
    private Animation<TextureRegion> spriteSheet;
    private Environment environment;
    private float animationStateTime;
    public boolean collected;

    public Coin(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        collected = false;
        width = environment.getAtlasAnimated().findRegion("coin1").originalWidth * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = width;
        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + 100;
        position.y = com.phucle.fadu.game.DupiAssets.screenWidth - MathUtils.random(70 * com.phucle.fadu.game.DupiAssets.scaleUnit, com.phucle.fadu.game.DupiAssets.screenWidth * 2 / 3f);
        setAnimation();
    }

    //currently no animation for meteor
    private void setAnimation() {
        spriteSheet = new Animation<TextureRegion>(0.05f,
                environment.getAtlasAnimated().findRegion("coin1"),
                environment.getAtlasAnimated().findRegion("coin2"),
                environment.getAtlasAnimated().findRegion("coin3"),
                environment.getAtlasAnimated().findRegion("coin4"),
                environment.getAtlasAnimated().findRegion("coin5"),
                environment.getAtlasAnimated().findRegion("coin6"),
                environment.getAtlasAnimated().findRegion("coin7"),
                environment.getAtlasAnimated().findRegion("coin8"),
                environment.getAtlasAnimated().findRegion("coin9"),
                environment.getAtlasAnimated().findRegion("coin10"));
        spriteSheet.setPlayMode(Animation.PlayMode.LOOP);

    }

    @Override
    public void update(float deltaTime) {
        animationStateTime += deltaTime;
        bounds.set(position.x, position.y, width, height);
        position.x -= Duck.getDeltaPosition();

        //remove tree when out of visibility to improve performance
        if (position.x < -width - 20 * DupiAssets.scaleUnit) {
            collected = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        batch.draw(spriteSheet.getKeyFrame(animationStateTime), position.x, position.y, width, height);
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

}
