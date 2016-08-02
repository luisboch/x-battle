package com.pucpr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.resources.SoundManager;
import com.pucpr.game.states.AppState;
import com.pucpr.game.states.startup.StartupState;

public class AppManager extends ApplicationAdapter {
//	SpriteBatch batch;
//	Texture img;

    private Skin skin;
    private ResourceLoader loader = ResourceLoader.getInstance();

    private AppState state;

    @Override
    public void create() {
        GameConfig.SOUND_MANAGER = new SoundManager(this);
        final StartupState startupState = new StartupState();

        startupState.addTask(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        skin = new Skin((FileHandle) loader.getResource("data/uiskin.json"));
                    }
                });
            }
        });

        setState(startupState);

    }

    @Override
    public void render() {

        if (state != null) {
            state.render();
        }
    }

    public final void setState(AppState state) {
        this.state = state;
        if (state != null) {
            state.setManager(this);
            state.create();
        }
    }

    public void quit() {
        Gdx.app.exit();
    }

    public ResourceLoader getResourceLoader() {
        return loader;
    }

    public Skin getSkin() {
        return skin;
    }
}
