package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

@SuppressWarnings("unused")
public class StageX extends Stage {
    private final Array<Actor> keyboardFocusQueue = new Array<>(true, 8);

    public StageX() {
    }

    public StageX(Viewport viewport) {
        super(viewport);
    }

    public StageX(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    /**
     * @deprecated Use {@link StageX#addKeyboardFocus} instead
     */
    @Deprecated
    @Override
    public void setKeyboardFocus(Actor actor) {
        throw new UnsupportedOperationException("This method is unsupported anymore. Use StageX#addKeyboardFocus instead.");
    }

    public void addKeyboardFocus(Actor actor) {
        if (actor == null) throw new NullPointerException();

        super.setKeyboardFocus(actor);
        keyboardFocusQueue.add(actor);
    }

    public void removeKeyboardFocus(Actor actor) {
        if (actor == null) throw new NullPointerException();

        keyboardFocusQueue.removeValue(actor, true);

        if (getKeyboardFocus() == actor) {
            if (keyboardFocusQueue.size == 0) {
                super.setKeyboardFocus(null);
            } else {
                Actor nextKeyboardFocus = keyboardFocusQueue.peek();
                super.setKeyboardFocus(nextKeyboardFocus);
            }
        }
    }

    @Override
    public void unfocus(Actor actor) {
        Actor keyboardFocus = getKeyboardFocus();
        if (keyboardFocus != null && keyboardFocus.isDescendantOf(actor)) {
            removeKeyboardFocus(keyboardFocus);
        }

        super.unfocus(actor);
    }

    @Override
    public void unfocusAll() {
        keyboardFocusQueue.clear();
        super.unfocusAll();
    }
}
