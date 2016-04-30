package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.CaptureScrollOnHover;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.view.preview.PreviewHolder;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class RootTable extends Table {
    public RootTable(MainScreenContext ctx) {

        BitmapFont.BitmapFontData fontData = App.inst().getModel().getFontDocument().getFont().getData();
        Array<GlyphModel> glyphs = new Array<>(fontData.glyphs[0].length);
        for (BitmapFont.Glyph[] glyphChunk : fontData.glyphs) {
            if (glyphChunk == null) continue;
            for (BitmapFont.Glyph glyph : glyphChunk) {
                if (glyph == null) continue;

                GlyphModel glyphModel = new GlyphModel();
                glyphModel.name = "#" + glyph.id;
                glyphModel.code = glyph.id;
                glyphs.add(glyphModel);
            }
        }

        GlyphGrid glyphGrid = new GlyphGrid(ctx, glyphs);
        ScrollPane glyphListScroller = new ScrollPane(glyphGrid);
        glyphListScroller.setScrollingDisabled(true, false);
        glyphListScroller.addListener(new CaptureScrollOnHover(glyphListScroller));

        PreviewHolder previewHolder = new PreviewHolder(ctx);

        add(glyphListScroller).expandY().fillY().align(Align.topLeft).padLeft(4f).padRight(4f).width(400f);
        add(previewHolder).expand().fill();
    }
}
