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
                    GlyphModel glyph = App.inst().getModel().getFontDocument().createGlyph(0);
                    ctx.getSelectionManager().setSelectedGlyph(glyph);
                }
                break;
            }
            case Keys.FORWARD_DEL: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    GlyphModel selectedGlyph = ctx.getSelectionManager().getSelectedGlyph();
                    if (selectedGlyph != null) {
                        App.inst().getModel().getFontDocument().deleteGlyph(selectedGlyph);
                    }
                }
                break;
            }
        }
        return super.keyDown(event, keycode);
    }
}
