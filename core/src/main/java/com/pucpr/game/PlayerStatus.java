/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class PlayerStatus {

    private static final PlayerStatus _instance = new PlayerStatus();

    private final Map<Keys, Object> playersKeys = new EnumMap<Keys, Object>(Keys.class);

    private byte health = 100;
    private PlayerLevel level = PlayerLevel.getLevel((byte) 1);

    private PlayerStatus() {
    }

    public static PlayerStatus getInstance() {
        return _instance;
    }

    /**
     * If the player has this key, return true, false otherwise.
     *
     * @Note when you need to check key value, plase, use
     * {@link #booleanKey(com.pucpr.game.Keys)}
     * @param key
     * @return if the player has this key, true, false otherwise.
     */
    public boolean is(Keys key) {
        return playersKeys.containsKey(key);
    }

    /**
     * If the player has this key, return true, false otherwise.
     *
     * @Note when you need to check key value, plase, use
     * {@link #booleanKey(com.pucpr.game.Keys)}
     * @param key
     * @return if the player has this key, true, false otherwise.
     */
    public static boolean isKey(Keys key) {
        return getInstance().is(key);
    }

    /**
     * If the player has this key, return the value, false otherwise.
     *
     * @Note when you need to check key value, plase, use
     *
     * @param key
     * @return if the player has this key, the value, false otherwise.
     */
    public boolean booleanKey(Keys key) {
        return playersKeys.containsKey(key) ? ((Boolean) playersKeys.get(key)) : false;
    }

    public short shortKey(Keys key) {
        return playersKeys.containsKey(key) ? ((Short) playersKeys.get(key)) : 0;
    }

    public String stringKey(Keys key) {
        return playersKeys.containsKey(key) ? ((String) playersKeys.get(key)) : "";
    }

    public void set(Keys k, Object obj) {
        playersKeys.put(k, obj);
    }

    /**
     *
     * @param level an value beween 1 and 100
     */
    public void setLevel(byte level) {

        if (level > 100) {
            throw new IllegalArgumentException("Invalid level, must be between 1 and 100.");
        }

        this.level = PlayerLevel.getLevel(level);
    }
}
