package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/20/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.phucle.fadu.game.DupiAssets;

public class Flag extends GameObject {
    private TextureRegion flagTexture;
    private Environment environment;
    public boolean isInScene;


    public Flag(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isInScene = true;
        flagTexture = environment.getAtlasStatic().findRegion("flag");
        width = flagTexture.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = flagTexture.getRegionHeight() * com.phucle.fadu.game.DupiAssets.scaleUnit;

        position.y = 50 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + 200;
    }

    @Override
    public void update(float deltaTime) {
        position.x -= Duck.getDeltaPosition();

        if (position.x < -width - 20 * DupiAssets.scaleUnit) {
            isInScene = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isInScene) return;
        batch.draw(flagTexture, position.x, position.y);
    }
}
