package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.common.scene2d.NumericField;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.GlyphUtils;

public class CreateGlyphDialog extends BaseDialog<CreateGlyphDialog.Result> {

    private final NumericField edtCode;
    private final Label lblHex;

    public CreateGlyphDialog(MainResources resources) {
        super(resources, "New glyph");

        edtCode = new NumericField(resources.styles.tfsField);
        edtCode.setAlignment(Align.right);
        edtCode.setInt(0);
        defaultFocus(edtCode);

        lblHex = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        edtCode.setTextFieldListener((textField, c) -> updateHexValue());
        updateHexValue();

        TextButton btnReadKeyCode = new TextButton("Read key", resources.styles.tbsButton);
        btnReadKeyCode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new ReadKeyCharDialog(resources).onResult(edtCode::setInt).show(getStage());
            }
        });

        Table content = getContentTable();
        content.add(new Label("Code", resources.styles.lsTitle));
        content.add(edtCode).width(64f);
        content.add(lblHex);
        content.add(btnReadKeyCode);

        button("Cancel");
        button("Ok", ()-> new Result(edtCode.getInt()));

        key(Input.Keys.ESCAPE);
        key(Input.Keys.ENTER, ()-> new Result(edtCode.getInt()));
    }

    private void updateHexValue() {
        lblHex.setText("U+" + GlyphUtils.hexCode(edtCode.getInt()));
    }

    public static class Result {
        public final int code;

        public Result(int code) {
            this.code = code;
        }
    }
}
