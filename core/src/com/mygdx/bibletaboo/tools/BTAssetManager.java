package com.mygdx.bibletaboo.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class BTAssetManager {
    public final AssetManager manager = new AssetManager();

        public BTAssetManager() {

            manager.load("sound/bell0.wav", Sound.class);
            manager.load("sound/goodbeep2.wav", Sound.class);
            manager.load("sound/badbeep.wav", Sound.class);
            manager.load("sound/newgame.wav", Sound.class);

            manager.finishLoading();
        }
}
