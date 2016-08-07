/**
 * RemoteSevice.class
 */

package com.pucpr.game.server;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.server.local.UDPClientService;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Boch 
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class RemoteSevice extends GameService{
    
    private String serverPath;
    private UDPClientService service;
    public RemoteSevice(String serverPath) {
        this.serverPath = serverPath;
        
        this.service = new UDPClientService(this);
    }
    
    public ActorControl insert(ActorObject obj){
       return service.connect(obj, serverPath);
    }

    @Override
    public List<ActorObject> getVisibleActors(Vector2 center) {
        return this.service.getActors();
    }

    @Override
    public List<ActorObject> getActors() {
        return this.service.getActors();
    }
    

    @Override
    public void calculate() {
        // Nothign to do (server only)
    }

    @Override
    public void insertPlanet(ActorObject actor) {
        // Nothign to do (server only)
    }

    @Override
    public void disconnect() {
        service.stop();
    }
    
    
    
}
