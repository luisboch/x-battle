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
public class AboutState implements AppState, InputProcessor {

    private AppManager manager;

    private Stage stage;
    private Table container;

    public AboutState() {
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        stage = new Stage();

        Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        final Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, manager.getSkin());

        table.pad(10).defaults().expandX().space(4);
        table.row();
        Label label = new Label("Progrador", manager.getSkin());
        table.add(label).expandX().fillX();
        label = new Label("Luis Carlos Boch", manager.getSkin());
        table.add(label).expandX().fillX();
        table.row();
        label = new Label("Progrador", manager.getSkin());
        table.add(label).expandX().fillX();
        label = new Label("Felipe Alan Belini Rocha", manager.getSkin());
        table.add(label).expandX().fillX();

        container.add(scroll).expand().fill().colspan(2);
    }

    @Override
    public void render() {
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
        manager.setState(new MenuState());
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
}
