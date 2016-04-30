package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScaleButton extends Widget {
    private static final Color TMP_COLOR = new Color();

    private final ClickListener clickListener;
    private final TextureRegion img;

    private float pressedScale = 0.85f;

    public ScaleButton(TextureAtlas atlas, String regionName) {
        this(findRegion(atlas, regionName));
    }

    public ScaleButton(TextureRegion img) {
        this.img = img;

        setTouchable(Touchable.enabled);
        addListener(clickListener = new ClickListener());
    }

    private static TextureRegion findRegion(TextureAtlas atlas, String regionName) {
        TextureRegion region = atlas.findRegion(regionName);
        if (region == null) {
            throw new IllegalArgumentException("Can't find region with name " + regionName + " in atlas " + atlas);
        }
        return region;
    }

    @Override
    public float getPrefWidth() {
        return img.getRegionWidth();
    }

    @Override
    public float getPrefHeight() {
        return img.getRegionHeight();
    }

    public boolean isPressed () {
        return clickListener.isVisualPressed();
    }

    public float getPressedScale() {
        return pressedScale;
    }

    public void setPressedScale(float pressedScale) {
        this.pressedScale = pressedScale;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        float scale = isPressed() ? pressedScale : 1.0f;

        Color tmpColor;
        tmpColor = TMP_COLOR.set(getColor());
        tmpColor.a *= parentAlpha;

        batch.setColor(tmpColor);
        batch.draw(img, getX(), getY(), getWidth()*0.5f, getHeight()*0.5f, getWidth(), getHeight(), scale, scale, 0);
    }
}
