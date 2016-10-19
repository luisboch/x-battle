/**
 * TrackMissile.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.steering.Steering;

public class MineShot extends Shot {

    protected ActorObject target;

    // Created when receive update from network (ignore steering and from, it will be controled by server)
    public MineShot() {
        this(null, null);
    }

    public MineShot(Steering steering, ActorObject from, ActorObject target, int minProximity) {
        this(steering, from);
        init(target, minProximity);
    }

    
    public MineShot(Steering steering, ActorObject from) {
        super("disasteroids/big_bomb.png", 5, 3f, 2000, 400, new Vector2(30, 30), 20f, 1000f, 15000);
        init(steering, from);

    }

    private void init(Steering steering, ActorObject from) {
        setSteering(steering);
        setFrom(from);
    }

    @Override
    public float getInitialVelocity() {
        return 0f; 
    }
    
    

}
