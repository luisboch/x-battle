/**
 * TrackMissile.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.steering.Steering;

public class SimpleShot extends Projectile {

    protected ActorObject target;

    public SimpleShot(Steering steering, ActorObject from) {
        super("disasteroids/fire.png", 5, 3f, 600, 400, new Vector2(30, 15), 500, 20f, 10000f, 15000);
        init(steering, from);
        
    }

    private void init(Steering steering, ActorObject from) {
        setSteering(steering);
        setFrom(from);
    }

}
