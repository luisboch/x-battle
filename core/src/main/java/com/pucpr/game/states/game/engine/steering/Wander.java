/**
 * Wander.class
 */
package com.pucpr.game.states.game.engine.steering;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 2, 2016
 */
public class Wander extends Steering<Wander> {

    private Float circleDistance = 50f;
    private Float circleRadius = 50f;
    private Float wanderAngle = 0f;
    private Float angleChange = 60f;

    public Wander circleDistance(float val) {
        circleDistance = val;
        return this;
    }

    public Wander circleRadius(float val) {
        circleRadius = val;
        return this;
    }

    public Wander wanderAngle(float val) {
        wanderAngle = val;
        return this;
    }

    public Wander angleChange(float val) {
        angleChange = val;
        return this;
    }

    @Override
    public Wander from(ActorObject from) {
        return super.from(from);
    }

    @Override
    public Vector2 _calculate() {

        final Vector2 cirCenter = from.getVelocity().cpy();
        cirCenter.nor();
        cirCenter.scl(circleDistance);

        final Vector2 dst = new Vector2(1, 1);
        dst.setLength(circleRadius);
        dst.setAngle((float) Math.toDegrees(wanderAngle));

        wanderAngle += (float) (Math.random() * angleChange - angleChange * 0.5);

        System.out.println("Angle: " + wanderAngle);

        final Vector2 wanderForce = cirCenter.add(dst);

        System.out.println("Force:  " + wanderForce);
//        System.out.println("Wander Angle: " + wanderAngle + ", From Angle: " + from.getVelocity().angle()+ ", WanderForce" + wanderForce);
        return wanderForce.sub(from.getVelocity());
//}
    }

    @Override
    public String toString() {
        return "Wander{" + "circleDistance=" + circleDistance + ", circleRadius=" + circleRadius + ", wanderAngle=" + wanderAngle + ", angleChange=" + angleChange + '}';
    }

}
