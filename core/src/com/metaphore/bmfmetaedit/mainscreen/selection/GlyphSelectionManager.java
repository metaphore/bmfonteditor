package com.metaphore.bmfmetaedit.mainscreen.selection;

import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.events.GlyphModelChangedEvent;

public class GlyphSelectionManager implements EventHandler {
    private final EventManager eventManager;
    private GlyphModel selectedGlyph;

    public GlyphSelectionManager(EventManager eventManager) {
        this.eventManager = eventManager;
        eventManager.addHandler(this, GlyphModelChangedEvent.class);
    }

    public void dispose() {
        eventManager.removeHandler(this);
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof GlyphModelChangedEvent) {
            GlyphModelChangedEvent e = (GlyphModelChangedEvent) event;
            if (e.getType() == GlyphModelChangedEvent.Type.REMOVED && e.getGlyphModel() == selectedGlyph) {
                setSelectedGlyph(null);
            }
        }
    }

    public void setSelectedGlyph(GlyphModel selectedGlyph) {
        this.selectedGlyph = selectedGlyph;
        eventManager.dispatchEvent(new GlyphSelectionChangedEvent(selectedGlyph));
    }

    public GlyphModel getSelectedGlyph() {
        return selectedGlyph;
    }
}
