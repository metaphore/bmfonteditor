package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.model.PageModel;
import com.metaphore.bmfmetaedit.model.events.PageTextureUpdatedEvent;

public class PreviewCanvas extends Group implements EventHandler {

    private PageModel pageModel;

    public PreviewCanvas(MainScreenContext ctx) {
//        FontDocument fontDocument = App.inst().getModel().getFontDocument();
//        TextureRegion region = fontDocument.getFont().getRegion();
//        setSize(region.getRegionWidth(), region.getRegionHeight());

        setupPage(App.inst().getModel().getFontDocument().getPages().first());

        addActor(new SelectionOverlay(ctx));

        addListener(new AdjacentPixelsBBGeneratorInputHandler(ctx));

        addListener(new BBClickSelectionInputHandler(ctx));
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
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            App.inst().getEvents().addHandler(this, PageTextureUpdatedEvent.class);
        } else {
            App.inst().getEvents().removeHandler(this);
        }
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof PageTextureUpdatedEvent) {
            PageTextureUpdatedEvent e = (PageTextureUpdatedEvent) event;
            PageModel pageModel = e.getPageModel();
            setupPage(pageModel);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (pageModel != null) {
            batch.draw(pageModel.getPageTexture(), getX(), getY(), getWidth(), getHeight());
        }

        super.draw(batch, parentAlpha);
    }

    private void setupPage(PageModel pageModel) {
        this.pageModel = pageModel;
        Texture pageTexture = this.pageModel.getPageTexture();

        setSize(pageTexture.getWidth(), pageTexture.getHeight());
        for (Actor actor : getChildren()) {
            if (actor instanceof Overlay) {
                Overlay overlay = (Overlay) actor;
                overlay.setBounds(0f, 0f, getWidth(), getHeight());
            }
        }
    }
}
