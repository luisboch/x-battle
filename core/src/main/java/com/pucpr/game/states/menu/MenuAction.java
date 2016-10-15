package com.pucpr.game.states.menu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class MenuAction {

    private final List<MenuAction> subMenu = new ArrayList<MenuAction>();
    private String label;
    private Runnable action;
    private final int index;

    public MenuAction() {
        index = 0;
    }

    public MenuAction(String label, int index) {
        this.label = label;
        this.index = index;
    }
    

    public MenuAction(String label, Runnable action) {
        this.label = label;
        this.action = action;
        this.index = 0;
    }

    public MenuAction(String label, Runnable action, int index) {
        this.label = label;
        this.action = action;
        this.index = index;
    }

    public MenuAction(String label) {
        this.label = label;
        this.index = 0;
    }

    public List<MenuAction> getSubMenu() {
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
