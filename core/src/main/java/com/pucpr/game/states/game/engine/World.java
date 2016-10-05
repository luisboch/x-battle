/**
 * World.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.server.ActorControl;
import com.pucpr.game.states.game.ForceField;
import com.pucpr.game.states.game.Planet;
import com.pucpr.game.states.game.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class World {

    private final Queue<ActorObject> actors = new ConcurrentLinkedQueue<ActorObject>();

    private final Map<ActorObject, ActorControl> controllRef
            = new HashMap<ActorObject, ActorControl>();

    private final Map<ActorObject, ProjectileInfo> projectileRef
            = new HashMap<ActorObject, ProjectileInfo>();

    private final Map<Projectile, Long> projectiles
            = new ConcurrentHashMap<Projectile, Long>();
    private final Queue<ActorObject> deadObjects = new ConcurrentLinkedQueue<ActorObject>();

    private final List<ActorObject> planets = new ArrayList<ActorObject>();
    private final List<Explosion> explosions = new ArrayList<Explosion>();

    private final float width;
    private final float height;

    private final float margin;

    public World(float width, float height, float margin) {
        this.width = width;
        this.height = height;
        this.margin = margin;
    }

    public World add(ActorObject actor) {
        Vector2 pos = actor.getPosition();
        if (pos.y < height - margin) {
            pos.y = height - margin;
        } else if (pos.y > height + margin) {
            pos.y = height + margin;
        }

        if (pos.x < width - margin) {
            pos.x = width - margin;
        } else if (pos.x > width + margin) {
            pos.y = width + margin;
        }

        actor.setPosition(pos);

        actors.add(actor);

        return this;
    }

    public World addPlanet(ActorObject planet) {
        planets.add(planet);
        actors.add(planet);
        return this;
    }

    public List<ActorObject> getActors() {
        return new ArrayList<ActorObject>(actors);
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

    public ActorObject getClosestActor(Vector2 center, Float viewSize) {

        final List<Class<? extends ActorObject>> allowedTypes = new ArrayList<Class<? extends ActorObject>>(2);
        allowedTypes.add(Player.class);

        return getClosestActor(center, viewSize, allowedTypes, new ArrayList<ActorObject>());
    }

    public ActorObject getClosestActor(Vector2 center, Float viewSize, Class<? extends ActorObject>... allowedTypes) {
        List<Class<? extends ActorObject>> list = Arrays.asList(allowedTypes);
        return getClosestActor(center, viewSize, list, new ArrayList<ActorObject>());
    }

    public ActorObject getClosestActor(Vector2 center, Float viewSize, List<Class<? extends ActorObject>> allowedTypes, List<ActorObject> ignored) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        if (allowedTypes == null) {
            allowedTypes = new ArrayList<Class<? extends ActorObject>>(2);
            allowedTypes.add(Player.class);
        }

        ActorObject closest = null;
        viewSize = viewSize * 1.3f; //ads 30% to view
        Float closestDis = null;

        for (ActorObject obj : actors) {
            if (!ignored.contains(obj)) {
                float dst = obj.getPosition().dst(center);
                if (allowedTypes.contains(obj.getClass()) && dst < viewSize) {
                    if (closest == null || dst < closestDis) {
                        closest = obj;
                        closestDis = dst;
                    }
                }
            }
        }
        return closest;
    }

    private void discoverActors(List<ActorObject> currentList, Collection<ActorObject> source) {

        for (ActorObject obj : source) {
            currentList.add(obj);
            discoverActors(currentList, obj.getListActorObject());
        }
    }

    public void calculate() {

        removeDeadObjects();

        float secs = Gdx.app.getGraphics().getDeltaTime();
        final List<ActorObject> fullList = new ArrayList<ActorObject>();

        discoverActors(fullList, actors);

        for (ActorObject obj : fullList) {

            resolveImpact(obj, fullList);

            /**
             * Primeiro calcula as forças.<br>
             * <ul>
             * <li>1 Sterring; </li>
             * <li>2 Gravidade; </li>
             * <li>3 Outras forças quaisquer (vendo, magnetismo, etc); </li>
             * </ul>
             * Depois multiplica a soma das forças pelo tempo gasto no loop
             * (secs) e limita pela força máxima do objeto (questionável). <br>
             * <br>
             * Aplica a variação da massa (força divida pela massa). <br>
             * <br>
             * Seta velocidade no objeto, considerando a soma da força.<br>
             * Move o objeto, de acordo com a velocidade atual (já com a força
             * aplicada) multiplicado pelo tempo gasto no loop (secs).
             */
            if (obj instanceof Projectile) {
                Projectile pro = (Projectile) obj;
                if (pro.canExplodeNow(false)) {
                    createExplosion(pro);
                } else if (System.currentTimeMillis() - projectiles.get(pro) > pro.getLifeTime()) {
                    if (pro.canExplodeNow(true)) {
                        createExplosion(pro);
                    } else {
                        deadObjects.add(obj);
                    }
                    continue;
                }
            } else if (obj instanceof Planet || obj instanceof Explosion) {
                continue;
            }

            final Vector2 aux = calculateSteering(obj);

            final Vector2 control;
            final Vector2 forces;

            if (!obj.getClass().equals(Planet.class)) {
                if (obj.getClass().equals(Player.class)) {
                    control = calculateControl((Player) obj);
                } else {
                    control = new Vector2();
                }
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

            if (obj instanceof Player) {
                if (obj.getPosition().x > (width + margin) && obj.getVelocity().x > 0) {
                    final Vector2 vel = obj.getVelocity();
                    vel.x = -vel.x;
                    obj.setVelocity(vel);
                }

                if (obj.getPosition().x < margin && obj.getVelocity().x < 0) {
                    final Vector2 vel = obj.getVelocity();
                    vel.x = -vel.x;
                    obj.setVelocity(vel);
                }

                if (obj.getPosition().y > (height + margin) && obj.getVelocity().y > 0) {
                    final Vector2 vel = obj.getVelocity();
                    vel.y = -vel.y;
                    obj.setVelocity(vel);
                }

                if (obj.getPosition().y < margin && obj.getVelocity().y < 0) {
                    final Vector2 vel = obj.getVelocity();
                    vel.y = -vel.y;
                    obj.setVelocity(vel);
                }
            }
        }

        for (Explosion ex : explosions) {
            ex.tick();
            if (!ex.isAlive()) {
                deadObjects.add(ex);
            }
        }

    }

    private Vector2 calculateSteering(ActorObject obj) {
        return obj.getSteering() != null ? obj.getSteering().calculate() : new Vector2();
    }

    private Vector2 calculateForceInfluence(ActorObject objA) {
        final Vector2 result = new Vector2();

        // Used to calculate how much we will hit obj.
        final Vector2 expForce = new Vector2();

        if (planets.contains(objA)) {
            return result;
        }
        if (!objA.getClass().equals(ForceField.class)) {
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
        }

        for (Explosion expl : explosions) {

            final Vector2 forceField = objA.getLastWorldPos().cpy().sub(expl.getPosition());
            final float dist = forceField.len();

            if (dist > expl.getRadius() + objA.getRadius()) {
                continue; // Ignore objects that is too far away.
            }

            final float intensity = (dist < expl.getRadius()) ? expl.getCurrentForce() : 1 - ((dist - expl.getRadius()) / expl.getRadius()) * expl.getCurrentForce();

            final float distSqr = dist * dist;

            forceField.nor();
            forceField.scl(intensity / distSqr);

            if (!objA.getClass().equals(ForceField.class)) {
                result.sub(forceField);
            }
            
            expForce.add(forceField);
        }

        objA.applyForce(expForce.len());

        return result;

    }

    public ActorControl create(ActorObject actor) {
        ActorControl act = new ActorControl(actor);
        actor.setPosition(new Vector2(500, 500));
        add(actor);
        actor.setVelocity(new Vector2(0.0001f, 0.0001f));
        actor.setDirection(actor.getVelocity().nor());
        bind(actor, act);
        return act;
    }

    private void bind(ActorObject actor, ActorControl act) {
        controllRef.put(actor, act);
    }

    private Vector2 calculateControl(Player obj) {
        if (controllRef.containsKey(obj)) {
            Vector2 cal = new Vector2(0.01f, 0.01f);
            ActorControl act = controllRef.get(obj);

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

            // Bombs?
            if (act.isAction1()) {
                final Projectile pro = obj.action1(this);
                if (pro != null && canPlayerCreateProjectile(obj, obj.getAction1Type())) {
                    createProjectile(obj, pro);
                }
            }
            // Bombs?
            if (act.isAction2()) {
                final Projectile pro = obj.action2(this);
                if (pro != null && canPlayerCreateProjectile(obj, obj.getAction2Type())) {
                    createProjectile(obj, pro);
                }
            }

            return cal;
        }

        return new Vector2();
    }

    public <E extends Projectile> void createProjectile(ActorObject from, E projectile) {

        Vector2 pos = from.getPosition();
        pos.add(from.getDirection().nor().scl(from.getRadius() + projectile.getRadius() + 1));

        projectile.setPosition(pos);
        projectile.setDirection(from.getDirection());
        projectile.setVelocity(from.getDirection().nor().scl(projectile.getInitialVelocity()));

        projectiles.put(projectile, System.currentTimeMillis());
        actors.add(projectile);
    }

    private boolean canPlayerCreateProjectile(Player from, Class<? extends Projectile> type) {
        long now = System.currentTimeMillis();
        final ProjectileInfo nfo;

        if (!projectileRef.containsKey(from)) {
            nfo = new ProjectileInfo();
            projectileRef.put(from, nfo);
        } else {
            nfo = projectileRef.get(from);
        }

        if (nfo.usedTypes.containsKey(type)) {
            final Long lastShot = nfo.usedTypes.get(type);

            if (now - lastShot <= Projectile.getReloadTimeConfig().get(type)) {
                return false; // User must wait for reload time before add new Projectile...
            }
        }

        nfo.usedTypes.put(type, now);

        return true;
    }

    private void removeDeadObjects() {

        ActorObject obj = null;

        while ((obj = deadObjects.poll()) != null) {

            if (obj instanceof Projectile) {
                projectiles.remove(obj);
            } else if (obj instanceof Explosion) {
                explosions.remove(obj);
            }

            actors.remove(obj);
        }
    }

    private void createExplosion(Projectile p) {
        final Explosion exp = p.createExplosion();
        explosions.add(exp);
        actors.add(exp);
        deadObjects.add(p);
    }

    private void resolveImpact(ActorObject ob, List<ActorObject> actors) {

        if (!ob.isAlive()) {
            return;
        }

        for (ActorObject act : actors) {

            if (!ob.equals(act) && act.isAlive()
                    && ob.getLastWorldPos().dst(act.getLastWorldPos()) <= (ob.getRadius() + act.getRadius())) {
                ob.contact(act);
            }
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
