package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.model.GlyphModel;

class BBClickSelectionInputHandler extends InputListener {

    private final MainScreenContext ctx;

    public BBClickSelectionInputHandler(MainScreenContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0 && !event.isHandled()) {

            int regionHeight = App.inst().getModel().getFontDocument().getFont().getRegion().getRegionHeight();
            Array<GlyphModel> glyphs = App.inst().getModel().getFontDocument().getGlyphs();

            for (GlyphModel glyph : glyphs) {
                float left = glyph.x;
                float right = left + glyph.width;
                float bot = regionHeight - glyph.y - glyph.height;
                float top = bot + glyph.height;

                if (x > left && x < right && y > bot && y < top) {
                    ctx.getSelectionManager().setSelectedGlyph(glyph);
                    return true;
                }
            }
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
