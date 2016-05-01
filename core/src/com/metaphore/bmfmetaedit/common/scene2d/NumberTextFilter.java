package com.metaphore.bmfmetaedit.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.CharArray;

public class NumberTextFilter implements TextField.TextFieldFilter {
    private static final CharArray numbers = CharArray.with('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private final CharArray availableChars = new CharArray();
    private boolean allowFloat = false;
    private int integralLength = 4;
    private int fractionalLength = 2;

    public NumberTextFilter allowFloat() {
        allowFloat = false;
        return this;
    }

    @Override
    public boolean acceptChar(TextField textField, char c) {
        availableChars.clear();
        availableChars.addAll(numbers);

        String text = textField.getText();
        if (text != null && text.length() != 0) {
            int dotIndex = text.indexOf('.');
            if (allowFloat && dotIndex==-1) {
                availableChars.add('.');
            }

            if (dotIndex == -1) {
                // Integral length check
                if (text.length() == integralLength) {
                    availableChars.removeAll(numbers);
                }
            } else {
                // Fractional length check
                if (text.substring(dotIndex).length() == fractionalLength) {
                    availableChars.removeAll(numbers);
                }
            }
        } else {
            availableChars.add('-');
        }

        return availableChars.contains(c);
    }
}
