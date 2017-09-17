package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.common.scene2d.NumericField;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.GlyphUtils;

public class CreateGlyphSpanDialog extends BaseDialog<CreateGlyphSpanDialog.Result> {

    private final NumericField edtDecFirst, edtDecLast;
    private final TextField edtHexFirst, edtHexLast;
    private final Label lblHexFirst, lblHexLast;
    private final ResultProvider<Result> resultProvider;
    private final Label lblAmountHelper;

    public CreateGlyphSpanDialog(MainResources resources) {
        super(resources, "Create glyphs range");

        edtDecFirst = new NumericField(resources.styles.tfsField);
        edtDecFirst.setAlignment(Align.right);
        edtDecFirst.setInt(0);
        edtDecFirst.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateHexValue(edtDecFirst, edtHexFirst);
                updateHexCodeLabel(edtDecFirst, lblHexFirst);
                updateTotalAmount();
            }
        });
        lblHexFirst = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        edtHexFirst = new TextField("0", resources.styles.tfsField);
        edtHexFirst.setAlignment(Align.right);
        edtHexFirst.setMaxLength(4);
        edtHexFirst.setTextFieldFilter((textField, c) -> GlyphUtils.hexChars.contains(c));
        edtHexFirst.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateDecValue(edtHexFirst, edtDecFirst);
                updateHexCodeLabel(edtDecFirst, lblHexFirst);
                updateTotalAmount();
            }
        });
        // Select all text on focus
        edtHexFirst.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                ((TextField) event.getListenerActor()).selectAll();
            }
        });

        edtDecLast = new NumericField(resources.styles.tfsField);
        edtDecLast.setAlignment(Align.right);
        edtDecLast.setInt(0);
        edtDecLast.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateHexValue(edtDecLast, edtHexLast);
                updateHexCodeLabel(edtDecLast, lblHexLast);
                updateTotalAmount();
            }
        });
        lblHexLast = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));
        edtHexLast = new TextField("0", resources.styles.tfsField);
        edtHexLast.setAlignment(Align.right);
        edtHexLast.setMaxLength(4);
        edtHexLast.setTextFieldFilter((textField, c) -> GlyphUtils.hexChars.contains(c));
        edtHexLast.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateDecValue(edtHexLast, edtDecLast);
                updateHexCodeLabel(edtDecLast, lblHexLast);
                updateTotalAmount();
            }
        });
        // Select all text on focus
        edtHexLast.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                ((TextField) event.getListenerActor()).selectAll();
            }
        });

        lblAmountHelper = new Label("", new Label.LabelStyle(resources.font, Color.LIGHT_GRAY));

//        edtDecFirst.setTextFieldListener((textField, c) -> {
//            updateHexValue(edtDecFirst, lblHexFirst);
//            updateTotalAmount();
//        });
//        edtDecLast.setTextFieldListener((textField, c) -> {
//            updateHexValue(edtDecLast, lblHexLast);
//            updateTotalAmount();
//        });

        Table content = getContentTable();
        content.add(new Label("First code:", resources.styles.lsTitle));
        content.add(edtDecFirst).width(64f);
        content.add(edtHexFirst).width(64f);
        content.add(lblHexFirst).right();
        content.row();
        content.add(new Label("Last code:", resources.styles.lsTitle));
        content.add(edtDecLast).width(64f);
        content.add(edtHexLast).width(64f);
        content.add(lblHexLast).right();
        content.row();
        content.add(lblAmountHelper).colspan(3);

        resultProvider = new GlyphRangeResultProvider();

        button("Cancel");
        button("Ok", resultProvider);

        key(Input.Keys.ESCAPE);
        key(Input.Keys.ENTER, resultProvider);

        updateHexValue(edtDecFirst, edtHexFirst);
        updateHexValue(edtDecLast, edtHexLast);
        updateHexCodeLabel(edtDecFirst, lblHexFirst);
        updateHexCodeLabel(edtDecLast, lblHexLast);
        updateTotalAmount();
        defaultFocus(edtDecFirst);
    }

    private void updateDecValue(TextField src, NumericField dst) {
        dst.setProgrammaticChangeEvents(false);
        try {
            int decCode = Integer.parseInt(src.getText(), 16);
            dst.setInt(decCode);
        } catch (NumberFormatException ignored) { }
        dst.setProgrammaticChangeEvents(true);
    }

    private void updateHexValue(NumericField src, TextField dst) {
        dst.setProgrammaticChangeEvents(false);
        dst.setText(Integer.toHexString(src.getInt()).toUpperCase());
        dst.setProgrammaticChangeEvents(true);
    }

    private void updateHexCodeLabel(NumericField src, Label dst) {
        dst.setText("U+" + GlyphUtils.hexCode(src.getInt()));
    }

    private void updateTotalAmount() {
        int first = edtDecFirst.getInt();
        int last = edtDecLast.getInt();
        int amount = Math.max(0, last - first + 1);

        lblAmountHelper.setText(amount + " glyphs will be generated");
    }

//    private void updateHexValue(NumericField numericField, Label label) {
//        label.setText("U+" + GlyphUtils.hexCode(numericField.getInt()));
//    }

    private class GlyphRangeResultProvider implements ResultProvider<Result> {
        @Override
        public Result generateResult() {
            int first = edtDecFirst.getInt();
            int last = edtDecLast.getInt();
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
