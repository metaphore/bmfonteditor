package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Use {@link FadeWidget.Builder} to create fade effect
 */
public class FadeWidget extends Widget {

    private final boolean blockInput;
    private Texture texture;

    FadeWidget(boolean fadeIn, float duration, boolean blockInput, final Runnable action) {
        this.blockInput = blockInput;

        if (blockInput) {
            setTouchable(Touchable.enabled);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    return true;
                }
            });
        }

        SequenceAction sequence = new SequenceAction();
        if (fadeIn) {
            sequence.addAction(Actions.alpha(1f));
            sequence.addAction(Actions.alpha(0f, duration, Interpolation.pow2Out));
        } else {
            sequence.addAction(Actions.alpha(0f));
            sequence.addAction(Actions.alpha(1f, duration, Interpolation.pow2In));
        }
        sequence.addAction(ActionsExt.unfocus(this));
        sequence.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                getStage().setKeyboardFocus(null);
                if (action != null) {
                    Gdx.app.postRunnable(action);
                }
            }
        }));
//        sequence.addAction(Actions.run(new PostRunnable(action)));
        sequence.addAction(Actions.removeActor());
        addAction(sequence);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (blockInput) {
            if (stage != null) {
                stage.setKeyboardFocus(this);
            } else {
//                stageX.removeKeyboardFocus(this);
            }
        }

        // Manage texture
        if (stage != null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            texture = new Texture(pixmap);
            pixmap.dispose();
        } else {
            texture.dispose();
            texture = null;
        }
    }

    @Override
    public void layout() {
        super.layout();

        Stage stage = getStage();
        setWidth(stage.getWidth());
        setHeight(stage.getHeight());
        setPosition(0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//        Viewport viewport = getStage().getViewport();
//        batch.draw(texture, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

//    private static class PostRunnable implements Runnable {
//        private final Runnable runnable;
//
//        public PostRunnable(Runnable runnable) {
//            this.runnable = runnable;
//        }
//
//        @Override
//        public void run() {
//            if (runnable != null) {
//                Gdx.app.postRunnable(runnable);
//            }
//        }
//    }

    public static class Builder {
        private final Color color = Colors.get("DIM_COLOR") != null ? Colors.get("DIM_COLOR") : new Color(Color.BLACK);
        private float duration = 0.35f;
        private boolean fadeIn = true;
        private Runnable action;
        private boolean blockInput = true;

        public Builder duration(float duration) {
            this.duration = duration;
            return this;
        }

        public Builder color(Color color) {
            this.color.set(color);
            return this;
        }

        public Builder fadeIn() {
            this.fadeIn = true;
            return this;
        }

        public Builder fadeOut() {
            this.fadeIn = false;
            return this;
        }

        public Builder action(Runnable action) {
            this.action = action;
            return this;
        }

        public Builder blockInput(boolean blockInput) {
            this.blockInput = blockInput;
            return this;
        }

        public void show(Group group) {
            FadeWidget fadeWidget = new FadeWidget(fadeIn, duration, blockInput, action);
            fadeWidget.setColor(color);
            fadeWidget.setFillParent(true);
            group.addActor(fadeWidget);
        }
    }
}
