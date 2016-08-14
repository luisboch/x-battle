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
        System.out.println("Target: " + target.getuID());
        System.out.println("From: " + from.getuID());
        if (target == null) {
            throw new IllegalArgumentException("Target is required");
        }
    }
    
}
