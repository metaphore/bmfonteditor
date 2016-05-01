package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

        Table content = getContentTable();
        content.add(new Label("Code:", resources.styles.lsTitle)).padRight(8f);
        content.add(edtCode).width(64f);
        content.row();
        content.add();
        content.add(lblHex).right();

//        content.row();
//        TextButton btnTest = new TextButton("Pick from unicode", resources.styles.tbsButton);
//        btnTest.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                new CreateGlyphDialog(resources)
//                        .onResult(r -> edtCode.setInt(r.code))
//                        .show(getStage());
//            }
//        });
//        content.add(btnTest).colspan(2);

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
