/**
 * World.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class World {

    private List<ActorObject> actors = new ArrayList<ActorObject>();

    private final float width;
    private final float height;

    private final Vector2 gravity;

    public World(float width, float height, Vector2 gravity) {
        this.width = width;
        this.height = height;
        this.gravity = gravity;
    }

    public World(float width, float height) {
        this.width = width;
        this.height = height;
        this.gravity = new Vector2(0, 0);
    }

    public World add(ActorObject actor) {
        actors.add(actor);
        return this;
    }

    public List<ActorObject> getActors() {
        return actors;
    }

    public List<ActorObject> getVisibleActors(Vector2 center, float viewSize) {
        if (center == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        viewSize = viewSize * 1.3f; //ads 30% to view

        final List<ActorObject> list = new ArrayList<ActorObject>();

        for (ActorObject obj : actors) {
            if (obj.getPosition().dst2(center) < viewSize) {
                list.add(obj);
            }
        }

        return list;

    }

    public void calculate() {
        for (ActorObject obj : actors) {
            obj.calculate();
        }

    }

    public static interface ContactListener {

        /**
         * Notify contact between two objects. Note, if return is null, then no
         * changes in physics we will do.
         *
         * @paramif obj1
         * @param obj2
         * @return
         */
        ContactResolver contact(ActorObject obj1, ActorObject obj2);
    }

    public static interface ContactResolver {

        /**
         * When one of two objects in contact will be destroyed, then, this
         * method must return that object.
         *
         * @return
         */
        ActorObject getDestroyedObject();
    }
}
