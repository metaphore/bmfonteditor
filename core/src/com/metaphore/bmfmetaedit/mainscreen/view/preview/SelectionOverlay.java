package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class SelectionOverlay extends Actor implements Overlay, EventHandler {
    private final MainScreenContext ctx;
    private final Array<GlyphModel> selectedGlyphs = new Array<>();

    public SelectionOverlay(MainScreenContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            ctx.getEvents().addHandler(this,
                    GlyphSelectionChangedEvent.class);
        } else {
            ctx.getEvents().removeHandler(this);
        }
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof GlyphSelectionChangedEvent) {
            GlyphSelectionChangedEvent e = (GlyphSelectionChangedEvent) event;

            selectedGlyphs.clear();
            selectedGlyphs.add(e.getSelectedGlyph());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (selectedGlyphs.size == 0) return;

        batch.end();
        ShapeRenderer shaper = ctx.getResources().shapeRenderer;
        shaper.setProjectionMatrix(batch.getProjectionMatrix());
        shaper.setTransformMatrix(batch.getTransformMatrix());
        shaper.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < selectedGlyphs.size; i++) {
            GlyphModel glyph = selectedGlyphs.get(i);
            shaper.setColor(Color.GREEN);
            shaper.rect(glyph.x, getHeight() - glyph.y - glyph.height, glyph.width, glyph.height);
        }
        shaper.end();
        batch.begin();
    }
}
