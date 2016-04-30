package com.metaphore.bmfmetaedit.mainscreen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainResources {
    public final AssetManager assets;
    public final TextureAtlas atlas;
    public final BitmapFont font;
    public final Texture whitePixel;
    public final ShapeRenderer shapeRenderer;

    public MainResources(AssetManager assets) {
        this.assets = assets;
        atlas = assets.get("atlases/main_screen.atlas");
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

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
}
