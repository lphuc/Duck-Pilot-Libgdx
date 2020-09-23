package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 10/18/2017.
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.phucle.fadu.game.DupiAssets;
import com.phucle.fadu.game.WorldUpdater;

public class Duck extends GameObject implements com.phucle.fadu.game.objects.IContract.CollideAble, IContract.MoveAble {
    private Animation<TextureRegion> spriteSheet;
    private Environment environment;
    private float animationStateTime;
    private ParticleEffect smoke;
    public boolean isCollided;
    private int distanceMovedLocal;
    private float velocityScale;

    // vị trí hiện tại so với điểm xuất phát
    private static float deltaPosition;

    private float terrainOffset;
    private int distanceMove;
    public float rotation;
    public static final int GRAVITY_Y = -49;

    // damping is a friction value to reduce the velocity of the duck.
    private final Vector2 damping = new Vector2(0.99f, 0.99f);

    private static final float minY = 80 * com.phucle.fadu.game.DupiAssets.scaleUnit + 50 * com.phucle.fadu.game.DupiAssets.scaleUnit;
    public boolean grounded;

    // public float timePassed;
    public Duck(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isCollided = false;
        grounded = true;
        rotation = 0;
        distanceMovedLocal = 0;
        velocityScale = 0;
//        timePassed = 1;

        width = (float) environment.getAtlasAnimated().findRegion("duck1").getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = (float) environment.getAtlasAnimated().findRegion("duck1").getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        setAnimation();

        velocity.set(0, 0);
        gravity.set(0, GRAVITY_Y * com.phucle.fadu.game.DupiAssets.scaleUnit);
        position = new Vector2(getDefaultPosition());
        smoke = com.phucle.fadu.game.DupiAssets.getInstance().getAssetManager().get("particles/smoke3", ParticleEffect.class);
    }

    //animate the plan based on delta time
    public void setAnimation() {
        spriteSheet = new Animation<TextureRegion>(0.022f,
                environment.getAtlasAnimated().findRegion("duck1"),
                environment.getAtlasAnimated().findRegion("duck2"),
                environment.getAtlasAnimated().findRegion("duck3"),
                environment.getAtlasAnimated().findRegion("duck4"),
                environment.getAtlasAnimated().findRegion("duck5"),
                environment.getAtlasAnimated().findRegion("duck6"),
                environment.getAtlasAnimated().findRegion("duck7"),
                environment.getAtlasAnimated().findRegion("duck8"),
                environment.getAtlasAnimated().findRegion("duck9"),
                environment.getAtlasAnimated().findRegion("duck10"),
                environment.getAtlasAnimated().findRegion("duck11"),
                environment.getAtlasAnimated().findRegion("duck12"),
                environment.getAtlasAnimated().findRegion("duck13"),
                environment.getAtlasAnimated().findRegion("duck14"),
                environment.getAtlasAnimated().findRegion("duck15"),
                environment.getAtlasAnimated().findRegion("duck16"),
                environment.getAtlasAnimated().findRegion("duck17"),
                environment.getAtlasAnimated().findRegion("duck18"),
                environment.getAtlasAnimated().findRegion("duck19"),
                environment.getAtlasAnimated().findRegion("duck20"));

        spriteSheet.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void update(float deltaTime) {
        // reduce velocity multiply
        velocity.scl(damping);

        position.mulAdd(velocity, deltaTime);

        if (com.phucle.fadu.game.WorldUpdater.distanceMove >= distanceMovedLocal + 100 && com.phucle.fadu.game.WorldUpdater.distanceMove < 4000) {
            velocityScale += 0.1;
            distanceMovedLocal += 100;
        }

        if (WorldUpdater.distanceMove < 100) {
            velocity.add(7 * com.phucle.fadu.game.DupiAssets.scaleUnit, 0);
        } else {
            velocity.add((7 + velocityScale) * com.phucle.fadu.game.DupiAssets.scaleUnit, 0);
        }

        velocity.add(gravity);
        animationStateTime += deltaTime;

        // update smoke
        smoke.setPosition(position.x + 15 * com.phucle.fadu.game.DupiAssets.scaleUnit, position.y + height / 2 - height / 5.5f);
        smoke.update(deltaTime);

        // move terrains based on the position of plane1
        deltaPosition = position.x - getDefaultPosition().x;
        terrainOffset -= deltaPosition;

        position.x = getDefaultPosition().x;
        bounds.set(position.x + 10 * com.phucle.fadu.game.DupiAssets.scaleUnit, position.y + 10 * com.phucle.fadu.game.DupiAssets.scaleUnit,
                width - 40 * com.phucle.fadu.game.DupiAssets.scaleUnit, height);

        distanceMove += deltaPosition;

        if (terrainOffset * -1 > environment.getGround().getRegionWidth()) {
            terrainOffset = 0;
        }
        if (terrainOffset > 0) {
            terrainOffset = -environment.getGround().getRegionWidth();
        }

        if (isCollided) {
            //random velocity of each meteor rock
            float x = -MathUtils.random(300, 500) * com.phucle.fadu.game.DupiAssets.scaleUnit;
            velocity.set(new Vector2(x, velocity.y));
        }

        if (position.y + height < 0) {
            velocity.set(new Vector2(0, velocity.y));
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        float offsetX = width / 2;
        float offsetY = height / 2;

        if (isCollided) {
            rotation += 12 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        }

        batch.draw(spriteSheet.getKeyFrame(animationStateTime), position.x, position.y,
                offsetX, offsetY, width, height, 1f, 1f, rotation);

        smoke.draw(batch);
    }

    public void reset() {
        smoke.reset();
        rotation = 0;
        isCollided = false;
        velocityScale = 0;
        distanceMovedLocal = 0;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    private Vector2 getDefaultPosition() {
        return new Vector2(DupiAssets.screenHeight / 6f, minY);
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    public Vector2 getGravity() {
        return this.gravity;
    }

    public int getDistanceMove() {
        return distanceMove;
    }

    public static float getDeltaPosition() {
        return deltaPosition;
    }

    public Animation<TextureRegion> getSpriteSheet() {
        return spriteSheet;
    }

}
