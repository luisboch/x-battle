/**
 * Aircrafts.class
 */
package com.pucpr.game;

import com.pucpr.game.states.game.Nave1;
import com.pucpr.game.states.game.Nave2;
import com.pucpr.game.states.game.Nave3;
import com.pucpr.game.states.game.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Oct 7, 2016
 */
public class Aircrafts {

    private static final Map<Integer, Class<? extends Player>> AIRCRAFT_REF_TYPE
            = new HashMap<Integer, Class<? extends Player>>();

    static {
        AIRCRAFT_REF_TYPE.put(1, Nave1.class);
        AIRCRAFT_REF_TYPE.put(2, Nave2.class);
        AIRCRAFT_REF_TYPE.put(3, Nave3.class);
    }

    public static Player getNewPlayer(Integer type) {
        try {
            return AIRCRAFT_REF_TYPE.get(type).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(Aircrafts.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
