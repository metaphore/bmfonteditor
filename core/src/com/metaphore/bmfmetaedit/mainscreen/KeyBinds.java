package com.metaphore.bmfmetaedit.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserParams;
import com.metaphore.bmfmetaedit.mainscreen.view.dialogs.CreateGlyphDialog;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.Model;

import java.io.File;

public class KeyBinds extends InputListener {
    private final MainScreenContext ctx;
    private final Model model;

    public KeyBinds(MainScreenContext ctx) {
        this.ctx = ctx;
        model = App.inst().getModel();

        ctx.getStage().addListener(this);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Keys.N: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Create new glyph

                    new CreateGlyphDialog(ctx.getResources())
                            .onResult(r -> {
                                GlyphModel glyph = model.getFontDocument().createGlyph(r.code);
                                ctx.getSelectionManager().setSelectedGlyph(glyph);
                            })
                            .show(ctx.getStage());
                }
                break;
            }
            case Keys.FORWARD_DEL: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Delete selected glyph

                    GlyphModel selectedGlyph = ctx.getSelectionManager().getSelectedGlyph();
                    if (selectedGlyph != null) {
                        model.getFontDocument().deleteGlyph(selectedGlyph);
                    }
                }
                break;
            }
            case Keys.S: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Save current font

                    File fontFile = model.getFontDocument().getFont().getData().fontFile.file();
                    App.inst().fileChoose(new FileChooserParams().save().title("Save as").extensions("fnt").rootDir(fontFile), (success, fileHandle) -> {
                        if (success) model.saveDocument(fileHandle);
                    });
                    return true;
                }
                break;
            }
        }
        return super.keyDown(event, keycode);
    }
}
