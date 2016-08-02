/**
 * GameScreenState.class
 */

package com.pucpr.game.states;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author Luis Boch 
 * @email luis.c.boch@gmail.com
 * @since May 15, 2016
 */
public interface GameScreenState extends AppState{
    void setStage(Stage stage);
    void close();
}
