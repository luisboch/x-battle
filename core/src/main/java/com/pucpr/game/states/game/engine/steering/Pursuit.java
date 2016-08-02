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
public class Pursuit extends Steering<Pursuit> {

    private ActorObject evader;

    public Pursuit() {
        this(null);
    }

    public Pursuit(ActorObject evader) {
        this.evader = evader;
    }

    public Pursuit evader(ActorObject evader) {
        this.evader = evader;
        return this;
    }

    @Override
    public Vector2 _calculate() {

        if (evader == null) {
            throw new IllegalStateException("Object evader can't be null");
        }

        Vector2 toEvader = evader.getPosition().cpy().sub(from.getPosition().cpy());

        float relativeDir = from.getDirection().cpy().dot(evader.getDirection().cpy());

        if (toEvader.dot(from.getDirection()) > 0
                && relativeDir < Math.toRadians(18)) {
            return new Seek(evader).from(from).calculate();
        }

        float lookAheadTime = toEvader.len() / (from.getMaxVel() + evader.getVelocity().len());
        return new Seek(evader.getPosition().add(evader.getVelocity().scl(lookAheadTime))).from(from).calculate();
    }
}
