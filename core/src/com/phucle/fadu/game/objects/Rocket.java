package com.phucle.fadu.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.phucle.fadu.game.DupiAssets;
import com.phucle.fadu.game.Level;

/*
 * Created by Phuc Engineer on 5/16/2018.
 */
public class Rocket extends GameObject implements IContract.CollideAble {
    private Vector2 velocity = new Vector2();
    private Vector2 position = new Vector2();
    private Animation<TextureRegion> spriteSheet;
    private com.phucle.fadu.game.objects.Environment environment;
    private float rocketWidth;
    private float rocketHeight;
    private float animationStateTime;
    public boolean isFinish;
    private Level level;

    private ParticleEffect rocketTail;

    public Rocket(Environment environment, Level level) {
        this.environment = environment;
        this.level = level;
        init();
    }

    private void init() {
        isFinish = false;
        rocketWidth = environment.getAtlasAnimated().findRegion("rocket1").originalWidth;
        rocketHeight = environment.getAtlasAnimated().findRegion("rocket1").originalHeight;
        rocketTail = DupiAssets.getInstance().getAssetManager().get("particles/rockettail", ParticleEffect.class);
        position.x = level.duck.position.x + 50;
        position.y = level.duck.position.y - rocketHeight / 2;
        setAnimation();
    }

    //currently no animation for meteor
    private void setAnimation() {
        spriteSheet = new Animation<TextureRegion>(0.05f,
                environment.getAtlasAnimated().findRegion("rocket1"),
                environment.getAtlasAnimated().findRegion("rocket2"),
                environment.getAtlasAnimated().findRegion("rocket3"),
                environment.getAtlasAnimated().findRegion("rocket4"));
        spriteSheet.setPlayMode(Animation.PlayMode.LOOP);

    }


    @Override
    public void update(float deltaTime) {
        animationStateTime += deltaTime;
        position.x += 15 * DupiAssets.scaleUnit;
        bounds.set(position.x, position.y, rocketWidth, rocketHeight);

        rocketTail.setPosition(position.x + 10, position.y + 20);
        rocketTail.update(deltaTime);

        if (position.x > level.duck.position.x + 2000) {
            isFinish = true;
        }
    }


    @Override
    public void render(SpriteBatch batch) {

        if (isFinish) {
            return;
        }
        batch.draw(spriteSheet.getKeyFrame(animationStateTime), position.x, position.y);
        rocketTail.draw(batch);
    }


    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
