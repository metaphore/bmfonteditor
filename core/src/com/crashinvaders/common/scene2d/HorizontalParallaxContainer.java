package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;

public class HorizontalParallaxContainer extends WidgetGroup {

    private final Actor actor;
    private float parallaxFactor = 0f;
    private ShiftFactorProvider shiftProvider;

    private final Rectangle cullingArea = new Rectangle();

    public HorizontalParallaxContainer(Actor actor) {
        this.actor = actor;
        addActor(actor);

        setTransform(false);
        setTouchable(Touchable.childrenOnly);
    }

    public void setParallaxFactor(float parallaxFactor) {
        this.parallaxFactor = parallaxFactor;
        invalidate();
    }

    public void setShiftProvider(ShiftFactorProvider shiftProvider) {
        this.shiftProvider = shiftProvider;
    }

    @Override
    public void layout() {
        super.layout();

        actor.setBounds(
                getX(),
                getY(),
                getWidth() * (1f + parallaxFactor),
                getHeight()
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (shiftProvider != null) {
            float scrollFactor = shiftProvider.getShiftFactor();
            float xShift = (actor.getWidth() - getWidth()) * scrollFactor;
            actor.setPosition(getX() - xShift, getY());

            if (actor instanceof Cullable) {
                Cullable cullable = (Cullable) actor;
                cullingArea.set(-actor.getX(), actor.getY(), getWidth(), getHeight());
                cullable.setCullingArea(cullingArea);
            }
        }

        super.draw(batch, parentAlpha);
    }

    public interface ShiftFactorProvider {
        /** @return value within [0..1] */
        float getShiftFactor();
    }
}
