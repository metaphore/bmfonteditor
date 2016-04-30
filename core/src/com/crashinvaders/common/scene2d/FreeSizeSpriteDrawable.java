package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * SpriteDrawable that can be shrunk to zero size (regardless to sprite dimens).
 */
public class FreeSizeSpriteDrawable extends SpriteDrawable {

    public FreeSizeSpriteDrawable() {
    }

    public FreeSizeSpriteDrawable(Sprite sprite) {
        super(sprite);
    }

    public FreeSizeSpriteDrawable(SpriteDrawable drawable) {
        super(drawable);
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        setMinWidth(0f);
        setMinHeight(0f);
    }
}
