package com.phucle.fadu.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Created by lhphuc on 10/15/2018.
 */
public interface ScreenTransition {
     float getDuration();
     void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
