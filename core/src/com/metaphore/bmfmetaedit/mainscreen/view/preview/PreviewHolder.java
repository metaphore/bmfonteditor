package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.metaphore.bmfmetaedit.common.scene2d.CaptureScrollOnHover;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;

public class PreviewHolder extends WidgetGroup {

    private final Texture background;
    private final PreviewCanvas previewCanvas;
    private float bgScale = 16f;

    public PreviewHolder(MainScreenContext ctx) {
        this.previewCanvas = new PreviewCanvas(ctx);
        PreviewCanvas previewCanvas = this.previewCanvas;
        addActor(previewCanvas);

        addListener(new DragInputListener(previewCanvas));
        addListener(new ScaleInputHandler(this));
        setTouchable(Touchable.enabled);

        background = ctx.getResources().assets.get("textures/page_canvas_bg_checker.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        batch.draw(background, getX(), getY(), getWidth(), getHeight(), 0f, 0f,
                getWidth()/background.getWidth()/bgScale/getScaleY(), getHeight()/background.getHeight()/bgScale/getScaleX());
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        batch.flush();
        clipBegin(0f, 0f, getWidth()/getScaleX(), getHeight()/getScaleY());
        super.drawChildren(batch, parentAlpha);
        batch.flush();
        clipEnd();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        float left = 0f;
        float right = left + getWidth();
        float bot = 0f;
        float top = bot + getHeight();
        if (x < left || x > right || y < bot || y > top) return null;

        return super.hit(x, y, touchable);
    }

    private static class DragInputListener extends InputListener {
        private final Actor target;
        private float lastX, lastY;

        public DragInputListener(Actor target) {
            this.target = target;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (button != 1) return false;

            lastX = x;
            lastY = y;
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            float xDif = x - lastX;
            float yDif = y - lastY;
            target.moveBy(xDif, yDif);

            lastX = x;
            lastY = y;
        }
    }

    private static class ScaleInputHandler extends CaptureScrollOnHover {

        public ScaleInputHandler(Actor target) {
            super(target);
        }

        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
            float scaleFactor = amount > 0 ? 0.8f : 1.2f;
            target.setScale(target.getScaleX() * scaleFactor);
            return true;
        }
    }
}
