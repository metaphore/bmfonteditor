package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.CaptureScrollOnHover;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
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
                glyphModel.x = glyph.srcX;
                glyphModel.y = glyph.srcY;
                glyphModel.width = glyph.width;
                glyphModel.height = glyph.height;
                glyphModel.xoffset = glyph.xoffset;
                glyphModel.yoffset = glyph.yoffset;
                glyphModel.xadvance = glyph.xadvance;
                glyphs.add(glyphModel);
            }
        }

        GlyphGrid glyphGrid = new GlyphGrid(ctx, glyphs);
        ScrollPane glyphListScroller = new ScrollPane(glyphGrid);
        glyphListScroller.setScrollingDisabled(true, false);
        glyphListScroller.addListener(new CaptureScrollOnHover(glyphListScroller));

        PreviewHolder previewHolder = new PreviewHolder(ctx);

        SplitPane splitPane = new SplitPane(glyphListScroller, previewHolder, false, new SplitPane.SplitPaneStyle(
                new SplitPaneDrawable(ctx.getResources().atlas)
        ));

        add(splitPane).fill().expand(true, true);
//        add(glyphListScroller).expandY().fillY().align(Align.topLeft).padLeft(4f).padRight(4f).width(400f);
//        add(previewHolder).expand().fill();
    }

    private static class SplitPaneDrawable extends BaseDrawable {

        private final TextureAtlas.AtlasRegion fill;
        private final TextureAtlas.AtlasRegion handle;

        public SplitPaneDrawable(TextureAtlas atlas) {
            fill = atlas.findRegion("split_pane_drawer_bg");
            handle = atlas.findRegion("split_pane_drawer_dots");

            setMinWidth(16f);
            setMinHeight(32f);
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            batch.draw(fill, x, y, width, height);
            batch.draw(handle, x + (width-handle.getRegionWidth())*0.5f, y + (height-handle.getRegionHeight())*0.5f);
        }
    }
}
