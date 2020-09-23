package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 10/18/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.phucle.fadu.game.DupiAssets;

public class Environment extends GameObject {
    /*The TextureRegion class is a rectangular portion of a bigger texture, we willbe increasing
     or decreasing this value to make the terrain scroll right or left, respectively */
    private TextureRegion bgRegion, ground;

    private TextureRegion btnRankLabel, btnPlayLabel, btnSignInLabel, btnSignOutLable;

    //put all images into a single atlas for better rendering performance
    private TextureAtlas atlasStatic, atlasAnimated;

    private static float terrainOffset;

    public float groundWidth, groundHeight;

    public Environment() {
        init();
    }

    private void init() {
        terrainOffset = 0;

        atlasStatic = new TextureAtlas(Gdx.files.internal("static.atlas"));
        atlasAnimated = new TextureAtlas(Gdx.files.internal("animated.atlas"));

        bgRegion = atlasStatic.findRegion("main_bg");
        ground = atlasStatic.findRegion("sea");

        groundWidth = ground.getRegionWidth();
        groundHeight = ground.getRegionHeight() * DupiAssets.scaleUnit;
    }

    @Override
    public void update(float deltaTime) {
        // move terrains based on the position of plane1
        terrainOffset -= Duck.getDeltaPosition();

        if (terrainOffset * -1 > getGround().getRegionWidth()) {
            terrainOffset = 0;
        }
        if (terrainOffset > 0) {
            terrainOffset = -getGround().getRegionWidth();
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        //draw ground & cloud
        batch.draw(getGround(), terrainOffset, 0, groundWidth, groundHeight);
        //ve truoc them vi truong hop man hinh do phan giai cao
        batch.draw(getGround(), terrainOffset + ground.getRegionWidth(), 0, groundWidth, groundHeight);
        batch.draw(getGround(), terrainOffset + ground.getRegionWidth() * 2, 0, groundWidth, groundHeight);
        batch.draw(getGround(), terrainOffset + ground.getRegionWidth() * 3, 0, groundWidth, groundHeight);

    }

    public TextureRegion getBgRegion() {
        return bgRegion;
    }


    public TextureRegion getGround() {
        return ground;
    }

    public TextureAtlas getAtlasStatic() {
        return atlasStatic;
    }

    public TextureAtlas getAtlasAnimated() {
        return atlasAnimated;
    }

    public static void setTerrainOffset(float terrainOffset) {
        Environment.terrainOffset = terrainOffset;
    }

    public TextureRegion getBtnRankLabel() {
        return new TextureRegion(atlasStatic.findRegion("btn_rank_text"));
    }

    public TextureRegion getBtnPlayLabel() {
        return new TextureRegion(atlasStatic.findRegion("btn_play_text"));
    }

    public TextureRegion getBtnSignInLabel() {
        return new TextureRegion(atlasStatic.findRegion("btn_sign_in_text"));
    }

    public TextureRegion getBtnSignOutLabel() {
        return new TextureRegion(atlasStatic.findRegion("btn_sign_out_text"));
    }

    public void resetEnvironment() {
        atlasStatic.dispose();
        atlasAnimated.dispose();
    }
}
