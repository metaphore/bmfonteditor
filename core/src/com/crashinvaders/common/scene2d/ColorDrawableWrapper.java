package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

public class ColorDrawableWrapper implements Drawable, TransformDrawable {
    private static final Color TMP_COLOR = new Color();

    private final Drawable drawable;
    private final TransformDrawable transformDrawable;
    private final Color color = new Color(Color.WHITE);

    public ColorDrawableWrapper(Drawable drawable) {
        this.drawable = drawable;

        if (drawable instanceof TransformDrawable) {
            transformDrawable = (TransformDrawable) drawable;
        } else {
            transformDrawable = null;
        }
    }

    public ColorDrawableWrapper setColor(Color color) {
        this.color.set(color);
        return this;
    }

    public Color getColor() {
        return color;
    }

    //region Drawable delegation methods
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color color = TMP_COLOR.set(this.color).mul(batch.getColor());
        batch.setColor(color);

        drawable.draw(batch, x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        Color color = TMP_COLOR.set(this.color).mul(batch.getColor());
        batch.setColor(color);

        if (transformDrawable != null) {
            transformDrawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        } else {
            drawable.draw(batch, x, y, width, height);
        }
    }

    @Override
    public float getLeftWidth() {
        return drawable.getLeftWidth();
    }

    @Override
    public void setLeftWidth(float leftWidth) {
        drawable.setLeftWidth(leftWidth);
    }

    @Override
    public float getRightWidth() {
        return drawable.getRightWidth();
    }

    @Override
    public void setRightWidth(float rightWidth) {
        drawable.setRightWidth(rightWidth);
    }

    @Override
    public float getTopHeight() {
        return drawable.getTopHeight();
    }

    @Override
    public void setTopHeight(float topHeight) {
        drawable.setTopHeight(topHeight);
    }

    @Override
    public float getBottomHeight() {
        return drawable.getBottomHeight();
    }

    @Override
    public void setBottomHeight(float bottomHeight) {
        drawable.setBottomHeight(bottomHeight);
    }

    @Override
    public float getMinWidth() {
        return drawable.getMinWidth();
    }

    @Override
    public void setMinWidth(float minWidth) {
        drawable.setMinWidth(minWidth);
    }

    @Override
    public float getMinHeight() {
        return drawable.getMinHeight();
    }

    @Override
    public void setMinHeight(float minHeight) {
        drawable.setMinHeight(minHeight);
    }
    //endregion
}
