package com.mygdx.bibletaboo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.bibletaboo.BibleTaboo;
import com.mygdx.bibletaboo.tools.BTAssetManager;
import com.mygdx.bibletaboo.tools.WordSelector;

public class GameScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private SpriteBatch batch;
    private BTAssetManager AsM;

    public static int timeValue;
    public static boolean soundValue;
    public static String languageValue;
    public static boolean firstStartUp;

    private int team1Points;
    private int team2Points;
    private static int time;
    private float timeCount;
    private boolean team1Turn;
    private int Random;

    private boolean gameOn;
    private boolean needNewWord;
    private long buzzerSoundId;


    WordSelector Ws;

    Texture bg;
    Sprite playB;
    Sprite greenB;
    Sprite redB;
    Sprite settingsB;
    Sprite textBackground;

    Image playButton;
    Image greenButton;
    Image redButton;
    Image settingsButton;


    public static BitmapFont openSans;
    public static BitmapFont openSansGuessWord;
    private Label.LabelStyle style1;
    private Label.LabelStyle style2;
    private Label.LabelStyle style3;
    private Label timeLabel;
    private Label guessWord;
    private Label guess;
    private Label dontSay;
    private Label tabooWord1;
    private Label tabooWord2;
    private Label tabooWord3;
    private Label tabooWord4;
    private Label t1PointsLabel;
    private Label t2PointsLabel;

    private Sound buzzer;
    private Sound pointSound;
    private Sound passSound;

    private boolean timeToStopBuzzer;

    public GameScreen(BibleTaboo game){

        Ws = new WordSelector();
        Random = Ws.randomizer();
        AsM = new BTAssetManager();

        buzzer = AsM.manager.get("sound/bell0.wav");
        pointSound = AsM.manager.get("sound/goodbeep2.wav");
        passSound = AsM.manager.get("sound/badbeep.wav");

        timeToStopBuzzer = false;

        this.game = game;
        this.batch = game.batch;
        viewport = new FitViewport(BibleTaboo.V_WIDTH, BibleTaboo.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(stage);

        playB = new Sprite(new Texture("menu/playbutton.png"));
        greenB = new Sprite(new Texture("menu/greenbutton.png"));
        redB = new Sprite(new Texture("menu/redbutton.png"));
        settingsB = new Sprite(new Texture("menu/settings.png"));
        textBackground = new Sprite(new Texture("menu/transparent.png"));

        bg = new Texture("menu/background.png");
        playButton = new Image(playB);
        greenButton = new Image(greenB);
        redButton = new Image(redB);
        settingsButton = new Image(new SpriteDrawable(settingsB));

        team1Points = 0;
        team2Points = 0;
        team1Turn = true;
        gameOn = false;
        needNewWord = false;


        BibleTaboo.mem.putBoolean("FirstStartUp", false);
        BibleTaboo.mem.flush();

        openSans = new BitmapFont(Gdx.files.internal("fonts/opensans1.fnt"));
        openSansGuessWord = new BitmapFont(Gdx.files.internal("fonts/opensansGuessWord.fnt"));

        loadPreferences();
        loadMemory();
        setupLabels();
        setupTables();
        addListeners();


    }

    private void setupTables(){
        Table table3 = new Table();
        table3.top();
        table3.setFillParent(true);

        table3.add(settingsButton).padTop(5);

        Table table2 = new Table();
        table2.center().setFillParent(true);
        table2.add();
        table2.add(guess).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(guessWord).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(dontSay).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(tabooWord1).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(tabooWord2).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(tabooWord3).expandX();
        table2.add();
        table2.row();
        table2.add();
        table2.add(tabooWord4).expandX().padBottom(15);
        table2.add();
        table2.row();

        Table buttonTable = new Table();
        buttonTable.bottom();
        buttonTable.setFillParent(true);

        buttonTable.add(t1PointsLabel).expandX();
        buttonTable.add(timeLabel);
        buttonTable.add(t2PointsLabel).expandX();
        buttonTable.row();
        buttonTable.add(redButton).expandX();
        buttonTable.add(playButton).pad(5,0,5,0);
        buttonTable.add(greenButton).expandX();

//        table2.setDebug(true);

        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(buttonTable);
    }

    private void setupLabels(){
        style1 = new Label.LabelStyle(openSans, Color.DARK_GRAY);
        style2 = new Label.LabelStyle(openSans, Color.DARK_GRAY);
        style3 = new Label.LabelStyle(openSansGuessWord, Color.DARK_GRAY);

        guess = new Label("-Guess-", style1);
        guess.setFontScale(0.5f);
        dontSay = new Label("-Don't Say-", style1);
        dontSay.setFontScale(0.5f);
        timeLabel = new Label(String.format("%02d", BibleTaboo.timeMemory), style3);
        t1PointsLabel = new Label("Team 1: \n" + team1Points, style1);
        t1PointsLabel.setFontScale(0.5f);
        t2PointsLabel = new Label("Team 2: \n" + team2Points, style1);
        t2PointsLabel.setFontScale(0.5f);
        guessWord = new Label("" + Ws.getGuessWord(Random), style1);
        tabooWord1 = new Label("" + Ws.getTabooWord(Random, 1), style1);
        tabooWord1.setFontScale(0.5f);
        tabooWord2 = new Label("" + Ws.getTabooWord(Random, 2), style1);
        tabooWord2.setFontScale(0.5f);
        tabooWord3 = new Label("" + Ws.getTabooWord(Random, 3), style1);
        tabooWord3.setFontScale(0.5f);
        tabooWord4 = new Label("" + Ws.getTabooWord(Random, 4), style1);
        tabooWord4.setFontScale(0.5f);
    }

    private void addListeners(){
        greenButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("greenButton", "touchDown");

                greenB.setColor(0,0.5f,0,1);
                greenB.setSize(redB.getWidth() + 3, redB.getHeight() + 3);
                greenButton.setDrawable(new SpriteDrawable(greenB));

                if(team1Turn && gameOn){
                    team1Points++;
                    playSound(pointSound);
                    Gdx.app.log("Team1", "Point");
                    Random = Ws.randomizer();
                    Gdx.app.log("Random", "=" + Random);
                } else if (gameOn) {
                    team2Points++;
                    playSound(pointSound);
                    Gdx.app.log("Team2", "Point");
                    Random = Ws.randomizer();
                    Gdx.app.log("Random", "=" + Random);
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                greenB.setColor(1,1,1,1);
                greenB.setSize(greenB.getWidth() - 3, greenB.getHeight() - 3);
                greenButton.setDrawable(new SpriteDrawable(greenB));


            }
        });
        redButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("redButton", "touchDown");

                redB.setColor(0.5f,0,0,1);
                redB.setSize(greenB.getWidth() + 3, greenB.getHeight() + 3);

                if(gameOn){
                    Random = Ws.randomizer();
                        playSound(passSound);
                    Gdx.app.log("Random", "=" + Random);
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                redB.setColor(1,1,1,1);
                redB.setSize(redB.getWidth() - 3, redB.getHeight() - 3);



            }
        });
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("playButton", "touchDown");

                playB.setColor(0.54f,0.54f,0.54f,1);
                playB.setSize(playB.getWidth() + 3, playB.getHeight() + 3);


                if(!gameOn) {
                    gameOn = true;
                    updateText();
                } else {
                    gameOn = false;
                    updateText();
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                playB.setColor(1,1,1,1);
                playB.setSize(playB.getWidth() - 3, playB.getHeight() - 3);


            }
        });
        settingsButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("settingsButton", "touchDown");

                settingsScreen();

                if(!gameOn) {
                    gameOn = true;
                    updateText();
                } else {
                    gameOn = false;
                    updateText();
                }



                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });
    }

    private void updateButtons(){

        greenButton.setDrawable(new SpriteDrawable(greenB));
        redButton.setDrawable(new SpriteDrawable(redB));
        playButton.setDrawable(new SpriteDrawable(playB));

    }

    private void update(float delta) {
        updateMemory();

        if(needNewWord){
            Random = Ws.randomizer();
            needNewWord = false;
        }

        if(time <= 0){
            timeOut();
        }

        //Timer
        timeCount += delta;
        if(timeCount >= 1){
            time--;
            timeCount = 0;
        }

        if(gameOn && timeToStopBuzzer){

            //Attempting to fade the sound out.
            for(int time = 1; time > 0; time-= delta){
                buzzer.setVolume(buzzerSoundId, time);
            }
            buzzer.stop();
            timeToStopBuzzer = false;
        }
    }

    private void updateText(){

        timeLabel.setText(String.format("%02d", time));

        t1PointsLabel.setText("Team 1: \n       " + team1Points);
        t2PointsLabel.setText("Team 2: \n       " + team2Points);

//        textBackground.setSize(t1PointsLabel.getPrefWidth() + 10, t1PointsLabel.getPrefHeight() + 10);
        style2.background = new SpriteDrawable(textBackground);

//        dynamicFontScaleX(t1PointsLabel, 71.5f, 0.5f);
//        dynamicFontScaleX(t2PointsLabel, 71.5f, 0.5f);

        if(team1Turn){
            t1PointsLabel.setStyle(style2);
            t2PointsLabel.setStyle(style1);
        } else {
            t1PointsLabel.setStyle(style1);
            t2PointsLabel.setStyle(style2);
        }

        if(gameOn) {
            guessWord.setText(Ws.getGuessWord(Random));
            tabooWord1.setText(Ws.getTabooWord(Random, 1));
            tabooWord2.setText(Ws.getTabooWord(Random, 2));
            tabooWord3.setText(Ws.getTabooWord(Random, 3));
            tabooWord4.setText(Ws.getTabooWord(Random, 4));

            dynamicFontScaleX(guessWord, 200, 1);
            dynamicFontScaleX(tabooWord1, 200, 0.5f);
            dynamicFontScaleX(tabooWord2, 200, 0.5f);
            dynamicFontScaleX(tabooWord3, 200, 0.5f);
            dynamicFontScaleX(tabooWord4, 200, 0.5f);
        } else {
            guessWord.setText("");
            tabooWord1.setText("");
            tabooWord2.setText("");
            tabooWord3.setText("");
            tabooWord4.setText("");
        }
    }

    private void loadPreferences(){
        timeValue = BibleTaboo.mem.getInteger("StartingSeconds", 60);
        soundValue = BibleTaboo.mem.getBoolean("SoundOn", true);
        languageValue = BibleTaboo.mem.getString("Language", "English");
        firstStartUp = BibleTaboo.mem.getBoolean("FirstStartUp", true);
        time = timeValue;
    }

    private void loadMemory(){
        if(BibleTaboo.timeToLoadMemory){
            if(BibleTaboo.timeMemory != 0)
                time = BibleTaboo.timeMemory;
            team1Points = BibleTaboo.score1Memory;
            team2Points = BibleTaboo.score2Memory;
            team1Turn = BibleTaboo.turnMemory;
            Ws.setUsedBeforeList(BibleTaboo.usedMemory);

            Gdx.app.log("Memory", "Loaded");
        }
    }

    private void updateMemory(){

        if(time != 0)
            BibleTaboo.timeMemory = time;
        BibleTaboo.score1Memory = team1Points;
        BibleTaboo.score2Memory = team2Points;
        BibleTaboo.turnMemory = team1Turn;
        BibleTaboo.usedMemory = Ws.getUsedBeforeList();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.174f, .176f, .178f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateText();
        updateButtons();

        if(gameOn) {
            update(delta);
            playButton.setDrawable(new SpriteDrawable(new Sprite(new Texture("menu/pausebutton.png"))));
        } else {
            playButton.setDrawable(new SpriteDrawable(new Sprite(new Texture("menu/playbutton.png"))));
        }

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();

        stage.draw();

    }

    private void timeOut(){
        gameOn = false;
        needNewWord = true;

        if(team1Turn){
            team1Turn = false;
        } else {
            team1Turn = true;
        }

        if(soundValue) {
            buzzerSoundId = playSound(buzzer);
            timeToStopBuzzer = true;
        }

        time = timeValue;

    }

    private void dynamicFontScaleX(Label labelName, float maxPrefWidth, float startingValue){
        labelName.setFontScale(startingValue);

        if(labelName.getPrefWidth() > maxPrefWidth){
            boolean textTooLarge = true;
            float x = startingValue;

            while(textTooLarge){

                labelName.setFontScaleX(x);
                x -= 0.1;

                if(labelName.getPrefWidth() < maxPrefWidth){
                    textTooLarge = false;
                }
            }
        }
    }

    public static long playSound(Sound sound){
        long soundId = 0;

        if(soundValue) {
            soundId = sound.play();
        }

        return soundId;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        gameOn = false;

    }

    @Override
    public void dispose() {
        stage.dispose();
        AsM.manager.clear();
    }

    private void settingsScreen(){
        game.setScreen(new SettingsScreen((BibleTaboo)game));
        dispose();
    }



}
