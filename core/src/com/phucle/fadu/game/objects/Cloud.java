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

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class Cloud extends GameObject implements IContract.MoveAble {
    private Array<TextureAtlas.AtlasRegion> cloudTextures = new Array<TextureAtlas.AtlasRegion>();
    private TextureRegion selectedCloud;
    private Environment environment;
    private boolean isCloudInScene;

    public Cloud(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isCloudInScene = true;

        cloudTextures.add(environment.getAtlasStatic().findRegion("cloud1"));
        cloudTextures.add(environment.getAtlasStatic().findRegion("cloud2"));
        cloudTextures.add(environment.getAtlasStatic().findRegion("cloud3"));

        //select meteor texture for the first time
        getRandomCloud();
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();

        if (position.x < - width - 20 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
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
        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + random(200, 2000);
        position.y = com.phucle.fadu.game.DupiAssets.screenWidth - random(150, 250);
        //random velocity of each meteor rock
        Random random = new Random();
        int x = -(random.nextInt(50));
        int y = 0;
        velocity.set(new Vector2(x, y));

        float randSize = MathUtils.random(0.8f, 1.2f);

        width = selectedCloud.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit * randSize;
        height = selectedCloud.getRegionHeight() * DupiAssets.scaleUnit * randSize;
    }

    public boolean isCloudInScene() {
        return isCloudInScene;
    }
}
