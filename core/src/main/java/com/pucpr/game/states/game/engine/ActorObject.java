/**
 * VectorObject.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.UIDManager;
import com.pucpr.game.states.game.engine.steering.Steering;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public abstract class ActorObject {

    private short uID = UIDManager.next(this);

    private final float radius;
    private final float mass;
    private final float maxVel;
    private final float accel = 300;
    private final float maxForce;
    private final Vector2 position = new Vector2();
    private final Vector2 velocity = new Vector2();
    private Vector2 direction = new Vector2();
    private Vector2 lastWorldPos = new Vector2();
    private Vector2 pivot = new Vector2(0f, 0f);
    private final Vector2 size; // Render used only

    private final String type;

    private Steering steering;

    private final List<ActorObject> listActorObject;
    private ActorObject parent;

    public ActorObject(float radius, float mass, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        type = _getType();
        maxVel = 500f;
        maxForce = 350f;
        size = new Vector2(width, height);
        listActorObject = new ControlList(this);
    }

    public ActorObject(float radius, float mass, float maxVel, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        maxForce = 350f;
        this.size = new Vector2(width, height);
        listActorObject = new ControlList(this);
    }

    public ActorObject(float radius, float mass, float maxVel, float maxForce, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        this.maxForce = maxForce;
        this.size = new Vector2(width, height);
        listActorObject = new ControlList(this);
    }

    public ActorObject(float radius, float mass, float maxVel, float maxForce, Vector2 size) {
        this.radius = radius;
        this.mass = mass;
        this.maxVel = maxVel;
        this.maxForce = maxForce;
        this.size = size;
        this.type = _getType();
        listActorObject = new ControlList(this);
    }

    protected abstract TextureRegion getTexture();

    protected void tick() {
    }

    ;

    public Vector2 getLastWorldPos() {
        return lastWorldPos;
    }

    public void draw(SpriteBatch render, Matrix3 world) {

        tick();

        final float angle = this.getAngle();

        final TextureRegion texture = this.getTexture();

        world = new Matrix3(world).mul(new Matrix3().setToTranslation(this.getPosition()));
        world.mul(new Matrix3().rotate(angle));
        world.mul(new Matrix3().setToTranslation(getPivot().x, getPivot().y));

        if (texture != null) {
            lastWorldPos = new Vector2();

            world.getTranslation(lastWorldPos);
            final Sprite sprite = new Sprite(texture);

            sprite.setPosition(lastWorldPos.x - (sprite.getWidth() / 2), lastWorldPos.y - (sprite.getHeight() / 2));
            sprite.setRotation(angle);
            sprite.setScale(this.getSize().x / sprite.getWidth(), this.getSize().y / sprite.getHeight());
            sprite.draw(render);

            for (ActorObject c : getListActorObject()) {
                c.draw(render, world);
            }
        }
    }

    public Vector2 getPivot() {
        return pivot;
    }

    public void setPivot(Vector2 pivot) {
        this.pivot = pivot;
    }

    public short getuID() {
        return uID;
    }

    public void setuID(short uID) {
        this.uID = uID;
    }

    private String _getType() {
        return getClass().getSimpleName().toUpperCase();
    }

    public String getType() {
        return type;
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public ActorObject setPosition(Vector2 pos) {
        position.set(pos);
        return this;
    }

    public ActorObject setDirection(Vector2 pos) {
        direction.set(pos);
        return this;
    }

    public ActorObject setVelocity(Vector2 pos) {
        velocity.set(pos);
        return this;
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public float getMaxVel() {
        return maxVel;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public Vector2 getDirection() {
        return direction.cpy();
    }

    public Vector2 getSize() {
        return size;
    }

    public float getAccel() {
        return accel;
    }

    public void setSteering(Steering steering) {
        this.steering = steering;
        if (steering != null) {
            steering.from(this);
        }
    }

    public Steering getSteering() {
        return steering;
    }

    public float getAngle() {
        return getDirection().angle();
    }

    public void contact(ActorObject e) {
    }

    public byte getAnnimationState() {
        return 0;
    }

    public void setAnnimationState(byte val) {
        // Ignored...
    }

    public List<ActorObject> getListActorObject() {
        return listActorObject;
    }

    public void setListActorObject(List<ActorObject> listActorObject) {
        this.listActorObject.clear();
        this.listActorObject.addAll(listActorObject);
    }

    public void setParent(ActorObject parent) {
        this.parent = parent;
    }

    public ActorObject getParent() {
        return parent;
    }

    
    private static class ControlList extends ArrayList<ActorObject> {

        private final ActorObject _instance;

        private ControlList(ActorObject ref) {
            _instance = ref;
        }

        @Override
        public boolean add(ActorObject e) {
            if (e == null) {
                return false;
            }

            e.setParent(_instance);
            return super.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends ActorObject> c) {

            if (c == null) {
                return false;
            }

            for (ActorObject a : c) {
                if (a != null) {
                    a.setParent(_instance);
                }
            }
            return super.addAll(c);
        }

        @Override
        public void clear() {

            for (ActorObject a : this) {
                a.setParent(null);
            }

            super.clear();
        }

        @Override
        public boolean remove(Object o) {

            if (o == null) {
                return false;
            }

            if (o instanceof ActorObject) {
                ((ActorObject) o).setParent(null);
            }

            return super.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {

            if (c == null) {
                return false;
            }

            for (Object o : c) {
                if (o instanceof ActorObject) {
                    ((ActorObject) o).setParent(null);
                }
            }

            return super.removeAll(c);
        }

    }

}
