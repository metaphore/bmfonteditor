package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.squareup.otto.Bus;

public class Model {

    private final Bus bus;
    private FontDocument fontDocument;

    public Model(Bus bus) {
        this.bus = bus;
    }

    public void initTestDocument() {
        fontDocument = FontDocument.createFromFont(Gdx.files.internal("test/nokia8.fnt").file());
    }

    public FontDocument getFontDocument() {
        return fontDocument;
    }
}
