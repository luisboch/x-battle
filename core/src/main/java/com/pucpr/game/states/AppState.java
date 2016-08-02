/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states;

import com.pucpr.game.AppManager;

/**
 *
 * @author luis
 */
public interface AppState {

    void render();

    void setManager(AppManager s);

    void create();
}
