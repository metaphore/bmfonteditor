package com.metaphore.bmfmetaedit.mainscreen.view.glyphgrid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.model.FontDocument;
import com.metaphore.bmfmetaedit.model.GlyphModel;

class GlyphItem extends Table {

    private final BgDrawable bgDrawable;
    private final ClickListener clickListener;
    private final GlyphModel model;

    private final Label lblHex;
    private final Label lblDec;
    private final GlyphPreview glyphPreview;

    private boolean selected;

    public GlyphItem(MainResources resources, FontDocument fontDocument, GlyphModel model) {
        this.model = model;
        setBackground(bgDrawable = new BgDrawable(resources.atlas));
        addListener(clickListener = new ClickListener());
        setTouchable(Touchable.enabled);

        lblHex = new Label("", new Label.LabelStyle(resources.font, Color.WHITE));
        lblDec = new Label("", new Label.LabelStyle(resources.font, Color.WHITE));
        glyphPreview = new GlyphPreview(resources, fontDocument, model);
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

        mapFromModel();
    }

    public void mapFromModel() {
        lblHex.setText(model.hex);
        lblDec.setText("#"+model.code);
        glyphPreview.updateTextureRegion();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (selected) {
            bgDrawable.setState(BgDrawable.State.SELECTED);
        } else if (clickListener.isOver()) {
            bgDrawable.setState(BgDrawable.State.HOVER);
        } else {
            bgDrawable.setState(BgDrawable.State.REGULAR);
        }

        super.draw(batch, parentAlpha);
    }

    public GlyphModel getModel() {
        return model;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private static class GlyphPreview extends Widget {
        public static final float GLYPH_SCALE = 8f;

        private final FontDocument fontDocument;
        private final GlyphModel model;
        private final TextureRegion glyphRegion = new TextureRegion();

        public GlyphPreview(MainResources resources, FontDocument fontDocument, GlyphModel model) {
            this.fontDocument = fontDocument;
            this.model = model;

            updateTextureRegion();
        }

        @Override
        public float getPrefWidth() {
            if (glyphRegion.getTexture() == null) return super.getPrefWidth();
            return GLYPH_SCALE * glyphRegion.getRegionWidth();
        }

        @Override
        public float getPrefHeight() {
            if (glyphRegion.getTexture() == null) return super.getPrefHeight();
            return GLYPH_SCALE * glyphRegion.getRegionHeight();
        }

        public void updateTextureRegion() {
            glyphRegion.setTexture(fontDocument.getPages().first().getPageTexture());
            glyphRegion.setRegion(model.x, model.y, model.width, model.height);

            invalidateHierarchy();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(getColor());
            if (glyphRegion.getTexture() != null) {
                batch.draw(glyphRegion, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    private static class BgDrawable extends BaseDrawable {

        private final NinePatch regular, hover, selected;
        private State state = State.REGULAR;

        public BgDrawable(TextureAtlas atlas) {
            regular = atlas.createPatch("glyph_li_bg");
            hover = atlas.createPatch("glyph_li_bg_hover");
            selected = atlas.createPatch("glyph_li_bg_selected");
        }

        public void setState(State state) {
            this.state = state;
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            NinePatch patch;
            switch (state) {
                case SELECTED:
                    patch = selected;
                    break;
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
            REGULAR, HOVER, SELECTED
        }
    }
}
