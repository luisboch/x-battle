/**
 * VectorObject.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.UIDManager;
import com.pucpr.game.states.game.engine.steering.Steering;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public abstract class ActorObject {

    private long uID = UIDManager.next(this);
    
    private final float radius;
    private final float mass;
    private final float maxVel;
    private final float accel = 300;
    private final float maxForce;
    private final Vector2 position = new Vector2();
    private final Vector2 velocity = new Vector2();
    private Vector2 direction = new Vector2();
    private final Vector2 size; // Render used only

    private final String type;

    private Steering steering;

    public ActorObject(float radius, float mass, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        type = _getType();
        maxVel = 500f;
        maxForce = 350f;
        size = new Vector2(width, height);
    }

    public ActorObject(float radius, float mass, float maxVel, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        maxForce = 350f;
        this.size = new Vector2(width, height);
    }

    public ActorObject(float radius, float mass, float maxVel, float maxForce, float width, float height) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        this.maxForce = maxForce;
        this.size = new Vector2(width, height);
    }

    public ActorObject(float radius, float mass, float maxVel, float maxForce, Vector2 size) {
        this.radius = radius;
        this.mass = mass;
        this.maxVel = maxVel;
        this.maxForce = maxForce;
        this.size = size;
        this.type = _getType();
    }

    public long getuID() {
        return uID;
    }

    public void setuID(long uID) {
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
        steering.from(this);
    }

    public Steering getSteering() {
        return steering;
    }

    public abstract TextureRegion getTexture();

    public float getAngle() {
        return getDirection().angle();
    }

    public void contact(ActorObject e) {
    }
}
