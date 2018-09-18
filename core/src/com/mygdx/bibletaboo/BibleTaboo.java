package com.mygdx.bibletaboo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bibletaboo.screens.GameScreen;
import com.mygdx.bibletaboo.screens.InstructionsScreen;

import java.util.ArrayList;
import java.util.List;

public class BibleTaboo extends Game {
	public static final int V_WIDTH = 208;
	public static final int V_HEIGHT = 400;


    public static Preferences mem;

	public static boolean timeToLoadMemory;
	public static int score1Memory;
	public static int score2Memory;
	public static int timeMemory;
	public static List<Integer> usedMemory;
	public static boolean turnMemory;

	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();

        mem = Gdx.app.getPreferences("Bible Taboo Memory");
		timeToLoadMemory = false;
		usedMemory = new ArrayList<Integer>();

		boolean FirstStartUp = BibleTaboo.mem.getBoolean("FirstStartUp", true);

		if(FirstStartUp){
			setScreen(new InstructionsScreen(this));
			Gdx.app.log("FirstStartUp", "True");
		} else {
			setScreen(new GameScreen(this));
		}


	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

}

