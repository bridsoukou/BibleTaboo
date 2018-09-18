package com.mygdx.bibletaboo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.bibletaboo.BibleTaboo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x = 1;
		config.y = 1;
		config.width = 208;
		config.height = 400;
		config.title = "JW Word Guess";
		new LwjglApplication(new BibleTaboo(), config);
	}

}
