package com.metaphore.bmfmetaedit.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class PatchedTextArea extends TextArea {
    public PatchedTextArea(TextFieldStyle tfStyle) {
        super("", tfStyle);
    }

    @Override
    protected int[] wordUnderCursor (int at) {
        String text = this.text;
        // This small fix refers to this bug https://github.com/libgdx/libgdx/issues/3887
        int start = Math.min(text.length(), at), right = text.length(), left = 0, index = start;
        for (; index < right; index++) {
            if (!isWordCharacter(text.charAt(index))) {
                right = index;
                break;
            }
        }
        for (index = start - 1; index > -1; index--) {
            if (!isWordCharacter(text.charAt(index))) {
                left = index + 1;
                break;
            }
        }
        return new int[] {left, right};
    }
}
