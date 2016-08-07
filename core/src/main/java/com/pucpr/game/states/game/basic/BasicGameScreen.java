package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.server.ActorControl;
import com.pucpr.game.server.GameService;
import com.pucpr.game.server.LocalhostService;
import com.pucpr.game.server.RemoteSevice;
import com.pucpr.game.states.GameScreenState;
import com.pucpr.game.states.game.GameState;
import com.pucpr.game.states.game.Planet;
import com.pucpr.game.states.game.Player;
import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author luis
 */
public class BasicGameScreen implements GameScreenState {

    private static final Float MAX_VELOCITY = 8f;

    private OrthographicCamera camera;
    protected AppManager manager;
    protected GameState gameState;

    /**
     * the immediate mode renderer to output our debug drawings *
     */
    private ShapeRenderer renderer;

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;

    private GameService service;

    protected TiledMap map;
    protected MapRenderer render;
    protected Vector3 mousePos = new Vector3();

    protected Player player = new Player();
    protected ActorControl playerControl;

    protected Planet planet = new Planet();
    protected Planet planet2 = new Planet();

    public BasicGameScreen(final String mapFile) {
        map = new TmxMapLoader().load("data/maps/" + mapFile);
    }

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / GameConfig.PPM, Gdx.graphics.getHeight() / GameConfig.PPM);

        camera.position.x = 0;
        camera.position.y = 0;

        render = new OrthogonalTiledMapRenderer(map, 1f / GameConfig.PPM);
        // next we setup the immediate mode renderer
        renderer = new ShapeRenderer();

        // next we create a SpriteBatch and a font
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
        font.setColor(Color.RED);

        if (GameConfig.serverHost == null) {
            service = new LocalhostService();
        } else {
            service = new RemoteSevice(GameConfig.serverHost);
        }

        playerControl = service.insert(player);
        service.insertPlanet(planet);
        service.insertPlanet(planet2);

        planet.setPosition(new Vector2(100, 100));
        planet2.setPosition(new Vector2(-100, -100));

    }

    @Override
    public void close() {

    }

    @Override
    public void render() {
        calculate();
        long start = TimeUtils.nanoTime();

        camera.position.x = player.getPosition().x;
        camera.position.y = player.getPosition().y;
        camera.update();
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos = camera.unproject(mousePos);
        service.calculate();

        if (!GameConfig.showDebug) {
            renderApp();
        } else {
            throw new IllegalStateException("Not implemented yet!");
//            renderDebug();
        }

    }

    @SuppressWarnings("empty-statement")
    private void renderApp() {

        final PlayerStatus status = PlayerStatus.getInstance();

        int[] baseMap = {0, 1, 2, 3};

        int[] topMap = {4, 5};

        batch.getProjectionMatrix().set(camera.combined);
        render.setView(camera);

        render.render(baseMap);

        batch.begin();

        for (ActorObject obj : service.getActors()) {

            Vector2 position = obj.getPosition(); // that's the box's center position

            float angle = obj.getDirection().angle() - 90;
//            System.out.println("position: " + position + ", angle: " + angle + ", size: " + obj.getSize());
            final TextureRegion texture = obj.getTexture();
            if (texture != null) {

                final Sprite sprite = new Sprite(texture);
                sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - (sprite.getHeight() / 2));
                sprite.setRotation(angle);
                sprite.setScale(obj.getSize().x / sprite.getWidth(), obj.getSize().y / sprite.getHeight());

                sprite.draw(batch);
            }
        }

        batch.end();
        render.render(topMap);

    }

    private void calculate() {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            playerControl.setForce(true);
        } else {
            playerControl.setForce(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerControl.setUp(true);
        } else {
            playerControl.setUp(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerControl.setLeft(true);
        } else {
            playerControl.setLeft(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerControl.setRight(true);
        } else {
            playerControl.setRight(false);
        }
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
