package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.CaptureScrollOnHover;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.EditBoxContainer;
import com.metaphore.bmfmetaedit.mainscreen.view.preview.PreviewHolder;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class RootTable extends Table {
    public RootTable(MainScreenContext ctx) {

        GlyphGrid glyphGrid = new GlyphGrid(ctx);
        ScrollPane glyphGridScroller = new ScrollPane(glyphGrid);
        glyphGridScroller.setScrollingDisabled(true, false);
        glyphGridScroller.addListener(new CaptureScrollOnHover(glyphGridScroller));
        glyphGrid.setParenScrollPane(glyphGridScroller);

        // Right pane
        WidgetGroup rightPane = new WidgetGroup();
        rightPane.setTransform(false);
        {
            PreviewHolder previewHolder = new PreviewHolder(ctx);
            previewHolder.setFillParent(true);
            rightPane.addActor(previewHolder);

            EditBoxContainer editBoxContainer = new EditBoxContainer(ctx);
            rightPane.addActor(editBoxContainer);
        }

        SplitPane splitPane = new SplitPane(glyphGridScroller, rightPane, false, new SplitPane.SplitPaneStyle(
                new SplitPaneDrawable(ctx.getResources().atlas)
        ));

        add(splitPane).fill().expand(true, true);
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
