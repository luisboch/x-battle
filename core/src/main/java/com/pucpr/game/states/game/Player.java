/**
 * Player.class
 */
package com.pucpr.game.states.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.Projectile;
import com.pucpr.game.states.game.engine.SimpleShot;
import com.pucpr.game.states.game.engine.World;
import com.pucpr.game.states.game.engine.steering.ProjectileSteering;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Player extends ActorObject {

    private final Map<State, Animation> animation = new HashMap<State, Animation>();
    private State state = State.IDLE;
    float stateTime;

    public Player() {
        super(25, 1f, 500, 50, 50);
        loadAnnimation();
    }

    private void loadAnnimation() {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/ships/SR-91A.png");
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation.put(State.IDLE, new Animation(01f, split[0][0]));
        animation.put(State.RUNNING, new Animation(01f, split[0][0]));
        stateTime = 0f;
    }

    private static enum State {
        IDLE,
        RUNNING;
    }

    @Override
    public TextureRegion getTexture() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            return animation.get(State.RUNNING).getKeyFrame(stateTime);
        }
        return animation.get(state).getKeyFrame(stateTime);
    }

    public Projectile action1(World w) {
        return new SimpleShot(new ProjectileSteering(), this);
    }
}
