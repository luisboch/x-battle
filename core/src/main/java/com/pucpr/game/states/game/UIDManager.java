/**
 * UIDManager.class
 */
package com.pucpr.game.states.game;

import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class UIDManager {

    private static short nextUID = 0;

    /**
     * Create a new valid id, set to given object and, finally, return the id
     *
     * @param object
     * @return
     */
    public static synchronized short next(ActorObject object) {

        if (object == null) {
            throw new IllegalStateException("Object can't be null");
        }

        nextUID++;
        
        object.setuID(nextUID);
        
        return nextUID;
    }
}
