package com.metaphore.bmfmetaedit.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class KeyBinds extends InputListener {
    private final MainScreenContext ctx;

    public KeyBinds(MainScreenContext ctx) {
        this.ctx = ctx;

        ctx.getStage().addListener(this);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Keys.N: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    //TODO new
                }
                break;
            }
            case Keys.DEL: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    //TODO del selected
                }
                break;
            }
        }
        return super.keyDown(event, keycode);
    }
}
