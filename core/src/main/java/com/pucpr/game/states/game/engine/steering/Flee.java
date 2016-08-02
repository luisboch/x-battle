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
public class Flee extends Steering<Flee> {

    private Vector2 target;
    private Float panicDistance = 300f;

    public Flee() {
    }

    public Flee(ActorObject target) {
        target(target);
    }

    public Flee(Vector2 target) {
        target(target);
    }

    public Flee target(ActorObject target) {
        this.target = target.getPosition();
        return this;
    }

    public Flee target(Vector2 target) {
        this.target = target;
        return this;
    }

    public Flee panicDist(float dist) {
        this.panicDistance = dist;
        return this;
    }

    @Override
    public Vector2 _calculate() {

        if (target == null) {
            throw new IllegalStateException("Target can't be null");
        }

        if (from.getPosition().dst(target) > panicDistance) {
            return new Vector2(0, 0);
        }

        final Vector2 desired = from.getPosition().cpy().sub(target);

        desired.nor();
        desired.scl(from.getMaxVel());

        return desired.sub(from.getVelocity());
    }
}
