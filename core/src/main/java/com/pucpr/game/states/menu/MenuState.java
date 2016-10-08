/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pucpr.game.AppManager;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.server.local.ProtocolTester;
import com.pucpr.game.states.game.GameState;
import java.util.LinkedList;
import java.util.List;
import com.pucpr.game.states.AboutState;
import com.pucpr.game.states.AircraftSelectionState;
import com.pucpr.game.states.AppState;

public class MenuState implements AppState {

    final List<MenuAction> actions = new LinkedList<MenuAction>();

    private AppManager manager;

    private Stage stage;
    private Table container;

    public void create() {

        // Validate network protocol
        ProtocolTester.test();

        final MenuAction start = new MenuAction("Start", 0);
        start.setAction(new Runnable() {
            @Override
            public void run() {
                manager.setState(new AircraftSelectionState());
            }
        });
        final MenuAction about = new MenuAction("About", 1);
        about.setAction(new Runnable() {
            @Override
            public void run() {
                manager.setState(new AboutState());
            }
        });

        final MenuAction quit = new MenuAction("Quit", 2);
        quit.setAction(new Runnable() {
            @Override
            public void run() {
                manager.quit();
            }
        });

        actions.add(start);
        actions.add(about);
        actions.add(quit);

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        final Table table = new Table();

        final Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        final ScrollPane scroll = new ScrollPane(table, skin);

        table.pad(10).defaults().expandX().space(4);

        for (final MenuAction ac : actions) {
            table.row();

            final TextButton button = new TextButton(ac.getLabel(), skin);
            table.add(button);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (ac.getAction() != null) {
                        ac.getAction().run();
                    }
                }

            });
        }

        container.add(scroll).expand().fill().colspan(2);
//        GameConfig.SOUND_MANAGER.setMusicName("menu");
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
//        Table.drawDebug(stage);

    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public void close() {
        Gdx.input.setInputProcessor(null);
    }

}
