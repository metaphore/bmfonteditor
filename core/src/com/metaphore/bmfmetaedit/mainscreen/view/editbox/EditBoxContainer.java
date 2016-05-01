package com.metaphore.bmfmetaedit.mainscreen.view.editbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.crashinvaders.common.scene2d.Scene2dUtils;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.NumericField;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;
import com.metaphore.bmfmetaedit.model.events.GlyphModelChangedEvent;

public class EditBoxContainer extends Container implements EventHandler {
    private final MainScreenContext ctx;
    private final Content content;

    public EditBoxContainer(MainScreenContext ctx) {
        this.ctx = ctx;
        setActor(content = new Content(ctx));
        bottom();
        setFillParent(true);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            ctx.getEvents().addHandler(this,
                    GlyphSelectionChangedEvent.class,
                    GlyphModelChangedEvent.class);
        } else {
            ctx.getEvents().removeHandler(this);
        }
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof GlyphSelectionChangedEvent) {
            GlyphSelectionChangedEvent e = (GlyphSelectionChangedEvent) event;
            content.setGlyphModel(e.getSelectedGlyph());

        } else if (event instanceof GlyphModelChangedEvent) {
            GlyphModelChangedEvent e = (GlyphModelChangedEvent) event;
            GlyphModel glyphModel = e.getGlyphModel();
            if (glyphModel == content.getGlyphModel()) {
                switch (e.getType()) {
                    case UPDATED:
                        content.mapFromModel();
                        break;
                    case REMOVED:
                        content.setGlyphModel(null);
                        break;
                }
            }
        }
    }

    private class Content extends Table {
        private static final float SIDE_PAD_H = 16f;
        private static final float SIDE_PAD_V = 8f;
        public static final float PAD_LBL = 8f;
        public static final float SPACE_COL = 16f;
        public static final float SPACE_ROW = 8f;

        // Styles
        private final Label.LabelStyle lsTitle;
        private final TextField.TextFieldStyle tfsField;
        private final TextButton.TextButtonStyle tbsButton;

        // Widgets
        private final Label lblUnicode;
        private final NumericField edtCode, edtX, edtY, edtWidth, edtHeight, edtXOff, edtYOff, edtXAdv;

        private GlyphModel glyphModel;

        public Content(MainScreenContext ctx) {
            TextureAtlas atlas = ctx.getResources().atlas;

            lsTitle = new Label.LabelStyle(ctx.getResources().font, Color.WHITE);
            tfsField = new TextField.TextFieldStyle(ctx.getResources().font, Color.WHITE,
                    new NinePatchDrawable(atlas.createPatch("text_cursor")),
                    new TextureRegionDrawable(atlas.findRegion("text_selection")),
                    new NinePatchDrawable(atlas.createPatch("input_field_bg")));
            tbsButton = new TextButton.TextButtonStyle(
                    new NinePatchDrawable(atlas.createPatch("btn_up")),
                    new NinePatchDrawable(atlas.createPatch("btn_down")),
                    null, ctx.getResources().font);

            pad(SIDE_PAD_V, SIDE_PAD_H, SIDE_PAD_V, SIDE_PAD_H);

            setBackground(new NinePatchDrawable(atlas.createPatch("edit_panel_bg")));

            TextButton bntSave = new TextButton("Save", tbsButton);
            bntSave.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (glyphModel != null) {
                        mapToModel();
                        App.inst().getModel().getFontDocument().saveGlyphData(glyphModel);
                    }
                }
            });

            addLabelCell("Code").padRight(PAD_LBL);
            edtCode = addInputCell().padRight(SPACE_COL).getActor();
            add();
            lblUnicode = addLabelCell("").left().getActor();

            row().padTop(SPACE_ROW);
            addLabelCell("X").padRight(PAD_LBL);
            edtX = addInputCell().padRight(SPACE_COL).getActor();
            addLabelCell("Y").padRight(PAD_LBL);
            edtY = addInputCell().getActor();

            row().padTop(SPACE_ROW);
            addLabelCell("Width").padRight(PAD_LBL);
            edtWidth = addInputCell().padRight(SPACE_COL).getActor();
            addLabelCell("Height").padRight(PAD_LBL);
            edtHeight = addInputCell().getActor();

            row().padTop(SPACE_ROW);
            addLabelCell("XOff").padRight(PAD_LBL);
            edtXOff = addInputCell().padRight(SPACE_COL).getActor();
            addLabelCell("YOff").padRight(PAD_LBL);
            edtYOff = addInputCell().getActor();

            row().padTop(SPACE_ROW);
            addLabelCell("XAdv").padRight(PAD_LBL);
            edtXAdv = addInputCell().padRight(SPACE_COL).getActor();
            add(bntSave).colspan(2).expandX().fill();

            // Save shortcut
            addListener(new InputListener() {
                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    switch (keycode) {
                        case Input.Keys.SPACE: {
                            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                                Scene2dUtils.simulateClick(bntSave);
                                event.handle();
                            }
                            break;
                        }
                    }
                    return super.keyDown(event, keycode);
                }
            });
        }

        private Cell<Label> addLabelCell(String text) {
            Label label = new Label(text, lsTitle);
            return add(label).right();
        }

        private Cell<NumericField> addInputCell() {
            NumericField numericField = new NumericField(tfsField);
            numericField.setAlignment(Align.right);
            return add(numericField).width(64f);
        }

        private void mapFromModel() {
            GlyphModel model = glyphModel;

            lblUnicode.setText("U+" + model.hex);
            edtCode.setInt(model.code);
            edtX.setInt(model.x);
            edtY.setInt(model.y);
            edtWidth.setInt(model.width);
            edtHeight.setInt(model.height);
            edtXOff.setInt(model.xoffset);
            edtYOff.setInt(model.yoffset);
            edtXAdv.setInt(model.xadvance);
        }

        private void mapToModel() {
            GlyphModel model = glyphModel;

            model.code = edtCode.getInt();
            model.x = edtX.getInt();
            model.y = edtY.getInt();
            model.width = edtWidth.getInt();
            model.height = edtHeight.getInt();
            model.xoffset = edtXOff.getInt();
            model.yoffset = edtYOff.getInt();
            model.xadvance = edtXAdv.getInt();
        }

        public void setGlyphModel(GlyphModel glyphModel) {
            if (this.glyphModel == glyphModel) return;

            this.glyphModel = glyphModel;

            if (glyphModel != null) {
                mapFromModel();
            }
        }

        public GlyphModel getGlyphModel() {
            return glyphModel;
        }
    }

}
