/**
 * SteeringStrategy.class
 */
package com.pucpr.game.states.game.engine.steering;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage Multiple Steerings. Example: <br>
 * BH 1 is [impact = 7] and it will result a value of 10;<br>
 * BH 2 is [impact = 10] and it will result a value of 20; <br>
 * This case will result ? (10*7 + * 20*10) / 17 = 81.7 <br>
 * if we add a new BH with impact = 30 and this results a value of 1 then:<br>
 * (10*7 + 20*10 + 1*30) / 47 = 6;<br>
 * <hr>
 * <b>Warning</b>: when result or impact is zero, then Steering and respective
 * impact will be ignored.<br>
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class WeightSteeringStrategy extends Steering<WeightSteeringStrategy> {

    final List<SteeringValue> behaviors = new ArrayList<SteeringValue>();

    /**
     *
     * @param from
     */
    public WeightSteeringStrategy() {
        super();
    }

    /**
     * Add an behavior to this Strategy. Remember, impact will affect all other
     * behaviors.
     *
     *
     * @param steering
     * @param impact
     * @return
     */
    public WeightSteeringStrategy add(Steering steering, Integer impact) {

        final SteeringValue val = new SteeringValue(steering, impact);

        val.strategy = this;
        val.behavior.from(from);
        behaviors.add(val);

        return this;
    }

    @Override
    public Vector2 _calculate() {
        int totalImpact = 0;
        final Vector2 result = new Vector2(0, 0);
        final List<SteeringValue> calculated = new ArrayList<SteeringValue>();

        for (SteeringValue val : behaviors) {
            if (val.impact > 0) {
                Vector2 rs = val.behavior.calculate();
                if (!rs.isZero()) {
                    totalImpact += val.impact;
                    calculated.add(val);
                    val.calculatedVal = rs;
                }
            }
        }

        for (SteeringValue val : calculated) {
            result.add(val.calculatedVal.scl(val.impact).scl(1f / totalImpact));
        }

        return result;
    }

    @Override
    public WeightSteeringStrategy from(ActorObject from) {
        
        for (SteeringValue v : behaviors) {
            v.behavior.from(from);
        }
        
        return super.from(from);
    }

    public static class SteeringValue {

        private WeightSteeringStrategy strategy;
        private final Steering behavior;
        private Integer impact;
        private Vector2 calculatedVal;

        public SteeringValue(Steering behavior, Integer impact) {
            this.behavior = behavior;
            this.impact = impact;
        }

        public SteeringValue setImpact(Integer impact) {
            this.impact = impact;
            return this;
        }

        public WeightSteeringStrategy strategy() {
            return strategy;
        }

    }

}
