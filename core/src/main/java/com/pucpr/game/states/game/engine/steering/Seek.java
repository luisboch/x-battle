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
public class Seek extends Steering<Seek> {

    private Vector2 target;

    public Seek(Vector2 target) {
        this.target = target;
    }

    public Seek(ActorObject target) {
        this.target = target.getPosition();
    }

    public Seek target(ActorObject target) {
        this.target = target.getPosition();
        return this;
    }

    public Seek target(Vector2 target) {
        this.target = target;
        return this;
    }

    @Override
    public Vector2 _calculate() {
        
        if (target == null) {
            throw new IllegalStateException("Target can't be null");
        }

        final Vector2 desired = target.cpy().sub(from.getPosition());

        desired.nor();
        desired.scl(from.getMaxVel());

        Vector2 seek = desired.sub(from.getVelocity());

        return seek;
    }

    @Override
    public String toString() {
        return "Seek{from" + (from == null ? "null" : from.getPosition()) + "target=" + target + '}';
    }

}
