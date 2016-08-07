/**
 * LocalhostService.class
 */
package com.pucpr.game.server;

import com.badlogic.gdx.Gdx;
import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.World;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class LocalhostService extends GameService {

    private World world;
    private ActorObject localActor;

    @Override
    public void connect() {
        world = new World(10000, 10000);
    }

    public void setMainActor(ActorObject mainActor) {
        this.localActor = mainActor;
    }
    
    @Override
    public List<ActorObject> getVisibleActors() {
        return world.getVisibleActors(localActor.getPosition(), Gdx.graphics.getWidth() / 2);
    }

    @Override
    public List<ActorObject> getActors() {
        return world.getActors();
    }

    
    @Override
    public void insert(ActorObject actor) {
        world.add(actor);
    }

    @Override
    public void insertPlanet(ActorObject actor) {
        world.addPlanet(actor);
    }

    @Override
    public void remove(ActorObject actor) {
        world.getActors().remove(actor);
    }

    @Override
    public void calculate() {
        world.calculate();
    }

    
}
