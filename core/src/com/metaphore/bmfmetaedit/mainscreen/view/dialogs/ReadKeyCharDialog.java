package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;

public class ReadKeyCharDialog extends BaseDialog<Integer> {

    public ReadKeyCharDialog(MainResources resources) {
        super(resources, "Key char reader");

        Label lblHint = new Label("Type any key and this\ndialog will be closed", resources.styles.lsTitle);
        lblHint.setAlignment(Align.center);

        Table content = getContentTable();
        content.add(lblHint);

        addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (character == 0) return false;

                result((int)character);
                hide();
                return true;
            }
        });

        button("Cancel");
        key(Keys.ESCAPE);
    }
}
