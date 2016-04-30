package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.model.FontDocument;

public class PreviewCanvas extends Group {

    public PreviewCanvas(MainScreenContext ctx) {
        FontDocument fontDocument = App.inst().getModel().getFontDocument();
        TextureRegion region = fontDocument.getFont().getRegion();
        setSize(region.getRegionWidth(), region.getRegionHeight());

        Image fontPage = new Image(region);
        addActor(fontPage);

        addActor(new SelectionOverlay(ctx));

        addActor(new BBClickSelectionOverlay(ctx));

        addActor(new AdjustPixelsBBGeneratorOverlay(ctx));
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if (actor instanceof Overlay) {
            Overlay overlay = (Overlay) actor;
            overlay.setBounds(0f, 0f, getWidth(), getHeight());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
