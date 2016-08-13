/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public class ResourceLoader {

    private static ResourceLoader _instance;

    private final Map<String, List<String>> resources = new HashMap();
    private Handler handler;
    private final State totalState = new State();
    private final State groupState = new State();
    private final State total = new State();
    private boolean loading = false;
    private boolean loaded = false;

    final Map<String, FileHandle> loadedResources = new HashMap();
    final Map<String, Sound> loadedAudios = new HashMap();
    final Map<String, Texture> loadedTextures = new HashMap();

    private ResourceLoader() {
        resources.put("skin", new ArrayList());
        resources.get("skin").add("data/uiskin.json");

        resources.put("sprites", new ArrayList<String>());

        // Player sprites
        resources.get("sprites").add("data/image/ships/F-22B.png");
        resources.get("sprites").add("data/image/ships/SR-91A.png");
        resources.get("sprites").add("data/image/ships/Su-51K in.png");
        resources.get("sprites").add("data/image/disasteroids/fire.png");
        resources.get("sprites").add("data/image/explosions/explosion1.png");
    }

    public static synchronized ResourceLoader getInstance() {
        if (_instance == null) {
            _instance = new ResourceLoader();
        }

        return _instance;
    }

    public void load() {
        Thread t = new Thread() {
            @Override
            public void run() {

                loading = true;
                total.quantity = 0;
                totalState.quantity = 0;

                // Just count load itens
                for (String k : resources.keySet()) {
                    final List<String> l = resources.get(k);
                    for (final String s : l) {
                        total.quantity++;
                    }
                }

                for (String k : resources.keySet()) {
                    groupState.quantity = 0;
                    final List<String> l = resources.get(k);

                    for (final String s : l) {
                        final FileHandle file = Gdx.files.internal(s);
                        loadedResources.put(s, file);

                        if (k.equals("audio")) {

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    loadedAudios.put(s, Gdx.audio.newSound(file));
                                }
                            });
                        } else if (k.equals("sprites")) {

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    loadedTextures.put(s, new Texture(file));
                                }
                            });
                        }

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                        }

                        totalState.quantity++;
                        groupState.quantity++;
                        int lPercent = ((Float) ((totalState.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();
                        if (lPercent >= 100) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                            }
                        }
                        update(lPercent, k, l.size(), groupState.quantity);
                    }

                }

            }

        };

        int lPercent = total.quantity == 0 ? 0 : ((Float) ((totalState.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();
        update(lPercent, "Starting", 0, 0);

        loading = false;
        loaded = true;

        t.start();
    }

    private void update(final int percent, final String k, final int groupQty, final int groupState) {
        if (handler != null) {
            handler.update(percent, k, groupQty, groupState);
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public static interface Handler {

        void update(int percent, String group, int groupQty, int groupState);
    }

    private static class State {

        Integer quantity = 0;
    }

    public Map<String, FileHandle> getLoadedResources() {
        return loadedResources;
    }

    public Sound getSound(String key) {
        return loadedAudios.get(key);
    }

    /**
     * Return new audio based on resource key.
     *
     * @param key
     * @return
     */
    public Sound getMusic(String key) {
        return Gdx.audio.newSound(loadedResources.get(key));
    }

    public Object getResource(String key) {
        return getLoadedResources().get(key);
    }

    public Texture getTexture(String key) {

        if (!loadedTextures.containsKey(key)) {
            loadedTextures.put(key, new Texture(Gdx.files.internal(key)));
            System.out.println("X-BATTLE WARN: Loading not loaded texture... "
                    + "please add this texture to loader, to improve game performance!");
        }

        return loadedTextures.get(key);
    }
}
