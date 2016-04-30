package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.model.FontDocument;

public class PreviewCanvas extends Actor {

    private final TextureRegion region;

    public PreviewCanvas(MainResources resources) {
        FontDocument fontDocument = App.inst().getModel().getFontDocument();
        region = fontDocument.getFont().getRegion();
        setSize(region.getRegionWidth(), region.getRegionHeight());
    }

    @Override
    protected void sizeChanged() {
        setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }
}
