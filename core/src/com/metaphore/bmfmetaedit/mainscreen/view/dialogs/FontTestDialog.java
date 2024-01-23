package com.metaphore.bmfmetaedit.mainscreen.view.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.common.scene2d.PatchedTextArea;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;
import com.metaphore.bmfmetaedit.mainscreen.view.editbox.BaseDialog;
import com.metaphore.bmfmetaedit.model.FontDocument;

public class FontTestDialog extends BaseDialog {
    private final static float FONT_SCALE_FACTOR = 1.2f;

    private static String lastText = "";
    private static float lastFontScale = 2f;

    private final BitmapFont testFont;
    private final TextArea edtTestArea;
    private boolean disposed = false;

    public FontTestDialog(MainResources resources) {
        super(resources, "Try the font");

        FontDocument fontDocument = App.inst().getModel().getFontDocument();
        BitmapFont.BitmapFontData fontData = fontDocument.getFont().getData();
        testFont = new BitmapFont(fontData, new TextureRegion(fontDocument.getPages().first().getPageTexture()), true);
        testFont.getData().markupEnabled = false;
        testFont.getData().setScale(lastFontScale);

        TextField.TextFieldStyle tfStyle =  new TextField.TextFieldStyle(resources.styles.tfsField);
        tfStyle.font = testFont;
        edtTestArea = new PatchedTextArea(tfStyle);
        defaultFocus(edtTestArea);

        Table content = getContentTable();
        content.add(edtTestArea).minWidth(400).minHeight(200);

        button("Close");
        // Increase font size.
        {
            TextButton btn = new TextButton("+", resources.styles.tbsButton);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    changeFontSize(FONT_SCALE_FACTOR);
                }
            });
            getButtonTable().add(btn);
        }
        // Decrease font size.
        {
            TextButton btn = new TextButton("-", resources.styles.tbsButton);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    changeFontSize(1f/FONT_SCALE_FACTOR);
                }
            });
            getButtonTable().add(btn);
        }

        key(Keys.ESCAPE);

        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                    float scaleFactor = 1f;
                    if (amount > 0) {
                        scaleFactor = 1.1f;
                    } else {
                        scaleFactor = 1f / 1.1f;
                    }
                    BitmapFont.BitmapFontData data = testFont.getData();
                    data.setScale(data.scaleX * scaleFactor);
                    System.out.println("PEWPEW");
                    return true;
                }
                return false;
            }
        });
    }

    private void changeFontSize(float scaleFactor) {
        BitmapFont.BitmapFontData data = testFont.getData();
        data.setScale(data.scaleX * scaleFactor);
        invalidateHierarchy();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            edtTestArea.setText(lastText);
            edtTestArea.setCursorPosition(lastText.length());
            testFont.getData().setScale(lastFontScale);
            stage.setScrollFocus(this);
        } else {
            lastText = edtTestArea.getText();
            lastFontScale = testFont.getData().scaleX;
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
