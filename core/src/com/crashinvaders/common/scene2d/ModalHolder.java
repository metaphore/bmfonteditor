package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.crashinvaders.common.timer.Timer;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Use this as container for any blocking actors (e.g. dialogs)
 */
public class ModalHolder<T extends Actor> extends Container<T> {
    public static final float DEFAULT_FADE_DURATION = 0.25f;

    private T content;

    private final Color dimTint = new Color(0x00000088);
    private Drawable dimDrawable;

    private final IntArray ignoreKeys = new IntArray();

    /** Prevents input from dismissing holder for some time */
    private final Timer cancelDismissInputTimer;

    private ContentAnimations<T> contentAnimations;
    private LifecycleListener lifecycleListener;
    private float fadeDuration = DEFAULT_FADE_DURATION;
    private boolean cancelable = false;
    private boolean consumeInput = true;
    private boolean dismissOnBack = false;

    private boolean dismissing = false;

    /**
     * You may use this CTOR to assign content later, but make sure you did,
     * otherwise exception will be thrown once ModalHolder got added to stage.
     */
    public ModalHolder() {
        this(null);
    }

    public ModalHolder(T content) {
        this.content = content;
        super.setActor(this.content);

        cancelDismissInputTimer = new Timer();

        setFillParent(true);
        align(Align.center);
        setTouchable(Touchable.enabled);

        addListener(new InputHandler());
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null && content == null) {
            throw new IllegalStateException("Content is not set");
        }

        if (stage != null) {
            if (lifecycleListener != null) {
                lifecycleListener.onShowing(this);
            }
            // Run animation on attaching to stage
            performAppearAnimation();
        }

        // Manage default dim drawable
        if (stage != null) {
            if (dimDrawable == null) {
                dimDrawable = new DefaultDimDrawable();
            }
        } else {
            if (dimDrawable != null && dimDrawable instanceof DefaultDimDrawable) {
                ((DefaultDimDrawable) dimDrawable).dispose();
                dimDrawable = null;
            }
        }

        // Keyboard focus on attached to stage
        if (consumeInput) {
            if (stage != null) {
                stage.setKeyboardFocus(this);
                stage.setScrollFocus(this);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        cancelDismissInputTimer.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (dimDrawable != null) {
            batch.setColor(dimTint.r, dimTint.g, dimTint.b, dimTint.a * getColor().a * parentAlpha);
            dimDrawable.draw(batch, 0, 0, getWidth(), getHeight());
        }

        super.draw(batch, parentAlpha);
    }

    public void dismiss() {
        if (dismissing) return;
        dismissing = true;

        if (lifecycleListener != null) {
            lifecycleListener.onDismissing(this);
        }

        Stage stage = getStage();
        stage.setKeyboardFocus(null);
        stage.setScrollFocus(null);

        setTouchable(Touchable.disabled);
        performDisappearAnimation();
    }

    /**
     * @deprecated Setting of actor is not allowed through this method. Use ModalHolder#content() instead
     */
    @Deprecated
    @Override
    public void setActor(Actor actor) {
        throw new UnsupportedOperationException("Setting of actor is not allowed after creation, use ctor.");
    }

    //region Getters & Setters

    public ModalHolder<T> content(T content) {
        this.content = content;
        super.setActor(content);
        return this;
    }

    public ModalHolder<T> dimTint(int rgba8888) {
        Color.rgba8888ToColor(dimTint, rgba8888);
        return this;
    }

    public ModalHolder<T> dimTint(Color color) {
        dimTint.set(color);
        return this;
    }

    /**
     * @param region will be drawn to fit ModalHolder size  */
    public ModalHolder<T> dimDrawable(TextureRegion region, Color tint) {
        return dimDrawable(new TextureRegionDrawable(region), tint);
    }

    /**
     * @param drawable will be drawn to fit ModalHolder size  */
    public ModalHolder<T> dimDrawable(Drawable drawable, Color tint) {
        dimDrawable = drawable;
        dimTint.set(tint);
        return this;
    }

    public ModalHolder<T> fadeDuration(float duration) {
        fadeDuration = duration;
        return this;
    }

    /**
     * Set to control appear and disappear behavior of content actor.
     * <br/>
     * <b>WARNING</b> try not to exceed ModalHolder's fade duration value.
     */
    public ModalHolder<T> contentAnimations(ContentAnimations<T> contentAnimations) {
        this.contentAnimations = contentAnimations;
        return this;
    }

    public ModalHolder<T> lifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    /** If true, ModalHolder will dismiss itself on any input that was not handled by content. */
    public ModalHolder<T> cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public boolean isConsumeInput() {
        return consumeInput;
    }

    /** If true, ModalHolder will consume any input it receives. */
    public ModalHolder<T> consumeInput(boolean consumeInput) {
        this.consumeInput = consumeInput;
        return this;
    }

    public boolean isDismissOnBack() {
        return dismissOnBack;
    }

    /**
     * Specific case to separately handle BACK/ESC keys, consume them and perform dismiss.
     * <br/>
     * This flag will ignore <code>cancelable</code> and <code>consumeInput</code> state.
     */
    public ModalHolder<T> dismissOnBack(boolean dismissOnBack) {
        this.dismissOnBack = dismissOnBack;
        return this;
    }

    /** Provide custom key handling exceptions */
    public ModalHolder<T> ignoreKeys(int... keyCodes) {
        ignoreKeys.clear();
        ignoreKeys.addAll(keyCodes);
        return this;
    }


    /** Prevents input from dismissing holder for some time */
    public ModalHolder<T> preventDismissInput(float duration) {
        cancelDismissInputTimer.start(duration, null);
        return this;
    }
    //endregion

    private void performAppearAnimation() {
        setTouchable(Touchable.enabled);

        if (contentAnimations != null) {
            contentAnimations.performAppearAnimation(content);
        }

        getColor().a = 0f;
        addAction(fadeIn(fadeDuration));
    }

    private void performDisappearAnimation() {
        setTouchable(Touchable.disabled);

        if (contentAnimations != null) {
            contentAnimations.performDisappearAnimation(content);
        }

        addAction(sequence(
                fadeOut(fadeDuration),
                Actions.removeActor(this)
        ));
    }

    public interface ContentAnimations<T extends Actor> {
        void performAppearAnimation(T content);
        void performDisappearAnimation(T content);
    }

    public interface LifecycleListener {
        void onShowing(ModalHolder modalHolder);
        void onDismissing(ModalHolder modalHolder);
    }

    private class InputHandler extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (event.isHandled()) return false;

            if (cancelable && !cancelDismissInputTimer.isRunning()) {
                dismiss();
            }
            return consumeInput;
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (event.isHandled()) return false;

            // Ignore specific keys
            if (ignoreKeys.contains(keycode)) return false;

            if (dismissOnBack) {
                switch (keycode) {
                    case Input.Keys.BACK:
                    case Input.Keys.ESCAPE:
                        dismiss();
                        return true;
                }
            }

            if (cancelable && !cancelDismissInputTimer.isRunning()) {
                dismiss();
            }
            return consumeInput;
        }
        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
            return consumeInput;
        }
    }

    private static class DefaultDimDrawable extends BaseDrawable implements Disposable {
        private final Texture texture;

        public DefaultDimDrawable() {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            texture = new Texture(pixmap);
            pixmap.dispose();
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            batch.draw(texture, x, y, width, height);
        }

        @Override
        public void dispose() {
            texture.dispose();
        }
    }
}
