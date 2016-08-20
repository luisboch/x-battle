/**
 * TrackMissile.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.steering.Steering;

public class SimpleShot extends Projectile {

    protected ActorObject target;

    // Created when receive update from network (ignore steering and from, it will be controled by server)
    public SimpleShot() {
        this(null, null);
    }

    public SimpleShot(Steering steering, ActorObject from) {
        super("disasteroids/fire.png", 5, 3f, 2000, 400, new Vector2(30, 15), 20f, 1000f, 15000);
        init(steering, from);

    }

    public SimpleShot(String image, float radius, float mass, float maxVel, float maxForce, Vector2 size, float explosionRadius, float explosionForce, int lifeTime) {
        super(image, radius, mass, maxVel, maxForce, size, explosionRadius, explosionForce, lifeTime);
    }
    
    

    private void init(Steering steering, ActorObject from) {
        setSteering(steering);
        setFrom(from);
    }

}
