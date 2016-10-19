/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game;

import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.Projectile;
import com.pucpr.game.states.game.engine.PursuitShot;
import com.pucpr.game.states.game.engine.World;
import com.pucpr.game.states.game.engine.steering.ProjectileSteering;
import com.pucpr.game.states.game.engine.steering.Pursuit;
import com.pucpr.game.states.game.engine.steering.WeightSteeringStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felipe
 */
public class Nave3 extends Player {

    public Nave3() {
        super("data/image/ships/F-45A-c.PNG");
    }

    @Override
    public Projectile action2(World w) {

        final List<Class<? extends ActorObject>> target = new ArrayList<Class<? extends ActorObject>>();

        final List<ActorObject> ignored = new ArrayList<ActorObject>();
        ignored.add(this);
        target.add(Player.class);

        final Player other = (Player) w.getClosestActor(this.getPosition(), 2000f, target, ignored);
        final WeightSteeringStrategy stg = new WeightSteeringStrategy();

        if (other != null) {
            stg.add(new Pursuit(other), 100);

            stg.add(new ProjectileSteering(), 100);

            return new PursuitShot(stg, this, other, 100);
        }

        return null;
    }

}
