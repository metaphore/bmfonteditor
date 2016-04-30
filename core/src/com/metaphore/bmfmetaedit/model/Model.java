package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.crashinvaders.common.eventmanager.EventManager;

public class Model {

    private final EventManager eventManager;
    private FontDocument fontDocument;

    public Model(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void initTestDocument() {
        fontDocument = FontDocument.createFromFont(Gdx.files.internal("test/nokia8.fnt").file());
    }

    public FontDocument getFontDocument() {
        return fontDocument;
    }
}
