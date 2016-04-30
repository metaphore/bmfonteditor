package com.metaphore.bmfmetaedit.mainscreen.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Pools;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.model.GlyphModel;

public class GlyphItem extends Table {

    private final BgDrawable bgDrawable;
    private final ClickListener clickListener;

    public GlyphItem(MainResources resources, GlyphModel model) {
        setBackground(bgDrawable = new BgDrawable(resources.atlas));
        addListener(clickListener = new ClickListener());
        setTouchable(Touchable.enabled);

        String hexValue = Integer.toHexString(model.code).toUpperCase();
        while (hexValue.length() < 4) {
            hexValue = 0 + hexValue;
        }

        Label lblHex = new Label("U+"+hexValue, new Label.LabelStyle(resources.font, Color.WHITE));
        Label lblDec = new Label("#"+model.code, new Label.LabelStyle(resources.font, Color.WHITE));
        GlyphPreview glyphPreview = new GlyphPreview(resources, model);
        Container previewContainer = new Container<>(glyphPreview);
        previewContainer.setBackground(new NinePatchDrawable(resources.atlas.createPatch("glyph_preview_border")));
        previewContainer.pad(1f);

        pad(8f);
        defaults().center();
        add(previewContainer);
        row().padTop(2f);
        add(lblHex);
        row();
        add(lblDec);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (clickListener.isOver()) {
            bgDrawable.setState(BgDrawable.State.HOVER);
        } else {
            bgDrawable.setState(BgDrawable.State.REGULAR);
        }

        super.draw(batch, parentAlpha);
    }

    private static class GlyphPreview extends Widget {
        public static final float GLYPH_SCALE = 8f;

        private final GlyphModel model;
        private final BitmapFont font;
        private final BitmapFont.Glyph glyph;
//        private final Texture background;
        private TextureRegion glyphRegion;

        public GlyphPreview(MainResources resources, GlyphModel model) {
            this.model = model;
            font = App.inst().getModel().getFontDocument().getFont();
            glyph = font.getData().getGlyph((char) model.code);

//            background = resources.assets.get("textures/glyph_preview_bg_checker.png");
//            background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//            background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            init();
        }

        @Override
        public float getPrefWidth() {
            if (glyphRegion == null) return super.getPrefWidth();
            return GLYPH_SCALE * glyphRegion.getRegionWidth();
        }

        @Override
        public float getPrefHeight() {
            if (glyphRegion == null) return super.getPrefHeight();
            return GLYPH_SCALE * glyphRegion.getRegionHeight();
        }

        @Override
        protected void setStage(Stage stage) {
            super.setStage(stage);
            if (stage != null) {
                init();
            } else {
//                dispose();
            }
        }

        private void init() {
            if (glyphRegion != null) return;

            glyphRegion = Pools.obtain(TextureRegion.class);
            TextureRegion pageRegion = font.getRegion(glyph.page);
            glyphRegion.setRegion(pageRegion, glyph.srcX, glyph.srcY, glyph.width, glyph.height);

            invalidate();
        }

        private void dispose() {
            if (glyphRegion != null) {
                Pools.free(glyphRegion);
                glyphRegion = null;
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(getColor());
//            batch.draw(background, getX(), getY(), getWidth(), getHeight(), 0, 0, glyphRegion.getRegionWidth()/2f, glyphRegion.getRegionHeight()/2f);

            if (glyphRegion != null) {
                batch.draw(glyphRegion, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    private static class BgDrawable extends BaseDrawable {

        private final NinePatch regular;
        private final NinePatch hover;
        private State state = State.REGULAR;

        public BgDrawable(TextureAtlas atlas) {
            regular = atlas.createPatch("glyph_li_bg");
            hover = atlas.createPatch("glyph_li_bg_hover");
        }

        public void setState(State state) {
            this.state = state;
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            NinePatch patch;
            switch (state) {
                case HOVER:
                    patch = hover;
                    break;
                case REGULAR:
                default:
                    patch = regular;
            }
            patch.draw(batch, x, y, width, height);
        }

        public enum State {
            REGULAR, HOVER
        }
    }
}
