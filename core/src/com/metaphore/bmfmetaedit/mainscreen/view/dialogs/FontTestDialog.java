package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.PatchedTextArea;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.FontDocument;

public class FontTestDialog extends BaseDialog {
    private static String lastText = "";

    private final BitmapFont testFont;
    private final TextArea edtTestArea;
    private boolean disposed = false;

    public FontTestDialog(MainResources resources) {
        super(resources, "Try current font");

        FontDocument fontDocument = App.inst().getModel().getFontDocument();
        BitmapFont.BitmapFontData fontData = fontDocument.getFont().getData();
        testFont = new BitmapFont(fontData, new TextureRegion(fontDocument.getPages().first().getPageTexture()), true);
        testFont.getData().markupEnabled = true;
        testFont.getData().setScale(2f);

        TextField.TextFieldStyle tfStyle =  new TextField.TextFieldStyle(resources.styles.tfsField);
        tfStyle.font = testFont;
        edtTestArea = new PatchedTextArea(tfStyle);
        defaultFocus(edtTestArea);

        Table content = getContentTable();
        content.add(edtTestArea).minWidth(400).minHeight(200);

        button("Close");
        key(Keys.ESCAPE);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            edtTestArea.setText(lastText);
            edtTestArea.setCursorPosition(lastText.length());
        } else {
            lastText = edtTestArea.getText();
        }
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        dispose();
    }
    @Override
    public void cancel() {
        super.cancel();
        dispose();
    }

    private void dispose() {
        if (disposed) throw new IllegalStateException("Resources already been disposed");

        disposed = true;
        testFont.dispose();
    }

}
