/**
 * TrackMissile.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.steering.Steering;

public class Shot extends SimpleShot {

    protected ActorObject target;
    protected int minProximity;

    public Shot() {
        super(null, null);
    }

    public Shot(String image, float radius, float mass, float maxVel, float maxForce, Vector2 size, float explosionRadius, float explosionForce, int lifeTime) {
        super(image, radius, mass, maxVel, maxForce, size, explosionRadius, explosionForce, lifeTime);
    }

    
    public Shot(Steering steering, ActorObject from, ActorObject target, int minProximity) {
        super(steering, from);
        init(target, minProximity);
    }

    
    protected void init(ActorObject target, int minProximity) {
        this.target = target;
        this.minProximity = minProximity;
    }
    
}
