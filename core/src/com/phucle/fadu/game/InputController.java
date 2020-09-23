package com.phucle.fadu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.phucle.fadu.screens.MainGameScreen;
import com.phucle.fadu.utils.AudioManager;

/*
 * Created by Phuc Engineer on 10/21/2018.
 */
public class InputController {

    private static WorldUpdater worldUpdater;

    //dùng để tính lực đẩy tác dụng lên máy bay khi touch vào  màn hình
    private static final float TOUCH_IMPULSE = 380 * com.phucle.fadu.game.DupiAssets.scaleUnit;

    public Vector3 touchPosition;

    //sử dụng như một unit vector (direction vector)
    public Vector2 tmpVector;

    // ấn càng lâu càng bay nhanh
    private float holdingTime;

    public InputController(WorldUpdater controller) {
        worldUpdater = controller;
        initController();
    }

    private void initController() {
        tmpVector = new Vector2();
        touchPosition = new Vector3();
        holdingTime = 1;

        float check = 380 * com.phucle.fadu.game.DupiAssets.scaleUnit;
//        Gdx.input.setInputProcessor(this);
    }

    public void checkUserInput() {

        if (Gdx.input.isTouched() && !MainGameScreen.disableInput) {

            //resume game when click on button to close advertisement
            if (worldUpdater.gameState == WorldUpdater.GameState.PAUSE) {
                return;
            }

            if (worldUpdater.gameState == WorldUpdater.GameState.INIT) {
                worldUpdater.gameState = WorldUpdater.GameState.ACTION;
                return;
            }

            if (worldUpdater.gameState == WorldUpdater.GameState.GAME_OVER) {
                worldUpdater.gameState = WorldUpdater.GameState.INIT;
                worldUpdater.resetScene();
                holdingTime = 1;
                return;
            }

            if (holdingTime == 1 && !worldUpdater.duck.grounded)
                return;

            if (holdingTime == 1) {
                AudioManager.getInstance().play(com.phucle.fadu.game.DupiAssets.getInstance().getJumpSound(), (float) 0.4);
            }

            // TODO: 2/10/2019  
            worldUpdater.duck.getVelocity().set(worldUpdater.duck.getVelocity().x, TOUCH_IMPULSE * holdingTime);

            holdingTime = holdingTime + DupiAssets.scaleUnit / 10;

        } else {
            holdingTime = 1;
        }
    }

//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        if (isCircleRocket) {
//            worldUpdater.level.spawnRocket();
//        }
//        return false;
//    }
}

