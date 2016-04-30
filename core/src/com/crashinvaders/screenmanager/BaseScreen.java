package com.crashinvaders.screenmanager;

public class BaseScreen implements Screen {
    protected ScreenManager screenManager;

    @Override
    public void create(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show(Bundle bundle) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void hide(Bundle bundle) {

    }

    @Override
    public void dispose() {

    }
}
