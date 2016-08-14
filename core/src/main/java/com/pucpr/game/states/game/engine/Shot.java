/**
 * TrackMissile.class
 */
package com.pucpr.game.states.game.engine;

import com.pucpr.game.states.game.engine.steering.Steering;

public class Shot extends SimpleShot {

    protected ActorObject target;
    protected int minProximity;

    public Shot() {
        super(null, null);
    }

    
    public Shot(Steering steering, ActorObject from, ActorObject target, int minProximity) {
        super(steering, from);
        init(target, minProximity);
    }

    private void init(ActorObject target, int minProximity) {
        this.target = target;
        this.minProximity = minProximity;
        
        if (target == null) {
            throw new IllegalArgumentException("Target is required");
        }
    }
    
}
