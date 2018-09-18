package com.mygdx.bibletaboo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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


public class SettingsScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private SpriteBatch batch;

    private BTAssetManager AsM;

    private Texture bg;
    private Table table;

    private Label timeLabel;
    private Label soundLabel;
    private Label languageLabel;
    private Label timeValueLabel;
    private Label soundValueLabel;
    private Label languageValueLabel;

    private Image playButton;
    private Image newGameButton;
    private Image plusButton;
    private Image minusButton;
    private Image helpButton;

    private int timeValue;
    private boolean soundValue;
    private String languageValue;

    private Sound newgameSound;
    private Sound pointSound;

    public SettingsScreen(BibleTaboo game){
        this.game = game;
        this.batch = game.batch;
        viewport = new FitViewport(BibleTaboo.V_WIDTH, BibleTaboo.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        AsM = new BTAssetManager();
        newgameSound = AsM.manager.get("sound/newgame.wav");
        pointSound = AsM.manager.get("sound/goodbeep2.wav");

        Gdx.input.setInputProcessor(stage);

        timeValue = GameScreen.timeValue;
        soundValue = GameScreen.soundValue;
        languageValue = GameScreen.languageValue;

        bg = new Texture("menu/background.png");

        Sprite playB = new Sprite(new Texture("menu/playbutton.png"));
        Sprite newgameB = new Sprite(new Texture("menu/newgamebutton.png"));
        Sprite plusB = new Sprite(new Texture("menu/plusbutton.png"));
        Sprite minusB = new Sprite(new Texture("menu/minusbutton.png"));
        Sprite helpB = new Sprite(new Texture("menu/helpbutton.png"));

        plusB.setSize(25,25);
        minusB.setSize(25, 25);
        playB.setSize(25,25);
        newgameB.setSize(25,25);
        helpB.setSize(25, 25);

        plusButton = new Image(new SpriteDrawable(plusB));
        minusButton = new Image(new SpriteDrawable(minusB));
        playButton = new Image(new SpriteDrawable(playB));
        newGameButton = new Image(new SpriteDrawable(newgameB));
        helpButton = new Image(new SpriteDrawable(helpB));

        setupLabels();
        setupTables();
        addListeners();
    }

    private void saveSettings(){
        BibleTaboo.mem.putInteger("StartingSeconds", timeValue);
        BibleTaboo.mem.putBoolean("SoundOn", soundValue);
        BibleTaboo.mem.putString("Language", languageValue);

        BibleTaboo.mem.flush();
    }

    private void playGame(){
        BibleTaboo.timeToLoadMemory = true;
        saveSettings();
        game.setScreen(new GameScreen((BibleTaboo)game));
        dispose();
    }

    private void wipeMemory(){
        BibleTaboo.timeMemory = timeValue;
        BibleTaboo.score1Memory = 0;
        BibleTaboo.score2Memory = 0;
        BibleTaboo.turnMemory = true;

        Gdx.app.log("Memory", "Wiped");
    }

    private void setupTables(){
        table = new Table();
        table.center().setFillParent(true);

        table.add(timeLabel);
        table.add(minusButton);
        table.add(timeValueLabel);
        table.add(plusButton);
        table.row();
        table.add(soundLabel);
        table.add();
        table.add(soundValueLabel);
        table.row();
        table.add(languageLabel);
        table.add();
        table.add(languageValueLabel);
        table.row();


        table.setDebug(false);

        Table buttonsTable = new Table();
        buttonsTable.setFillParent(true);
        buttonsTable.setPosition(buttonsTable.getX(), buttonsTable.getY()-50);
        buttonsTable.add(playButton);
        buttonsTable.add(newGameButton).pad(0,20,0,20);
        buttonsTable.add(helpButton);

        stage.addActor(buttonsTable);
        stage.addActor(table);
    }

    private void setupLabels(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(GameScreen.openSans, Color.BLACK);
        GameScreen.openSans.getData().setScale(0.5f);
        timeLabel = new Label("Time: ", labelStyle);
        soundLabel = new Label("Sound: ", labelStyle);
        languageLabel = new Label("Language: ", labelStyle);
        timeValueLabel = new Label("", labelStyle);
        soundValueLabel = new Label("", labelStyle);
        languageValueLabel = new Label("", labelStyle);
    }

    private void addListeners(){
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Play", "touchDown");
                playGame();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });
        newGameButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("newGameButton", "touchUp");
                GameScreen.playSound(newgameSound);
                wipeMemory();
                playGame();
            }
        });
        helpButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                viewInstructions();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });
        plusButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                timeValue += 5;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });
        minusButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                timeValue = timeValue == 5 ? 5 : timeValue - 5;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });
        soundValueLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(soundValue){
                    soundValue = false;
                } else {
                    soundValue = true;
                    GameScreen.playSound(pointSound);
                }
                Gdx.app.log("Sound", "touchDown");

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


            }
        });

    }

    @Override
    public void render(float delta) {
        updateText();

        Gdx.gl.glClearColor(.174f, .176f, .178f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();

        stage.draw();

    }

    private void updateText(){
        timeValueLabel.setText("" + timeValue);
        soundValueLabel.setText("" + soundValue);
        languageValueLabel.setText("" + languageValue);

        soundValueLabel.setAlignment(5);
        languageValueLabel.setAlignment(5);
    }

    private void viewInstructions(){
        game.setScreen(new InstructionsScreen((BibleTaboo)game));
        dispose();
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        AsM.manager.clear();
    }


}
