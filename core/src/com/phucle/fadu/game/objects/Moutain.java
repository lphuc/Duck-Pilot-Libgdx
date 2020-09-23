/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package com.phucle.fadu.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.phucle.fadu.game.DupiAssets;

public class Moutain extends GameObject {

    private static TextureRegion regMountainLeft;
    private static TextureRegion regMountainRight;
    public static Vector2 dimension = new Vector2(1, 1);
    public static Vector2 origin = new Vector2();
    public static Vector2 scale = new Vector2(1, 1);
    private static Environment environment;
    public static int length;
    public static float xPosition;

    private float rotation = 0;

    public Moutain(Environment environment) {
        Moutain.environment = environment;
        init();
    }

    private void init() {
        xPosition = 0;
        length = 0;
        // ti le 5:1

        dimension.set(1280 * com.phucle.fadu.game.DupiAssets.scaleUnit, 252 * com.phucle.fadu.game.DupiAssets.scaleUnit);

        regMountainLeft = environment.getAtlasStatic().findRegion("moutain_left");
        regMountainRight = environment.getAtlasStatic().findRegion("moutain_right");
        // shift mountain and extend length
//        origin.x = -dimension.x * 2;
        origin.x = Duck.getDeltaPosition() - 1280 * com.phucle.fadu.game.DupiAssets.scaleUnit;
        length += dimension.x * 2;
    }

    private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX) {
        TextureRegion reg = null;
        batch.setColor(tintColor, tintColor, tintColor, 1);
        float xRel = dimension.x * offsetX;
        float yRel = dimension.y * offsetY;

        // mountains span the whole level
        int mountainLength = 0;
        mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
        mountainLength += MathUtils.ceil(0.5f + offsetX);
        for (int i = 0; i < mountainLength; i++) {
            // mountain left
            reg = regMountainLeft;
            batch.draw(reg.getTexture(), origin.x + xRel + xPosition * parallaxSpeedX, origin.y + yRel + position.y, origin.x,
                    origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            xRel += dimension.x;
            // mountain right
            reg = regMountainRight;
            batch.draw(reg.getTexture(), origin.x + xRel + xPosition * parallaxSpeedX, origin.y + yRel + position.y, origin.x,
                    origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            xRel += dimension.x;
        }
        // reset color to white
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public void update(float deltaTime) {
        // TODO: 11/13/2018 tránh làm game bị lag do render núi quá dài
        xPosition -= Duck.getDeltaPosition();
        origin.x = Duck.getDeltaPosition() - 1280 * DupiAssets.scaleUnit;
        length += Duck.getDeltaPosition();
    }

    @Override
    public void render(SpriteBatch batch) {

        // 80% distant mountains (dark gray)
        drawMountain(batch, 0.75f, 0.75f, 0.84f, 0.08f);
        // 50% distant mountains (gray)
        drawMountain(batch, 0.5f, 0.5f, 0.73f, 0.2f);
        // 30% distant mountains (light gray)
        drawMountain(batch, 0.15f, 0.15f, 0.62f, 0.35f);
    }


}
