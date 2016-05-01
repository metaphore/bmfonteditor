package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
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
        private static final float SCALE_FACTOR = 0.25f;

        private ScaleToAction scaleAction;

        public ScaleInputHandler(Actor target) {
            super(target);

            scaleAction = new ScaleToAction();
            scaleAction.setInterpolation(Interpolation.pow4Out);
            scaleAction.setDuration(0.25f);
        }

        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
            float scaleFactor = 1f + SCALE_FACTOR*amount;

            target.removeAction(scaleAction);
            scaleAction.restart();
            scaleAction.setScale(target.getScaleX() * scaleFactor);
            target.addAction(scaleAction);

            return true;
        }
    }
}
