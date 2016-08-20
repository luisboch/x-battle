/**
 * LocalhostService.class
 */
package com.pucpr.game.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.server.local.UDPServerService;
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
    private UDPServerService udpService;
    private static final int mapWidth = 100*32;
    public LocalhostService(){
        world = new World(mapWidth, mapWidth);
        udpService = new UDPServerService(world, this);
    }
    
    @Override
    public List<ActorObject> getVisibleActors(Vector2 center) {
        return world.getVisibleActors(center, (float) Gdx.graphics.getWidth() / 2);
    }

    @Override
    public List<ActorObject> getActors() {
        return world.getActors();
    }

    
    @Override
    public ActorControl insert(ActorObject actor) {
        return udpService.connect(actor);
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

    @Override
    public void disconnect() {
        udpService.stop();
    }
    
}
