package com.metaphore.bmfmetaedit.mainscreen.selection;

import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphSelectionManager {
    private final EventManager eventManager;
    private GlyphModel selectedGlyph;

    public GlyphSelectionManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setSelectedGlyph(GlyphModel selectedGlyph) {
        this.selectedGlyph = selectedGlyph;
        eventManager.dispatchEvent(new GlyphSelectionChangedEvent(selectedGlyph));
    }

    public GlyphModel getSelectedGlyph() {
        return selectedGlyph;
    }
}
