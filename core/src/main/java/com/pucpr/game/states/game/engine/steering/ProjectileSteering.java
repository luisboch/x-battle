/**
 * ProjectileSteering.class
 */

package com.pucpr.game.states.game.engine.steering;

import com.badlogic.gdx.math.Vector2;


public class ProjectileSteering extends Steering<ProjectileSteering> {
    private float accel = 50f;
    
    @Override
    protected Vector2 _calculate() {
        return from.getVelocity().nor().scl(accel);
    }

    public ProjectileSteering accel(float accel){
        this.accel = accel;
        return this;
    }
}
