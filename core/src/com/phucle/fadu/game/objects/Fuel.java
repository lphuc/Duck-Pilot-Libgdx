package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/24/2017.
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.phucle.fadu.game.DupiAssets;

public class Fuel extends GameObject implements IContract.CollideAble {
    private Animation<TextureRegion> spriteSheet;
    private com.phucle.fadu.game.objects.Environment environment;
    private float animationStateTime;
    public boolean collected;

    public Fuel(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        collected = false;
        width = environment.getAtlasAnimated().findRegion("energy1").getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = environment.getAtlasAnimated().findRegion("energy1").getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + 100;
        position.y = com.phucle.fadu.game.DupiAssets.screenWidth - MathUtils.random(91 * com.phucle.fadu.game.DupiAssets.scaleUnit, com.phucle.fadu.game.DupiAssets.screenWidth * 2 / 3f);
        velocity.set(0, 0);

        setAnimation();
    }

    //currently no animation for meteor
    private void setAnimation() {
        spriteSheet = new Animation<TextureRegion>(0.07f,
                environment.getAtlasAnimated().findRegion("energy1"),
                environment.getAtlasAnimated().findRegion("energy2"),
                environment.getAtlasAnimated().findRegion("energy3"),
                environment.getAtlasAnimated().findRegion("energy4"),
                environment.getAtlasAnimated().findRegion("energy5"),
                environment.getAtlasAnimated().findRegion("energy6"),
                environment.getAtlasAnimated().findRegion("energy7"),
                environment.getAtlasAnimated().findRegion("energy8"),
                environment.getAtlasAnimated().findRegion("energy9"),
                environment.getAtlasAnimated().findRegion("energy10"));
        spriteSheet.setPlayMode(Animation.PlayMode.LOOP);

    }


    @Override
    public void update(float deltaTime) {
        animationStateTime += deltaTime;
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();
        bounds.set(position.x, position.y, width, height);

        //remove when out of visibility to improve performance
        if (position.x < -width - 20 * DupiAssets.scaleUnit) {
            collected = true;
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        batch.draw(spriteSheet.getKeyFrame(animationStateTime), position.x, position.y, width, height);
    }

}
