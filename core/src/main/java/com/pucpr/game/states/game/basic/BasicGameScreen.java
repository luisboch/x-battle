package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix3;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.pucpr.game.Aircrafts;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.Keys;
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
import java.util.ArrayList;
import java.util.List;

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

    protected Player player;
    protected Player player2;
    protected ActorControl playerControl;
    protected ActorControl playerControl2;

    protected List<Planet> planet = new ArrayList<Planet>();

    public BasicGameScreen(final String mapFile) {
        map = new TmxMapLoader().load("data/maps/" + mapFile);
    }

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / GameConfig.PPM, Gdx.graphics.getHeight() / GameConfig.PPM);

        camera.position.x = 0;
        camera.position.y = 0;

        player = Aircrafts.getNewPlayer(PlayerStatus.getInstance().intKey(Keys.AIRCRAFT));
        player2 = Aircrafts.getNewPlayer(PlayerStatus.getInstance().intKey(Keys.AIRCRAFT));

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

        playerControl2 = service.insert(player2);
        player2.setPosition(new Vector2(2500, 2500));
        playerControl = service.insert(player);
        player.setPosition(new Vector2(2000, 2000));

        for (int i = 1; i < 5; i++) {
            Planet p = new Planet();
            p.setPosition(new Vector2(2000 * i, 1500 * i));
            service.insertPlanet(p);

        }
    }

    @Override
    public void close() {

    }

    @Override
    public void render() {
        calculate();
        long start = TimeUtils.nanoTime();
////
//        camera.position.x = player.getPosition().x;
//        camera.position.y = player.getPosition().y;

        Vector2 p2Pos = player2.getPosition().cpy();

        // Cancula a distancia e pega a metade 
        //**** no p2Pos vai ficar apenas o vetor resultante do calculo, ou seja modificado ****
        float dist = p2Pos.sub(player.getPosition()).len() * 0.5f;
        
        // Vamos pegar a direcao e multiplicar pela metade da distancia.
        p2Pos.nor().scl(dist);
        
        // Adicionamos o menor
        p2Pos.add(player.getPosition());
        
        camera.position.x = p2Pos.x;
        camera.position.y = p2Pos.y;
//        
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

        final int[] baseMap = {0, 1, 2, 3};

        final int[] topMap = {4, 5};

        batch.getProjectionMatrix().set(camera.combined);
        render.setView(camera);

        render.render(baseMap);

        batch.begin();
        for (ActorObject obj : service.getActors()) {
            obj.draw(batch, new Matrix3().idt(), camera);
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

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerControl.setUp(true);
        } else {
            playerControl.setUp(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerControl2.setUp(true);
        } else {
            playerControl2.setUp(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerControl.setLeft(true);
        } else {
            playerControl.setLeft(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerControl2.setLeft(true);
        } else {
            playerControl2.setLeft(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerControl.setRight(true);
        } else {
            playerControl.setRight(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerControl2.setRight(true);
        } else {
            playerControl2.setRight(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            playerControl.setAction1(true);
        } else {
            playerControl.setAction1(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
            playerControl2.setAction1(true);
        } else {
            playerControl2.setAction1(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            playerControl.setAction2(true);
        } else {
            playerControl.setAction2(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            playerControl2.setAction2(true);
        } else {
            playerControl2.setAction2(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            playerControl.setAction3(true);
        } else {
            playerControl.setAction3(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            playerControl2.setAction3(true);
        } else {
            playerControl2.setAction3(false);
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
