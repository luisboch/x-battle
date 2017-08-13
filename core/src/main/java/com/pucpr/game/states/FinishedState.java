/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pucpr.game.AppManager;
import com.pucpr.game.states.menu.MenuState;

/**
 *
 * @author luis
 */
public class FinishedState implements AppState, InputProcessor {

    private AppManager manager;

    private Stage stage;
    private Table container;
    private final String player;
    private final Table table = new Table();
    private float loadedTime = 0f;
    private float timeToExit = 5f;
    private boolean canExit = false;

    public FinishedState(String player) {
        this.player = player;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        stage = new Stage();

        Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        final ScrollPane scroll = new ScrollPane(table, manager.getSkin());

        table.pad(10).defaults().expandX().space(4);
        table.row();
        Label label = new Label("Player " + player + " venceu a batalha!", manager.getSkin());
        table.add(label).expandX().fillX();

        container.add(scroll).expand().fill().colspan(2);
    }

    @Override
    public void render() {
        float secs = Gdx.app.getGraphics().getDeltaTime();
        loadedTime += secs;
        if (loadedTime >= timeToExit) {
            Label label = new Label("Pressione qualquer botao para continuar", manager.getSkin());
            table.add(label).expandX().fillX();
            canExit = true;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (canExit) {
            manager.setState(new MenuState());
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        manager.setState(new MenuState());
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        manager.setState(new MenuState());
        Gdx.input.setInputProcessor(this);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void close() {
    }

}
