package com.pucpr.game.states.menu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
class Action {

    private final List<Action> subMenu = new ArrayList<Action>();
    private String label;
    private Runnable action;
    private final int index;

    public Action() {
        index = 0;
    }

    public Action(String label, int index) {
        this.label = label;
        this.index = index;
    }
    

    public Action(String label, Runnable action) {
        this.label = label;
        this.action = action;
        this.index = 0;
    }

    public Action(String label, Runnable action, int index) {
        this.label = label;
        this.action = action;
        this.index = index;
    }

    public Action(String label) {
        this.label = label;
        this.index = 0;
    }

    public List<Action> getSubMenu() {
        return subMenu;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public int getIndex() {
        return index;
    }

}
