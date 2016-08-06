/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game;

import com.pucpr.game.resources.SoundManager;

/**
 *
 * @author luis
 */
public class GameConfig {

    /**
     * 50/50 
     */
    public static final float PPM = 0.5f;
    public static final float GAME_SCALE = 1f;
    public static SoundManager SOUND_MANAGER;
    public static final boolean showDebug = false;
    public static final int skipFramesQty = 60;
    public static final boolean skipFrames = false;
    public static long waitScreenToChange = 1500;
    public static float defaultMusicVolume = 0.3f;

}
