package com.metaphore.bmfmetaedit.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserParams;
import com.metaphore.bmfmetaedit.mainscreen.view.dialogs.CreateGlyphDialog;
import com.metaphore.bmfmetaedit.mainscreen.view.dialogs.CreateGlyphSpanDialog;
import com.metaphore.bmfmetaedit.mainscreen.view.dialogs.FontTestDialog;
import com.metaphore.bmfmetaedit.mainscreen.view.dialogs.ReadKeyCharDialog;
import com.metaphore.bmfmetaedit.model.FontDocument;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.Model;
import jdk.javadoc.internal.doclets.toolkit.taglets.UserTaglet;

import java.io.File;

public class KeyBinds extends InputAdapter {
    private final MainScreenContext ctx;
    private final Model model;

    public KeyBinds(MainScreenContext ctx) {
        this.ctx = ctx;
        model = App.inst().getModel();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.N: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                    // Create range of glyphs
                    new CreateGlyphSpanDialog(ctx.getResources())
                            .onResult(r -> {
                                GlyphModel lastCreated = null;
                                for (int i = r.first; i <= r.last; i++) {
                                    lastCreated = model.getFontDocument().createGlyph(i);
                                }
                                ctx.getSelectionManager().setSelectedGlyph(lastCreated);
                            })
                            .show(ctx.getStage());
                    return true;
                }
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Create new glyph
                    new CreateGlyphDialog(ctx.getResources()).onResult(r -> {
                        GlyphModel glyph = model.getFontDocument().createGlyph(r.code);
                        ctx.getSelectionManager().setSelectedGlyph(glyph);
                    }).show(ctx.getStage());
                    return true;
                }
                return false;
            }
            case Keys.FORWARD_DEL: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Delete selected glyph
                    GlyphModel selectedGlyph = ctx.getSelectionManager().getSelectedGlyph();
                    if (selectedGlyph != null) {
                        model.getFontDocument().deleteGlyph(selectedGlyph);
                    }
                    return true;
                }
                return false;
            }
            case Keys.S: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Save current font
                    File fontFile = model.getFontDocument().getFont().getData().fontFile.file();
                    App.inst().showFileChooser(new FileChooserParams()
                                    .save()
                                    .title("Save as")
                                    .extensions("fnt")
                                    .rootDir(fontFile),
                            (success, fileHandle) -> {
                                if (success) model.saveDocument(fileHandle);
                            });
                    return true;
                }
                return false;
            }
            case Keys.F: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Find glyph
                    // Post is just to skip current key down event (otherwise dialog will catch it instantly)
                    Gdx.app.postRunnable(() -> new ReadKeyCharDialog(ctx.getResources())
                            .onResult(charCode -> {
                                GlyphModel glyphModel = model.getFontDocument().findGlyphByCode(charCode);
                                if (glyphModel != null) {
                                    ctx.getSelectionManager().setSelectedGlyph(glyphModel);
                                }
                            })
                            .show(ctx.getStage()));
                    return true;
                }
                return false;
            }
            case Keys.T: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    // Test current font
                    new FontTestDialog(ctx.getResources())
                            .show(ctx.getStage());
                    return false;
                }
                return false;
            }
            case Keys.I: {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                    // Copy capital letters to small letters.
                    int firstCodeCapital = 65;
                    int firstCodeSmall = 97;
                    int alphabetSize = 26;
                    FontDocument document = model.getFontDocument();
                    for (int i = 0; i < alphabetSize; i++) {
                        int codeSrc = firstCodeCapital + i;
                        int codeDst = firstCodeSmall + i;
                        GlyphModel glyphSrc = document.findGlyphByCode(codeSrc);
                        if (glyphSrc == null) throw new GdxRuntimeException("Cannot find glyph with code: " + codeSrc);
                        GlyphModel glyphDst = document.findGlyphByCode(codeDst);
                        if (glyphDst == null) {
                            glyphDst = document.createGlyph(codeDst);
                        }
                        glyphDst.copyFrom(glyphSrc);
                        glyphDst.code = codeDst;
                        document.saveGlyphData(glyphDst);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
