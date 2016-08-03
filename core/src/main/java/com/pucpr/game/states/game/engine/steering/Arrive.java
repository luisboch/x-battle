/**
 * Flee.class
 */
package com.pucpr.game.states.game.engine.steering;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Arrive extends Steering<Arrive> {

    private Vector2 target;
    private Float deceleration = 1f;

    public Arrive() {
    }

    public Arrive(Vector2 target) {
        target(target);
    }

    public Arrive(ActorObject target) {
        target(target);
    }

    public Arrive target(Vector2 target) {
        this.target = target;
        return this;
    }

    public Arrive target(ActorObject target) {
        this.target = target.getPosition();
        return this;
    }

    public Arrive deceleration(float d) {
        this.deceleration = d;
        return this;
    }

    @Override
    public Vector2 _calculate() {
        if (target == null) {
            throw new IllegalArgumentException("Target can't be null");
        }
        
        Vector2 toTarget = target.cpy().sub(from.getPosition());
        float dist = toTarget.len();

        float speed = dist / deceleration;
        speed = Math.min(from.getMaxVel(), speed);
        
        Vector2 desired = toTarget.scl(speed).scl(1.0f / dist);
        desired.sub(from.getVelocity());
        
        return desired;
    }
}
