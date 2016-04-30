package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphGrid extends VerticalGroup {
//    public static final int COLUMNS = 4;
    public static final float SPACE_X = 4f;
//    public static final float SPACE_Y = 4f;

    private final Array<GlyphItem> glyphItems;

    private boolean rearrangeRequired = true;

    public GlyphGrid(MainResources resources, Array<GlyphModel> glyphs) {
        setTransform(false);

        glyphItems = new Array<>(glyphs.size);
        for (int i = 0; i < glyphs.size; i++) {
            GlyphModel glyph = glyphs.get(i);
            GlyphItem glyphItem = new GlyphItem(resources, glyph);
            glyphItems.add(glyphItem);
        }
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        rearrangeRequired = true;
    }

    @Override
    public void layout() {
        if (rearrangeRequired) {
            rearrangeRequired = false;
            float width = getWidth();

            int idx = 0;
            int idxInRow = 0;
            float rowWidth = 0f;
            HorizontalGroup row = new HorizontalGroup();
            row.setTransform(false);
            row.space(SPACE_X);
            while (idx<glyphItems.size) {
                GlyphItem glyphItem = glyphItems.get(idx++);
                glyphItem.pack();

                boolean fitsRow = glyphItem.getWidth() <= (width - rowWidth);
                if (!fitsRow && idxInRow == 0) {
                    addActor(glyphItem);
                    continue;
                }

                if (fitsRow) {
                    row.addActor(glyphItem);
                    rowWidth += glyphItem.getWidth()+SPACE_X;
                    idxInRow++;
                } else {
                    addActor(row);
                    row = new HorizontalGroup();
                    row.setTransform(false);
                    row.space(SPACE_X);
                    row.addActor(glyphItem);
                    rowWidth = glyphItem.getWidth()+SPACE_X;
                    idxInRow = 1;
                }
            }

            if (idxInRow > 0) {
                addActor(row);
            }
        }

        super.layout();
    }
}
