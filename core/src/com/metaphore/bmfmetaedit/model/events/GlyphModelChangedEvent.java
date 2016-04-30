package com.metaphore.bmfmetaedit.model.events;

import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphModelChangedEvent implements EventInfo {
    private final GlyphModel glyphModel;
    private final Type type;

    public GlyphModelChangedEvent(GlyphModel glyphModel, Type type) {
        this.glyphModel = glyphModel;
        this.type = type;
    }

    public GlyphModel getGlyphModel() {
        return glyphModel;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        UPDATED,
        CREATED,
        REMOVED
    }
}
