package com.metaphore.bmfmetaedit.mainscreen.view.editbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.crashinvaders.common.eventmanager.EventHandler;
import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.common.scene2d.NumberField;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.mainscreen.selection.events.GlyphSelectionChangedEvent;
import com.metaphore.bmfmetaedit.model.GlyphModel;

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
            ctx.getEvents().addHandler(this, GlyphSelectionChangedEvent.class);
        } else {
            ctx.getEvents().removeHandler(this);
        }
    }

    @Override
    public void handle(EventInfo event) {
        if (event instanceof GlyphSelectionChangedEvent) {
            GlyphSelectionChangedEvent e = (GlyphSelectionChangedEvent) event;
            content.setGlyphModel(e.getSelectedGlyph());
        }
    }

    private class Content extends Table {
        private static final float SIDE_PAD = 16f;
        public static final float PAD_LBL = 4f;
        public static final float SPACE_COL = 16f;
        public static final float SPACE_ROW = 8f;

        private final Label.LabelStyle lsTitle;
        private final TextField.TextFieldStyle tfsField;
        private final Label lblUnicode;
        private final NumberField edtCode, edtX, edtY, edtWidth, edtHeight, edtXOff, edtYOff, edtXAdv;

        private GlyphModel glyphModel;

        public Content(MainScreenContext ctx) {
            lsTitle = new Label.LabelStyle(ctx.getResources().font, Color.WHITE);
            tfsField = new TextField.TextFieldStyle(ctx.getResources().font, Color.WHITE,
                    new NinePatchDrawable(ctx.getResources().atlas.createPatch("text_cursor")),
                    new TextureRegionDrawable(ctx.getResources().atlas.findRegion("text_selection")),
                    new NinePatchDrawable(ctx.getResources().atlas.createPatch("input_field_bg")));

            padTop(SIDE_PAD);
            padLeft(SIDE_PAD);
            padRight(SIDE_PAD);

            setBackground(new NinePatchDrawable(ctx.getResources().atlas.createPatch("edit_panel_bg")));

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
        }

        private Cell<Label> addLabelCell(String text) {
            Label label = new Label(text, lsTitle);
            return add(label).right();
        }

        private Cell<NumberField> addInputCell() {
            NumberField numberField = new NumberField(tfsField);
            numberField.setAlignment(Align.right);
            return add(numberField).width(64f);
        }

        private void mapFromModel(GlyphModel model) {
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

        public void setGlyphModel(GlyphModel glyphModel) {
            if (this.glyphModel == glyphModel) return;

            this.glyphModel = glyphModel;

            if (glyphModel != null) {
                mapFromModel(glyphModel);
            }
        }
    }

}
