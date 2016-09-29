/**
 * ForceField.class
 */
package com.pucpr.game.states.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 * @email felipe
 * @since Aug 10, 2016
 */
public class ForceField extends ActorObject {

    private final Map<State, Animation> animation = new EnumMap<State, Animation>(State.class);
    private State state = State.IDLE;
    float stateTime;

    public ForceField() {
        super(25, 1f, 200, 50, 50);
        loadAnnimation();
        setDirection(new Vector2(0.0001f, 0.0001f));
    }

    private void loadAnnimation() {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/disasteroids/fire2.png");
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation.put(State.IDLE, new Animation(01f, split[0][0]));
        stateTime = 0f;
    }

    private static enum State {
        IDLE;
    }

    @Override
    protected TextureRegion getTexture() {
        stateTime += Gdx.graphics.getDeltaTime();
        return animation.get(state).getKeyFrame(stateTime);
    }

    @Override
    protected void tick() {
        setDirection(getDirection().nor().rotate(50f * Gdx.graphics.getDeltaTime()));
    }

}
