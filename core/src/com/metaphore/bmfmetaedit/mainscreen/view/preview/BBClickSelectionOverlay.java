package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.events.GlyphModelChangedEvent;

public class BBClickSelectionOverlay extends Actor implements Overlay {
    private final BBClickSelectionInputHandler bbClickSelection;
    private final MainScreenContext ctx;

    public BBClickSelectionOverlay(MainScreenContext ctx) {
        this.ctx = ctx;
        setTouchable(Touchable.enabled);
        addListener(bbClickSelection = new BBClickSelectionInputHandler());
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            Array<GlyphModel> glyphs = App.inst().getModel().getFontDocument().getGlyphs();
            bbClickSelection.init(glyphs);
        } else {
            bbClickSelection.dispose();
        }
    }

    //TODO subscribe model change events
    private class BBClickSelectionInputHandler extends InputListener implements EventHandler {
        private final ArrayMap<GlyphModel, GlyphBB> index = new ArrayMap<>(1024);

        public void init(Array<GlyphModel> glyphs) {
            for (GlyphModel model : glyphs) {
                createGlyphBB(model);
            }

            ctx.getEvents().addHandler(this, GlyphModelChangedEvent.class);
        }

        public void dispose() {
            index.clear();
            ctx.getEvents().removeHandler(this);
        }

        @Override
        public void handle(EventInfo event) {
            if (event instanceof GlyphModelChangedEvent) {
                GlyphModelChangedEvent e = (GlyphModelChangedEvent) event;
                GlyphModel glyphModel = e.getGlyphModel();
                switch (e.getType()) {
                    case UPDATED:
                        updateGlyphBB(glyphModel);
                        break;
                    case CREATED:
                        createGlyphBB(glyphModel);
                        break;
                    case REMOVED:
                        removeGlyphBB(glyphModel);
                        break;
                }
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (button == 0) {
                for (int i = 0; i < index.size; i++) {
                    GlyphBB glyphBB = index.getValueAt(i);
                    if (glyphBB.hit(x, y)) {
                        ctx.getSelectionManager().setSelectedGlyph(glyphBB.getModel());
                        return true;
                    }
                }
            }

            return super.touchDown(event, x, y, pointer, button);
        }

        private void createGlyphBB(GlyphModel model) {
            index.put(model, new GlyphBB(model));
        }
        private void removeGlyphBB(GlyphModel model) {
            index.removeKey(model);
        }
        private void updateGlyphBB(GlyphModel model) {
            GlyphBB glyphBB = index.get(model);
            glyphBB.updateBB();
        }
    }

    private class GlyphBB {
        private final GlyphModel model;
        private final Rectangle bb = new Rectangle();

        public GlyphBB(GlyphModel model) {
            this.model = model;
            updateBB();
        }

        public boolean hit(float x, float y) {
            return bb.contains(x, y);
        }

        public GlyphModel getModel() {
            return model;
        }

        private Rectangle updateBB() {
            float y = getHeight() - model.y - model.height;
            return bb.set(model.x, y, model.width, model.height);
        }
    }
}
