package com.mygdx.bibletaboo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.mygdx.bibletaboo.tools.SimpleDirectionGestureDetector;

public class InstructionsScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;
    private Game game;

    private InputMultiplexer inputMultiplexer;

    private boolean timeToGoLeft;
    private boolean timeToGoRight;

    private float lastVelocityX;
    private int pageNumber;

    private Texture bg = new Texture("menu/background.png");
    private Texture pipTexture = new Texture("menu/lightpip.png");

    private Sprite darkPipSprite = new Sprite(new Texture("menu/pip.png"));
    private Image[] pipsArray = new Image[]{
            new Image(new Sprite(pipTexture)),
            new Image(new Sprite(pipTexture)),
            new Image(new Sprite(pipTexture)),
            new Image(new Sprite(pipTexture)),
            new Image(new Sprite(pipTexture))
    };

    public InstructionsScreen(BibleTaboo game){
        this.game = game;
        this.batch = game.batch;
        viewport = new FitViewport(BibleTaboo.V_WIDTH, BibleTaboo.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(stage);

        pageNumber = 1;

        setupLabels();
        setupTables();
        addListeners();

        timeToGoLeft = false;
    }


    private Label instructions1;

    private void addListeners(){

        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                pageNumber++;
                instructions1.setText(getPageText(pageNumber));
                if(pageNumber > 5)
                    doneWithInstructions();
            }
        });
    }

    private void setupLabels(){
        BitmapFont openSans = new BitmapFont(Gdx.files.internal("fonts/opensans1.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(openSans, Color.DARK_GRAY);

        openSans.getData().setScale(0.5f);

        instructions1 = new Label(getPageText(pageNumber), labelStyle);
        instructions1.setWrap(true);
        instructions1.setTouchable(null);
    }

    private void setupTables(){
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(instructions1).width(150);
        table.setWidth(200);

        Table pips = new Table();
        pips.setFillParent(true);
        pips.bottom();
        pips.add(pipsArray[0]).pad(5);
        pips.add(pipsArray[1]).pad(5);
        pips.add(pipsArray[2]).pad(5);
        pips.add(pipsArray[3]).pad(5);
        pips.add(pipsArray[4]).pad(5);

        stage.addActor(table);
        stage.addActor(pips);
    }

    private String getPageText(int pageNumber){
        FileHandle handle;
        String text;
        String[] words;
        handle = Gdx.files.internal("data/instructions.txt");
        text = handle.readString();
        words = text.split("\n");

        for(int x = 0; x < words.length; x++){
            words[x] = words[x].trim();
        }

        switch(pageNumber){
            case 1:
                return words[0];
            case 2:
                return words[1];
            case 3:
                return words[2];
            case 4:
                return words[3];
            case 5:
                return words[4];
        }

        return "Null";
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.174f, .176f, .178f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();

        stage.draw();

        for(Image pip : pipsArray){
            pip.setDrawable(new SpriteDrawable(new Sprite(pipTexture)));
        }

        switch(pageNumber){
            case 1:
                pipsArray[0].setDrawable(new SpriteDrawable(darkPipSprite));
                break;
            case 2:
                pipsArray[1].setDrawable(new SpriteDrawable(darkPipSprite));
                break;
            case 3:
                pipsArray[2].setDrawable(new SpriteDrawable(darkPipSprite));
                break;
            case 4:
                pipsArray[3].setDrawable(new SpriteDrawable(darkPipSprite));
                break;
            case 5:
                pipsArray[4].setDrawable(new SpriteDrawable(darkPipSprite));
                break;
        }

    }

/*    private void moveLabel(Label label){
        label.setPosition(label.getX() + lastVelocityX/1000 * 4, label.getY());
        if(label.getX() > BibleTaboo.V_WIDTH || label.getX() < -100){
            label.setText(getPageText(pageNumber));
            label.setX(BibleTaboo.V_WIDTH/2);
            timeToGoLeft = false;
            timeToGoRight = false;
        }
    }*/

    private void doneWithInstructions(){
        if(BibleTaboo.mem.getBoolean("FirstStartUp", true)){
            game.setScreen(new GameScreen((BibleTaboo)game));
            BibleTaboo.timeToLoadMemory = true;
        } else {
            game.setScreen(new SettingsScreen((BibleTaboo) game));
        }
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
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }
}
