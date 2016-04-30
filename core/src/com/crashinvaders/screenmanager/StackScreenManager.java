package com.crashinvaders.screenmanager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class StackScreenManager implements ApplicationListener, ScreenManager {
    private final Array<Screen> screenStack;
    private final Bundle bundle;
    private final Bundle dummyBundle;
    private final boolean descOnlyBundle;

    private Screen currentScreen;

    public StackScreenManager() {
        this(false);
    }

    /**
     * @param descOnlyBundle Prevents bundle from filling by screen that just hiding to show new screen (e.g. on ScreenManager#showScreen(Screen, boolean) call).
     *                     In this mode, all bundle changes will be ignored for ascending screen workflow (both for hiding old one and new showing screen).
     */
    public StackScreenManager(boolean descOnlyBundle) {
        this.descOnlyBundle = descOnlyBundle;
        screenStack = new Array<>();
        bundle = new MapBundle();
        dummyBundle = new DummyBundle();
    }

    @Override
    public void create() {

    }

    @Override
    public void resume() {
        if (currentScreen != null) currentScreen.resume();
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) currentScreen.resize(width, height);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (currentScreen != null) currentScreen.render(delta);
    }

    @Override
    public void pause() {
        if (currentScreen != null) currentScreen.pause();
    }

    @Override
    public void dispose() {
        if (currentScreen != null) {
            killScreen(currentScreen);
            currentScreen = null;
        }

        while (screenStack.size > 0) {
            Screen screen = screenStack.pop();
            killScreen(screen);
        }
    }

    @Override
    public void showScreen(Screen screen, boolean preserveCurrent) {
        if (currentScreen != null) {
            currentScreen.pause();
            currentScreen.hide(descOnlyBundle ? dummyBundle : bundle); // Prevent filling bundle by hiding screens before showing new one.
            if (preserveCurrent) {
                screenStack.add(currentScreen);
            } else {
                currentScreen.dispose();
            }
            currentScreen = null;
        }

        screen.create(this);
        screen.show(descOnlyBundle ? dummyBundle : bundle);   // Prevent passing bundle to new screens.
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screen.resume();
        currentScreen = screen;
    }

    @Override
    public void closeCurrentScreen() {
        if (currentScreen == null) throw new IllegalStateException("There is no current screen.");

        Gdx.app.log("ScreenManager", "screenStack.size = " + screenStack.size);
        Gdx.app.log("ScreenManager", "Gdx.app.getType() = " + Gdx.app.getType());
        // Do not terminate WebGL application if this is the last screen
        if (screenStack.size == 0 && Gdx.app.getType() == Application.ApplicationType.WebGL) return;

        killScreen(currentScreen);
        currentScreen = null;

        if (screenStack.size > 0) {
            Screen prevScreen = screenStack.pop();
            prevScreen.show(bundle);
            prevScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            prevScreen.resume();
            currentScreen = prevScreen;
        } else {
            Gdx.app.exit();
        }

    }

    public Bundle getBundle() {
        return bundle;
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    private void killScreen(Screen screen) {
        if (screen == currentScreen) {
            screen.pause();
            screen.hide(descOnlyBundle ? dummyBundle : bundle);
        }
        screen.dispose();
    }
}
