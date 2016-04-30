package com.metaphore.bmfmetaedit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.crashinvaders.common.eventmanager.EventManager;
import com.crashinvaders.screenmanager.StackScreenManager;
import com.metaphore.bmfmetaedit.loadscreen.LoadScreen;
import com.metaphore.bmfmetaedit.mainscreen.MainScreen;
import com.metaphore.bmfmetaedit.model.Model;

public class App extends StackScreenManager implements LoadScreen.Listener {
    private static App instance;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final EventManager eventManager = new EventManager();
    private final Model model = new Model(eventManager);
    private AssetManager assets;

    public static App inst() {
        if (instance == null) {
            throw new NullPointerException("Instance is not available yet");
        }
        return instance;
    }

    @Override
    public void create() {
        instance = this;
        // To initialize input
        resume();


        showScreen(new LoadScreen(this), false);
    }

    @Override
    public void onLoadComplete(AssetManager assets) {
        this.assets = assets;
        model.initTestDocument();
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

    public InputMultiplexer getInput() {
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
}
