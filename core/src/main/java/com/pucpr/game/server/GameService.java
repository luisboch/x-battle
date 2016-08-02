/**
 * GameServer.class
 */
package com.pucpr.game.server;

import com.pucpr.game.states.game.engine.ActorObject;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public abstract class GameService {
    public abstract void connect();
    
    public void insert(ActorObject actor){
        getVisibleActors().add(actor);
    }
    
    public void remove(ActorObject actor){
        getVisibleActors().remove(actor);
    }
    
    public abstract void setMainActor(ActorObject mainActor);
    
    public abstract List<ActorObject> getVisibleActors();
    
    public abstract List<ActorObject> getActors();
    
    public abstract void calculate();
}
