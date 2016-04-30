package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.events.GlyphModelChangedEvent;

import java.util.HashMap;
import java.util.Map;

public class GlyphGrid extends VerticalGroup implements EventHandler {
    private static final Vector2 TMP_VEC2 = new Vector2();
    public static final float SPACE_X = 4f;

    private final MainScreenContext ctx;
    private final ArrayMap<GlyphModel, GlyphItem> index;

    private boolean rearrangeRequired = true;
    private ScrollPane parenScrollPane;

    public GlyphGrid(MainScreenContext ctx) {
        this.ctx = ctx;
        setTransform(false);

        // Generate items
        Array<GlyphModel> glyphs = App.inst().getModel().getFontDocument().getGlyphs();
        index = new ArrayMap<>(glyphs.size);
        for (int i = 0; i < glyphs.size; i++) {
            GlyphModel model = glyphs.get(i);
            createGlyphItem(model);
//            GlyphItem glyphItem = new GlyphItem(ctx.getResources(), model);
//            glyphItem.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    ctx.getSelectionManager().setSelectedGlyph(model);
//                }
//            });
//            index.put(model, glyphItem);
        }
    }

    public void setParenScrollPane(ScrollPane parenScrollPane) {
        this.parenScrollPane = parenScrollPane;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            ctx.getEvents().addHandler(this,
                    GlyphSelectionChangedEvent.class,
                    GlyphModelChangedEvent.class
            );
        } else {
            ctx.getEvents().removeHandler(this);
        }
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof GlyphSelectionChangedEvent) {
            GlyphSelectionChangedEvent e = (GlyphSelectionChangedEvent) event;
            updateSelection(e.getSelectedGlyph());

        } else if (event instanceof GlyphModelChangedEvent) {
            GlyphModelChangedEvent e = (GlyphModelChangedEvent) event;
            GlyphModel glyphModel = e.getGlyphModel();
            switch (e.getType()) {
                case UPDATED:
                    updateGlyphItem(glyphModel);
                    break;
                case CREATED:
                    createGlyphItem(glyphModel);
                    break;
                case REMOVED:
                    removeGlyphItem(glyphModel);
                    break;
            }
        }
    }

    private void updateSelection(GlyphModel selectedGlyph) {
        GlyphItem lastSelected = null;
        for (int i = 0; i < index.size; i++) {
            GlyphItem glyphItem = index.getValueAt(i);
            boolean selected = glyphItem.getModel() == selectedGlyph;
            glyphItem.setSelected(selected);

            if (selected) lastSelected = glyphItem;
        }
        if (lastSelected != null && parenScrollPane != null) {
            Vector2 scrollPos = lastSelected.localToAscendantCoordinates(this, TMP_VEC2.set(0f, 0f));
            parenScrollPane.scrollTo(scrollPos.x, scrollPos.y,
                    lastSelected.getWidth(), lastSelected.getHeight(), false, true);
        }
    }


    private void createGlyphItem(GlyphModel glyphModel) {
        GlyphItem glyphItem = new GlyphItem(ctx.getResources(), glyphModel);
        glyphItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ctx.getSelectionManager().setSelectedGlyph(glyphModel);
            }
        });
        index.put(glyphModel, glyphItem);

        rearrangeRequired = true;
        invalidate();
    }
    private void removeGlyphItem(GlyphModel glyphModel) {
        GlyphItem glyphItem = index.removeKey(glyphModel);
        glyphItem.remove();

        rearrangeRequired = true;
        invalidate();
    }
    private void updateGlyphItem(GlyphModel glyphModel) {
        GlyphItem glyphItem = index.get(glyphModel);
        glyphItem.mapFromModel();
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

            // Optimize rows with pools

            int idx = 0;
            int idxInRow = 0;
            float rowWidth = 0f;
            HorizontalGroup row = new HorizontalGroup();
            row.setTransform(false);
            row.space(SPACE_X);
            while (idx < index.size) {
                GlyphItem glyphItem = index.getValueAt(idx++);
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
