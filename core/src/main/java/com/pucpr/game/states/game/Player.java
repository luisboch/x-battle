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
import com.pucpr.game.states.game.engine.MineShot;
import com.pucpr.game.states.game.engine.ParticleEmitter;
import com.pucpr.game.states.game.engine.Projectile;
import com.pucpr.game.states.game.engine.PursuitShot;
import com.pucpr.game.states.game.engine.Shot;
import com.pucpr.game.states.game.engine.SimpleShot;
import com.pucpr.game.states.game.engine.World;
import com.pucpr.game.states.game.engine.steering.ProjectileSteering;
import com.pucpr.game.states.game.engine.steering.Pursuit;
import com.pucpr.game.states.game.engine.steering.WeightSteeringStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Player extends ActorObject {

    private static final int INIT_HEALTH = 3000;

    private final String warcraft;
    private Animation animation;
    private float stateTime;
    private final ParticleEmitter rocketEmitter = new ParticleEmitter("rocket.party");

    public Player(String warcraft) {
        super(25, 1f, 200, 50, 50);
        this.warcraft = warcraft;
        
        final ForceField f1 = new ForceField();
        f1.getPivot().x = 70;

        final ForceField f2 = new ForceField();
        f2.getPivot().x = 70;
        f2.setDirection(f1.getDirection().rotate(360 / 3));

        final ForceField f3 = new ForceField();
        f3.getPivot().x = 70;
        f3.setDirection(f2.getDirection().rotate(360 / 3));

        rocketEmitter.getPivot().x = -12;
        rocketEmitter.getPivot().y = 12f;
        rocketEmitter.setFollowParentAngle(true);

        getListActorObject().add(f1);
        getListActorObject().add(f2);
        getListActorObject().add(f3);

        getListActorObject().add(rocketEmitter);
        
        // Create health
        setHealth(INIT_HEALTH);
    }

    @Override
    protected void setupGraphics() {
        loadAnnimation(warcraft);

    }
    
    protected void loadAnnimation(String spriteWarcrafit) {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture(spriteWarcrafit);
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation = new Animation(stateTime, split[0][0]);
        stateTime = 0f;
    }

    @Override
    protected TextureRegion getTexture() {
        stateTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(stateTime);
    }

    public Projectile action1(World w) {
        final SimpleShot shot = new SimpleShot(new ProjectileSteering(), this);
        shot.setInitialVelocity(getVelocity().len() + 400);
        return shot;
    }

    public Projectile action2(World w) {

        final List<Class<? extends ActorObject>> target = new ArrayList<Class<? extends ActorObject>>();

        final List<ActorObject> ignored = new ArrayList<ActorObject>();
        ignored.add(this);
        target.add(Player.class);

        final Player other = (Player) w.getClosestActor(this.getPosition(), 2000f, target, ignored);
        final WeightSteeringStrategy stg = new WeightSteeringStrategy();

        if (other != null) {
            stg.add(new Pursuit(other), 100);

            stg.add(new ProjectileSteering(), 100);

            return new PursuitShot(stg, this, other, 100);
        }

        return null;
    }
    
    public Projectile action3(World w) {
            return new MineShot(null, this, null, 100);
    }

    public Class<? extends Projectile> getAction1Type() {
        return SimpleShot.class;
    }

    public Class<? extends Projectile> getAction2Type() {
        return Shot.class;
    }

    public Class<? extends Projectile> getAction3Type() {
        return MineShot.class;
    }

    public ParticleEmitter getRocketEmitter() {
        return rocketEmitter;
    }

    public Projectile Action3(World w) {

        return null;
    }

}
