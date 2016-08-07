/**
 * GameServer.class
 */
package com.pucpr.game.server;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public abstract class GameService {
    
    public abstract void disconnect();
    
    public abstract ActorControl insert(ActorObject actor);
            
    public abstract void insertPlanet(ActorObject actor);
    public void remove(ActorObject actor){
        getActors().remove(actor);
    }
    
    
    public abstract List<ActorObject> getVisibleActors(Vector2 center);
    
    public abstract List<ActorObject> getActors();
    
    public abstract void calculate();
}
