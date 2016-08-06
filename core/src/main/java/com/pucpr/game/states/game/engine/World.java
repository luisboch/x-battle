/**
 * World.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
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
    private List<ActorObject> planets = new ArrayList<ActorObject>();

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
       
//        
//        x = 50;
//        
//        Velocidade por segundo = 50;
//        Força ganha 10;
//        
//        Velocidade por segundo 60;
//        
//        1 quadro por segundo;
//        
//        
//        Velocidade por segundo = 50;
//        Força ganha 10;
//        10 quebra pelo loop  = 0.1;
//        
//        Ganho de velocidade neste loop = 0.1;
//        
//        Velocidade que eu já tenho + Ganho de velocidade neste loop 
//        
//        Velocidade por segundo = 50.1;
//        
//        Posição do objeto = 50 + 0.501;
//        
//        Posição do objeto = 5.501;
//        
//        
//        
//                
                
        float secs = Gdx.app.getGraphics().getDeltaTime();
        for (ActorObject obj : actors) {

            /**
             * Primeiro calcula as forças.
             *  1 Sterring;
             *  2 Gravidade;
             *  3 Outras forças quaisquer (vendo, magnetismo, etc);
             * 
             */
            
            final Vector2 aux = calculateSteering(obj);
            final Vector2 forces = calculateForceInfluence(obj);
            aux.add(forces).scl(secs).limit(obj.getMaxForce());
            // Divide by mass
            aux.scl(1f / obj.getMass());
            obj.setVelocity(obj.getVelocity().add(aux).limit(obj.getMaxVel()));

            if (!obj.getVelocity().isZero()) {
                final Vector2 velSec = obj.getVelocity().cpy().scl(secs);

                obj.setPosition(obj.getPosition().add(velSec));
                obj.setDirection(velSec.nor());
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

            final float intensity = objA.getMass() * pln.getMass();
            final Vector2 forceField = objA.getPosition().cpy().sub(pln.getPosition());
            final float dist = forceField.len();

            final float distSqr = dist * dist;

            forceField.nor();
            forceField.scl(intensity / distSqr);
            result.sub(forceField);
        }
        System.out.println("Gravity" + result);
        return result;

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
