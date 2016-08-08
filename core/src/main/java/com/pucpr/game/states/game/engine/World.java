/**
 * World.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.server.ActorControl;
import com.pucpr.game.states.game.Planet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class World {

    private final List<ActorObject> actors = new ArrayList<ActorObject>();

    private final Map<ActorObject, ActorControl> controledRef
            = new HashMap<ActorObject, ActorControl>();

    private final List<ActorObject> planets = new ArrayList<ActorObject>();

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

    public World addPlanet(ActorObject planet) {
        planets.add(planet);
        actors.add(planet);
        return this;
    }

    public List<ActorObject> getActors() {
        return actors;
    }

    public List<ActorObject> getVisibleActors(Vector2 center, Float viewSize) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        viewSize = viewSize * 1.3f; //ads 30% to view

        final List<ActorObject> list = new ArrayList<ActorObject>();

        for (ActorObject obj : actors) {
            if (obj.getPosition().dst(center) < viewSize) {
                list.add(obj);
            }
        }

        return list;

    }

    public void calculate() {

        float secs = Gdx.app.getGraphics().getDeltaTime();
        for (ActorObject obj : actors) {

            /**
             * Primeiro calcula as forças.<br>
             * <ul>
             * <li>1 Sterring; </li>
             * <li>2 Gravidade; </li>
             * <li>3 Outras forças quaisquer (vendo, magnetismo, etc); </li>
             * </ul>
             * Depois multiplica a soma das forças pelo tempo gasto no loop
             * (secs) e limita pela força máxima do objeto (questionável). <br>
             * Aplica a variação da massa (força divida pela massa). <br>
             * Seta velocidade no objeto, considerando a soma da força.<br>
             * Move o objeto, de acordo com a velocidade atual (já com a força
             * aplicada) multiplicado pelo tempo gasto no loop (secs).
             */
            final Vector2 aux = calculateSteering(obj);

            final Vector2 control;
            final Vector2 forces;

            if (!obj.getClass().equals(Planet.class)) {
                control = calculateControl(obj);
                forces = calculateForceInfluence(obj);
            } else {
                control = new Vector2();
                forces = new Vector2();
            }

            aux.add(control).add(forces).scl(secs).limit(obj.getMaxForce());
            // Divide by mass
            aux.scl(1f / obj.getMass());
            obj.setVelocity(obj.getVelocity().add(aux).limit(obj.getMaxVel()));

            if (!obj.getVelocity().isZero()) {
                final Vector2 velSec = obj.getVelocity().cpy().scl(secs);
                obj.setPosition(obj.getPosition().add(velSec));
            }
        }

    }

    private Vector2 calculateSteering(ActorObject obj) {
        return obj.getSteering() != null ? obj.getSteering().calculate() : new Vector2();
    }

    private Vector2 calculateForceInfluence(ActorObject objA) {
        final Vector2 result = new Vector2();

        if (planets.contains(objA)) {
            return result;
        }

        for (ActorObject pln : planets) {

            final Vector2 forceField = objA.getPosition().cpy().sub(pln.getPosition());
            final float dist = forceField.len();
            
            if (dist > 300) {
                continue; // Ignore objects that is too far away.
            }
            
            final float intensity = objA.getMass() * pln.getMass();

            final float distSqr = dist * dist;

            forceField.nor();
            forceField.scl(intensity / distSqr);
            result.sub(forceField);
        }

        return result;

    }

    public ActorControl create(ActorObject actor) {
        ActorControl act = new ActorControl(actor);
        add(actor);
        actor.setVelocity(new Vector2(0.0001f, 0.0001f));
        actor.setDirection(actor.getVelocity().nor());
        bind(actor, act);
        return act;
    }

    private void bind(ActorObject actor, ActorControl act) {
        controledRef.put(actor, act);
    }

    private Vector2 calculateControl(ActorObject obj) {
        if (controledRef.containsKey(obj)) {
            Vector2 cal = new Vector2(0.01f, 0.01f);
            ActorControl act = controledRef.get(obj);

            if (!obj.getDirection().isZero() && obj.getVelocity().isZero()) {
                cal.set(obj.getDirection().isZero() ? obj.getVelocity().nor() : obj.getDirection().nor());
            }

            if (act.isLeft() && !act.isRight()) {
                obj.setDirection(obj.getDirection().rotate(2).nor());
            }

            if (!act.isLeft() && act.isRight()) {
                obj.setDirection(obj.getDirection().rotate(-2).nor());
            }

            if (act.isUp()) {
                Vector2 scl = cal.setLength(obj.getAccel());
                cal.add(scl);
                cal.scl(obj.getDirection()).limit(obj.getMaxVel());
            } else {
                cal = new Vector2(0, 0);
            }

            return cal;
        }

        return new Vector2();
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
