package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.common.scene2d.NumericField;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.GlyphUtils;

public class CreateGlyphDialog extends BaseDialog<CreateGlyphDialog.Result> {

    private final NumericField edtDec;
    private final TextField edtHex;
    private final Label lblHex;

    public CreateGlyphDialog(MainResources resources) {
        super(resources, "New glyph");

        edtDec = new NumericField(resources.styles.tfsField);
        edtDec.setAlignment(Align.right);
        edtDec.setInt(0);
        edtDec.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateHexValue();
                updateHexCodeLabel();
            }
        });

        edtHex = new TextField("0", resources.styles.tfsField);
        edtHex.setAlignment(Align.right);
        edtHex.setMaxLength(4);
        edtHex.setTextFieldFilter((textField, c) -> GlyphUtils.hexChars.contains(c));
        edtHex.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateDecValue();
                updateHexCodeLabel();
            }
        });
        // Select all text on focus
        edtHex.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                ((TextField) event.getListenerActor()).selectAll();
            }
        });

        lblHex = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));

        TextButton btnReadKeyCode = new TextButton("Read key", resources.styles.tbsButton);
        btnReadKeyCode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new ReadKeyCharDialog(resources).onResult(edtDec::setInt).show(getStage());
            }
        });

        Table firstRow = new Table();
        firstRow.add(new Label("Code (dec/hex)", resources.styles.lsTitle)).padRight(4);
        firstRow.add(edtDec).width(64f).padRight(4);
        firstRow.add(edtHex).width(64f);

        Table secondRow = new Table();
        secondRow.add(lblHex).padRight(4);
        secondRow.add(btnReadKeyCode);

        Table content = getContentTable();
        content.add(firstRow);
        content.row();
        content.add(secondRow);

        button("Cancel");
        button("Ok", ()-> new Result(edtDec.getInt()));

        key(Input.Keys.ESCAPE);
        key(Input.Keys.ENTER, ()-> new Result(edtDec.getInt()));

        updateHexValue();
        updateDecValue();
        defaultFocus(edtDec);
    }

    private void updateHexValue() {
        edtHex.setProgrammaticChangeEvents(false);
        edtHex.setText(Integer.toHexString(edtDec.getInt()).toUpperCase());
        edtHex.setProgrammaticChangeEvents(true);
    }

    private void updateDecValue() {
        edtDec.setProgrammaticChangeEvents(false);
        try {
            int decCode = Integer.parseInt(edtHex.getText(), 16);
            edtDec.setInt(decCode);
        } catch (NumberFormatException ignored) { }
        edtDec.setProgrammaticChangeEvents(true);
    }

    private void updateHexCodeLabel() {
        lblHex.setText("U+" + GlyphUtils.hexCode(edtDec.getInt()));
    }

    public static class Result {
        public final int code;

        public Result(int code) {
            this.code = code;
        }
    }
}
