package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/24/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.phucle.fadu.game.DupiAssets;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class CloudFar extends GameObject implements IContract.MoveAble {
    private Array<TextureAtlas.AtlasRegion> cloudTextures = new Array<TextureAtlas.AtlasRegion>();
    private TextureRegion selectedCloud;
    private Environment environment;
    private boolean isCloudInScene;


    public CloudFar(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isCloudInScene = true;

        // cloudTextures.add(environment.getAtlasStatic().findRegion("meteor_huge"));
        cloudTextures.add(environment.getAtlasStatic().findRegion("cloudfar1"));
        cloudTextures.add(environment.getAtlasStatic().findRegion("cloudfar2"));

        //select meteor texture for the first time
        getRandomCloud();
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition() / 10;

        if (position.x < -width - 10 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
            isCloudInScene = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isCloudInScene) return;
        batch.draw(selectedCloud, position.x, position.y, width, height);
    }

    private void getRandomCloud() {
        // get random meteor texture from the list
        int id = (int) (Math.random() * cloudTextures.size);
        selectedCloud = cloudTextures.get(id);

        // set random velocity
        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + random(5, 200);
        position.y = com.phucle.fadu.game.DupiAssets.screenWidth - random(320 * com.phucle.fadu.game.DupiAssets.scaleUnit, 350 * com.phucle.fadu.game.DupiAssets.scaleUnit);
        //random velocity of each meteor rock
        Random random = new Random();
        int x = -(random.nextInt(5));
        int y = 0;
        velocity.set(new Vector2(x, y));

        width = selectedCloud.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = selectedCloud.getRegionHeight() * DupiAssets.scaleUnit;
    }
}
