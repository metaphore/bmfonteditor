package com.metaphore.bmfmetaedit.mainscreen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainResources {
    public final AssetManager assets;
    public final TextureAtlas atlas;
    public final BitmapFont font;
    public final Texture whitePixel;
    public final ShapeRenderer shapeRenderer;
    public final Styles styles;

    public MainResources(AssetManager assets) {
        this.assets = assets;
        atlas = assets.get("atlases/main_screen.atlas");
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        styles = new Styles();

        // White pixel texture
        {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            pixmap.drawPixel(0, 0, 0xffffffff);
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public void dispose() {
        font.dispose();
        whitePixel.dispose();
        shapeRenderer.dispose();
    }

    public class Styles {
        public final Window.WindowStyle wsDialog;
        public final Label.LabelStyle lsTitle;
        public final TextField.TextFieldStyle tfsField;
        public final TextButton.TextButtonStyle tbsButton;

        Styles() {
            wsDialog = new Window.WindowStyle(font, Color.LIGHT_GRAY, new NinePatchDrawable(atlas.createPatch("dialog_bg")));
            wsDialog.stageBackground = new TextureRegionDrawable(atlas.findRegion("dialog_dim"));

            lsTitle = new Label.LabelStyle(font, Color.WHITE);

            tfsField = new TextField.TextFieldStyle(font, Color.WHITE,
                    new NinePatchDrawable(atlas.createPatch("text_cursor")),
                    new TextureRegionDrawable(atlas.findRegion("text_selection")),
                    new NinePatchDrawable(atlas.createPatch("input_field_bg")));

            tbsButton = new TextButton.TextButtonStyle(
                    new NinePatchDrawable(atlas.createPatch("btn_up")),
                    new NinePatchDrawable(atlas.createPatch("btn_down")),
                    null, font);


        }
    }
}
