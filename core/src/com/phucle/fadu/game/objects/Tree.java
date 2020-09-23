package com.phucle.fadu.game.objects;
/*
 * Created by lhphuc on 11/20/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.phucle.fadu.game.DupiAssets;

import static com.badlogic.gdx.math.MathUtils.random;

public class Tree extends GameObject {
    private Array<TextureAtlas.AtlasRegion> treeTextures = new Array<TextureAtlas.AtlasRegion>();
    private TextureRegion selectedTree;
    private Environment environment;
    public boolean isInScene;

    public Tree(Environment environment) {
        this.environment = environment;
        init();
    }

    private void init() {
        isInScene = true;

        treeTextures.add(environment.getAtlasStatic().findRegion("tree1"));
        treeTextures.add(environment.getAtlasStatic().findRegion("tree2"));
        treeTextures.add(environment.getAtlasStatic().findRegion("tree3"));
        treeTextures.add(environment.getAtlasStatic().findRegion("tree4"));

        //select meteor texture for the first time
        getRandomTree();

    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        position.x -= Duck.getDeltaPosition();

        if (position.x < - width - 50 * com.phucle.fadu.game.DupiAssets.scaleUnit) {
            isInScene = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isInScene) return;
        batch.draw(selectedTree, position.x, position.y, width, height);
    }

    private void getRandomTree() {
        // get random meteor texture from the list
        int id = (int) (Math.random() * treeTextures.size);
        selectedTree = treeTextures.get(id);

        position.x = com.phucle.fadu.game.DupiAssets.screenHeight + random(300, 1000);
        position.y = 50 * com.phucle.fadu.game.DupiAssets.scaleUnit;

        width = selectedTree.getRegionWidth() * com.phucle.fadu.game.DupiAssets.scaleUnit;
        height = selectedTree.getRegionHeight() * DupiAssets.scaleUnit;
    }

}
