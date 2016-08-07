/**
 * ActorControl.class
 */
package com.pucpr.game.server;

import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 7, 2016
 */
public class ActorControl {

    private ActorObject actor;

    /**
     * UP
     */
    private boolean up = false;

    /**
     * RIGHT
     */
    private boolean right = false;

    /**
     * DOWN
     */
    private boolean down = false;

    /**
     * LEFT
     */
    private boolean left = false;

    /**
     * FORCE
     */
    private boolean force = false;

    /**
     * ACTION 1
     */
    private boolean action1 = false;

    /**
     * ACTION 2
     */
    private boolean action2 = false;

    /**
     * ACTION 3
     */
    private boolean action3 = false;

    /**
     * MOUSE POINT X
     */
    private short mouseX = 0;

    /**
     * MOUSE POINT Y
     */
    private short mouseY = 0;

    public ActorControl() {
    }

    public ActorControl(ActorObject actor) {
        this.actor = actor;
    }

    public ActorObject getActor() {
        return actor;
    }

    public void setActor(ActorObject actor) {
        this.actor = actor;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isAction1() {
        return action1;
    }

    public void setAction1(boolean action1) {
        this.action1 = action1;
    }

    public boolean isAction2() {
        return action2;
    }

    public void setAction2(boolean action2) {
        this.action2 = action2;
    }

    public boolean isAction3() {
        return action3;
    }

    public void setAction3(boolean action3) {
        this.action3 = action3;
    }

    public short getMouseX() {
        return mouseX;
    }

    public void setMouseX(short mouseX) {
        this.mouseX = mouseX;
    }

    public short getMouseY() {
        return mouseY;
    }

    public void setMouseY(short mouseY) {
        this.mouseY = mouseY;
    }

    @Override
    public String toString() {
        return "ActorControl{" + "actor=" + actor + ", up=" + up + ", right=" + right + ", down=" + down + ", left=" + left + ", force=" + force + ", action1=" + action1 + ", action2=" + action2 + ", action3=" + action3 + ", mouseX=" + mouseX + ", mouseY=" + mouseY + '}';
    }
}
