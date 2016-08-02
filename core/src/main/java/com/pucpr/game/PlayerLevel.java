/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class PlayerLevel {

    private static final Map<Byte, PlayerLevel> availableLevels = new HashMap();

    static {
        for (byte i = 1; i < 100; i++) {
            availableLevels.put(i, new PlayerLevel(i, i));
        }
    }

    private short attack;
    private short defense;

    private PlayerLevel() {
    }

    private PlayerLevel(short attack, short defense) {
        this.attack = attack;
        this.defense = defense;
    }

    public static PlayerLevel getLevel(Byte key) {
        return key == null ? null : availableLevels.get(key);
    }

    public short getAttack() {
        return attack;
    }

    public void setAttack(short attack) {
        this.attack = attack;
    }

    public short getDefense() {
        return defense;
    }

    public void setDefense(short defense) {
        this.defense = defense;
    }

}
