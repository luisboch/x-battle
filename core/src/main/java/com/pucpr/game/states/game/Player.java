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
import com.pucpr.game.states.game.engine.Shot;
import com.pucpr.game.states.game.engine.SimpleShot;
import com.pucpr.game.states.game.engine.World;
import com.pucpr.game.states.game.engine.steering.ProjectileSteering;
import com.pucpr.game.states.game.engine.steering.Pursuit;
import com.pucpr.game.states.game.engine.steering.WeightSteeringStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        super(25, 1f, 200, 50, 50);
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

    public Projectile action2(World w) {
        
        final List<Class<? extends ActorObject>> target = new ArrayList<Class<? extends ActorObject>>();
        
        final List<ActorObject> ignored = new ArrayList<ActorObject>();
        ignored.add(this);
        target.add(Player.class);
        
        final Player other = (Player) w.getClosestActor(this.getPosition(), 2000f, target, ignored);

        final WeightSteeringStrategy stg = new WeightSteeringStrategy();
        stg.add(new Pursuit(other), 100);
        stg.add(new ProjectileSteering(), 100);

        return new Shot(stg, this, other, 100);
    }

    public Class<? extends Projectile> getAction1Type() {
        return SimpleShot.class;
    }

    public Class<? extends Projectile> getAction2Type() {
        return Shot.class;
    }

    public Class<? extends Projectile> getAction3Type() {
        return SimpleShot.class;
    }
}
