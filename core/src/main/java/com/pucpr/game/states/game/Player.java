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
        super(15, 1f, 500, 50, 50);
        loadAnnimation();
    }

    public Player(final float mass, final float maxVel) {
        super(15, mass, maxVel, 50, 50);
        loadAnnimation();
    }

    private void loadAnnimation() {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/nave.png");
        TextureRegion[][] split = TextureRegion.split(texture, 50, 50);

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

}
