package com.metaphore.bmfmetaedit.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

public class NumberField extends TextField {
    private final NumberTextFilter filter;

    public NumberField(TextFieldStyle style) {
        super("", style);
        filter = new NumberTextFilter();
        setTextFieldFilter(filter);

        // Select all text on focus
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (actor == NumberField.this) {
                    selectAll();
                }
            }
        });
    }

    public NumberField allowFloat() {
        filter.allowFloat();
        return this;
    }

    public void setInt(int value) {
        setText(value+"");
    }

    public int getInt() {
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
//            e.printStackTrace();
            return 0;
        }
    }
}
