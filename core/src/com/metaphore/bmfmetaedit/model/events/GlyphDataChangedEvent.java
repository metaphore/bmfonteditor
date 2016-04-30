package com.metaphore.bmfmetaedit.model.events;

import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphDataChangedEvent implements EventInfo {
    private final GlyphModel glyphModel;

    public GlyphDataChangedEvent(GlyphModel glyphModel) {
        this.glyphModel = glyphModel;
    }

    public GlyphModel getGlyphModel() {
        return glyphModel;
    }
}
