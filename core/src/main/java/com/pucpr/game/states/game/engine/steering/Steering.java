/**
 * Steering.class
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
public abstract class Steering<E extends Steering> {

    protected ActorObject from;

    public Steering() {
    }

    public E from(ActorObject from) {
        this.from = from;
        return (E) this;
    }

    public Vector2 calculate() {

        if (this.from == null) {
            throw new IllegalStateException("Object reference can't be null");
        }

        return _calculate();
    }

    protected abstract Vector2 _calculate();
}
