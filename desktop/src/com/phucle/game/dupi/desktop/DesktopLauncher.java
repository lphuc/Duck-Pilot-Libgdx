package com.phucle.game.dupi.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.phucle.game.dupi.DupiMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//set game screen size on Desktop
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new DupiMain(), config);

	}
}
