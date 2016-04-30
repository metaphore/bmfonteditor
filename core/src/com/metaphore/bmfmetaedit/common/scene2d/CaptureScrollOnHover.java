package com.metaphore.bmfmetaedit.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class CaptureScrollOnHover extends InputListener {
    protected final Actor target;

    public CaptureScrollOnHover(Actor target) {
        this.target = target;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer != -1) return;

        target.getStage().setScrollFocus(target);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer != -1) return;

        target.getStage().setScrollFocus(null);
    }
}
