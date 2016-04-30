package com.metaphore.bmfmetaedit;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.common.PauseManager;
import com.crashinvaders.common.PrioritizedInputMultiplexer;
import com.crashinvaders.common.eventmanager.EventManager;
import com.crashinvaders.screenmanager.*;
import com.crashinvaders.screenmanager.Screen;
import com.metaphore.bmfmetaedit.actionresolver.ActionResolver;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserListener;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserParams;
import com.metaphore.bmfmetaedit.loadscreen.LoadScreen;
import com.metaphore.bmfmetaedit.mainscreen.MainScreen;
import com.metaphore.bmfmetaedit.model.Model;
import com.metaphore.bmfmetaedit.overlayscene.OverlayScene;
import com.metaphore.bmfmetaedit.overlayscene.events.GlobalSuspendEvent;

public class App extends StackScreenManager implements LoadScreen.Listener {
    private static App instance;
    private final ActionResolver actionResolver;
    private final PrioritizedInputMultiplexer inputMultiplexer = new PrioritizedInputMultiplexer();
    private final EventManager eventManager = new EventManager();
    private final Model model = new Model(eventManager);
    private final PauseManager pauseManager = new PauseManager();
    private AssetManager assets;

    // Scene that draws above any screen and lives outside of regular screen lifecycle
    private OverlayScene overlayScene;

    public static App inst() {
        if (instance == null) {
            throw new NullPointerException("Instance is not available yet");
        }
        return instance;
    }

    public App(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {
        instance = this;
        // To initialize input
        resume();

        inputMultiplexer.addProcessor(new GlobalInputHandler(), Integer.MIN_VALUE);

        showScreen(new LoadScreen(this), false);
    }

    @Override
    public void onLoadComplete(AssetManager assets) {
        this.assets = assets;

        model.initTestDocument();

        overlayScene = new OverlayScene(assets);
        overlayScene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        showScreen(new MainScreen(), false);
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void pause() {
        super.pause();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render() {
        // Constrain delta to max 1/20
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f/20f);
        float deltaAffected = pauseManager.affect(delta);

        Screen screen = getCurrentScreen();
        if (screen != null) {
            screen.render(deltaAffected);
        }

        if (overlayScene != null) { overlayScene.render(delta); }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (overlayScene != null) { overlayScene.resize(width, height); }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (overlayScene != null) { overlayScene.dispose(); }
        if (assets != null) {
            assets.dispose();
            assets = null;
        }
    }

    //region Accessors
    public PrioritizedInputMultiplexer getInput() {
        return inputMultiplexer;
    }

    public EventManager getEvents() {
        return eventManager;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Model getModel() {
        return model;
    }

    public PauseManager getPauseManager() {
        return pauseManager;
    }
    //endregion

    private class GlobalInputHandler extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Keys.F11: {
                    GlobalSuspendEvent.dispatchHold();
                    return true;
                }
                case Keys.F12: {
                    GlobalSuspendEvent.dispatchRelease();
                    return true;
                }
                case Keys.Q: {
                    actionResolver.fileChooser(new FileChooserParams().save().extensions("png", "fnt"), new FileChooserListener() {
                        @Override
                        public void onSuccess(FileHandle fileHandle) {
                            System.out.println();
                        }

                        @Override
                        public void onCanceled() {
                            System.out.println();
                        }
                    });
                    return true;
                }
                case Keys.S: {
                    if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                        model.saveDocument();
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
    }
}
