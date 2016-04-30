package com.crashinvaders.screenmanager;

public interface Screen {

    /** Called when screen showing for first time */
    void create(ScreenManager screenManager);

    /**
     * Called when this screen becomes the current screen.
     * @param bundle You may extract args that came from previously closed screens.
     *               Don't forget to remove them from bundle if you handled them.
     */
    void show(Bundle bundle);

    /** @see com.badlogic.gdx.ApplicationListener#resize(int, int) */
    void resize(int width, int height);

    /** @see com.badlogic.gdx.ApplicationListener#resume() */
    void resume();

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    void render(float delta);

    /** @see com.badlogic.gdx.ApplicationListener#pause() */
    void pause();

    /**
     * Called when this screen is no longer the current screen for a {@link com.badlogic.gdx.Game}.
     * @param bundle Put some args here to pass them to some screen downward by stack.
     */
    void hide(Bundle bundle);

    /** Called when this screen should release all resources and will be destroyed. */
    void dispose();

}
