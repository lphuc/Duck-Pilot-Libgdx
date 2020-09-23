package com.phucle.fadu.utils;
/*
 * Created by LENOVO on 10/18/2017.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.phucle.fadu.game.DupiAssets;

/*A camera can be set to any width or height and does not relate to the pixel dimensions
 of the screentake the window size and create a camera that respects the aspect ratio */

/*Viewport is used for Handling multiple screen sizes and aspect ratios:
* • StretchViewport: This will stretch the virtual viewport to it the screen.
• FitViewport: This will it the virtual viewport within the screen with black
bars that ill the additional area.
• FillViewport: This is similar to FitViewport but there won't be any
black bars, as it will always ill the screen with the fixed aspect ratio. This
means some part of the game will get cut off or fall outside the screen.
• ScreenViewport: This will always match the window size without any
scaling or black bars. */

public class MyCamera {
    private static OrthographicCamera camera = null;

    private MyCamera() {
    }

    public static OrthographicCamera getCamera() {

        if (camera == null) {
            camera = new OrthographicCamera();
            camera.setToOrtho(false, DupiAssets.screenHeight, DupiAssets.screenWidth);
        }
        return camera;
    }

}
