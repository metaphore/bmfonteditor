package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;

public class PreviewHolder extends WidgetGroup {

    private final Texture background;
    private float bgScale = 64f;

    public PreviewHolder(MainResources resources) {
        PreviewCanvas previewCanvas = new PreviewCanvas(resources);
        addActor(previewCanvas);

        addListener(new DragInputListener(previewCanvas));
        setTouchable(Touchable.enabled);

        background = resources.assets.get("textures/page_canvas_bg_checker.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        batch.draw(background, getX(), getY(), getWidth(), getHeight(), 0f, 0f, getWidth()/background.getWidth()/bgScale, getHeight()/background.getHeight()/bgScale);
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        batch.flush();
        clipBegin(0f, 0f, getWidth(), getHeight());
        super.drawChildren(batch, parentAlpha);
        batch.flush();
        clipEnd();
    }

    private static class DragInputListener extends InputListener {
        private final Actor actor;
        private float lastX, lastY;

        public DragInputListener(Actor actor) {
            this.actor = actor;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            lastX = x;
            lastY = y;
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            float xDif = x - lastX;
            float yDif = y - lastY;
            actor.moveBy(xDif, yDif);

            lastX = x;
            lastY = y;
        }
    }
}
