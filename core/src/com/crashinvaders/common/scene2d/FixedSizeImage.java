package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Image that take size of source drawable at first place
 */
public class FixedSizeImage extends Image {

    //region Default constructors
    public FixedSizeImage() {
    }
    public FixedSizeImage(NinePatch patch) {
        super(patch);
    }
    public FixedSizeImage(TextureRegion region) {
        super(region);
    }
    public FixedSizeImage(Texture texture) {
        super(texture);
    }
    public FixedSizeImage(Skin skin, String drawableName) {
        super(skin, drawableName);
    }
    public FixedSizeImage(Drawable drawable) {
        super(drawable);
    }
    public FixedSizeImage(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
    }
    public FixedSizeImage(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
    }
    //endregion

    @Override
    public void setDrawable(Drawable drawable) {
        super.setDrawable(drawable);

        if (drawable != null) {
            setWidth(drawable.getMinWidth());
            setHeight(drawable.getMinHeight());
        }
    }

    @Override
    public float getMinWidth () {
        return getWidth();
    }
    @Override
    public float getMinHeight () {
        return getHeight();
    }
    @Override
    public float getMaxWidth() {
        return getWidth();
    }
    @Override
    public float getMaxHeight() {
        return getHeight();
    }
    @Override
    public float getPrefWidth () {
        return getWidth();
    }
    @Override
    public float getPrefHeight () {
        return getHeight();
    }

}
