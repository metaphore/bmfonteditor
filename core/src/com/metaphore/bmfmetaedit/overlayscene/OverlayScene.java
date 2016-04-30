package com.metaphore.bmfmetaedit.overlayscene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.crashinvaders.common.scene2d.StageX;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.overlayscene.events.GlobalSuspendEvent;

public class OverlayScene implements EventHandler {

    public static final String PAUSE_KEY = "overlay_scene";
    private final StageX stage;
    private final AssetManager assets;

    private SuspendActor suspendActor;

    public OverlayScene(AssetManager assets) {
        this.assets = assets;
        stage = new StageX(new ScreenViewport());
//        stage.setDebugAll(true);
        App.inst().getInput().addProcessor(stage, Integer.MIN_VALUE+1);
        App.inst().getEvents().addHandler(this, GlobalSuspendEvent.class);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        App.inst().getInput().removeProcessor(stage);
        App.inst().getEvents().removeHandler(this);
        stage.dispose();
    }

    @Override
    public void handle(EventInfo info) {
        if (info instanceof GlobalSuspendEvent) {
            GlobalSuspendEvent i = (GlobalSuspendEvent) info;
            switch (i.getActionType()) {
                case HOLD:
                    handleSuspendHold();
                    break;
                case RELEASE:
                    handleSuspendRelease();
                    break;
            }
        }
    }

    private void handleSuspendHold() {
        if (suspendActor != null) return;

        suspendActor = new SuspendActor(assets);
        stage.addActor(suspendActor);
        App.inst().getPauseManager().holdPause(PAUSE_KEY);
    }

    private void handleSuspendRelease() {
        if (suspendActor == null) return;

        suspendActor.dismiss();
        suspendActor = null;
        App.inst().getPauseManager().releasePause(PAUSE_KEY);
    }
}
