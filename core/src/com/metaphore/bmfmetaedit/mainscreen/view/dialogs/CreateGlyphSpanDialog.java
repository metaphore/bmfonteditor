package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.common.scene2d.NumericField;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.GlyphUtils;

public class CreateGlyphSpanDialog extends BaseDialog<CreateGlyphSpanDialog.Result> {

    private final NumericField edtCodeFirst, edtCodeLast;
    private final Label lblHexFirst, lblHexLast;
    private final ResultProvider<Result> resultProvider;
    private final Label lblAmountHelper;

    public CreateGlyphSpanDialog(MainResources resources) {
        super(resources, "Create glyphs range");

        edtCodeFirst = new NumericField(resources.styles.tfsField);
        edtCodeFirst.setAlignment(Align.right);
        edtCodeFirst.setInt(0);
        defaultFocus(edtCodeFirst);
        lblHexFirst = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        updateHexValue(edtCodeFirst, lblHexFirst);

        edtCodeLast = new NumericField(resources.styles.tfsField);
        edtCodeLast.setAlignment(Align.right);
        edtCodeLast.setInt(0);
        defaultFocus(edtCodeLast);
        lblHexLast = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        updateHexValue(edtCodeLast, lblHexLast);

        lblAmountHelper = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        updateTotalAmount();

        edtCodeFirst.setTextFieldListener((textField, c) -> {
            updateHexValue(edtCodeFirst, lblHexFirst);
            updateTotalAmount();
        });
        edtCodeLast.setTextFieldListener((textField, c) -> {
            updateHexValue(edtCodeLast, lblHexLast);
            updateTotalAmount();
        });

        Table content = getContentTable();
        content.add(new Label("First code:", resources.styles.lsTitle));
        content.add(edtCodeFirst).width(64f);
        content.add(lblHexFirst).right();
        content.row();
        content.add(new Label("Last code:", resources.styles.lsTitle));
        content.add(edtCodeLast).width(64f);
        content.add(lblHexLast).right();
        content.row();
        content.add(lblAmountHelper).colspan(3);

        resultProvider = new GlyphRangeResultProvider();

        button("Cancel");
        button("Ok", resultProvider);

        key(Input.Keys.ESCAPE);
        key(Input.Keys.ENTER, resultProvider);
    }

    private void updateTotalAmount() {
        int first = edtCodeFirst.getInt();
        int last = edtCodeLast.getInt();
        int amount = Math.max(0, last - first + 1);

        lblAmountHelper.setText(amount + " glyphs will be generated");
    }

    private void updateHexValue(NumericField numericField, Label label) {
        label.setText("U+" + GlyphUtils.hexCode(numericField.getInt()));
    }

    private class GlyphRangeResultProvider implements ResultProvider<Result> {
        @Override
        public Result generateResult() {
            int first = edtCodeFirst.getInt();
            int last = edtCodeLast.getInt();
            int amount = last - first + 1;
            if (amount < 1) {
                return null;
            }
            return new Result(first, last);
        }
    }

    public static class Result {
        public final int first;
        public final int last;

        public Result(int first, int last) {
            this.first = first;
            this.last = last;
        }
    }
}
