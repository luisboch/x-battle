/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.startup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pucpr.game.AppManager;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.states.AppState;
import com.pucpr.game.states.menu.MenuState;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class StartupState implements AppState, ResourceLoader.Handler {

    private static final Logger LOG = Logger.getLogger(StartupState.class.getName());

    private ResourceLoader loader;
    private String updateString;
    private int percent = 0;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    boolean changed = true;
    private AppManager manager;

    private List<Runnable> tasks = new ArrayList();
    private boolean loadFinished = false;

    @Override
    public void create() {
        loader = manager.getResourceLoader();
        loader.setHandler(this);
    }

    @Override
    public void render() {
        if (!loader.isLoading() && !loader.isLoaded()) {
            loader.load();
        }

        if (changed) {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (updateString != null) {
                batch.begin();
                font.draw(batch, percent + "%", 100, 150);
                font.draw(batch, updateString, 100, 100);
                batch.end();
            }

            changed = false;
        }

        if (loadFinished) {
            manager.setState(new MenuState());
        }
    }

    @Override
    public void update(int percent, String group, int groupQty, int groupState) {
        final String str = "Loading: " + group + " (" + groupState + "/" + groupQty + ")";

        if (!str.equals(updateString)) {
            updateString = str;
            changed = true;
        }

        this.percent = percent;

        if (percent == 100) {
            LOG.fine("Running startup tasks...");

            for (Runnable r : tasks) {
                try {
                    r.run();
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error while executing startup task", e);
                    throw new IllegalStateException(e);
                }
            }

            loadFinished = true;
        }
    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    public void addTask(Runnable r) {
        if (r != null) {
            tasks.add(r);
        }
    }
}
