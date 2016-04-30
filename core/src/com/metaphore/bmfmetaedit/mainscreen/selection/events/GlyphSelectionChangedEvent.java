package com.metaphore.bmfmetaedit.mainscreen.selection.events;

import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphSelectionChangedEvent implements EventInfo {
    private final GlyphModel selectedGlyph;

    public GlyphSelectionChangedEvent(GlyphModel selectedGlyph) {
        this.selectedGlyph = selectedGlyph;
    }

    public GlyphModel getSelectedGlyph() {
        return selectedGlyph;
    }
}
