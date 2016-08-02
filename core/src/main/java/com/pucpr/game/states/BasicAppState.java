/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states;

import com.pucpr.game.AppManager;


public abstract class BasicAppState implements AppState {
    
    protected AppManager manager;

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }
    
}
