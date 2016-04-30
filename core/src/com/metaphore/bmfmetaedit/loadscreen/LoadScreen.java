package com.metaphore.bmfmetaedit.loadscreen;
/* Metaphore 2/7/14 7:21 PM */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.screenmanager.BaseScreen;
import com.crashinvaders.screenmanager.Bundle;

public class LoadScreen extends BaseScreen {

    private final Listener listener;
    private final InternalDirFileHandleResolver fileHandleResolver;
    private final AssetManager assetManager;
    private final LoaderBar loaderBar;

    public LoadScreen(Listener listener) {
        this.listener = listener;

        fileHandleResolver = new InternalDirFileHandleResolver("res");
        assetManager = new AssetManager(fileHandleResolver);
        initializeFileHandles();

        loaderBar = new LoaderBar();
    }

    private void initializeFileHandles() {
        for (FileHandle fileHandle : fileHandleResolver.resolve("textures").list(".png")) {
            assetManager.load(fileHandleResolver.unresolve(fileHandle), Texture.class);
        }
        for (FileHandle fileHandle : fileHandleResolver.resolve("atlases").list(".atlas")) {
            assetManager.load(fileHandleResolver.unresolve(fileHandle), TextureAtlas.class);
        }
        for (FileHandle fileHandle : fileHandleResolver.resolve("fonts").list(".fnt")) {
            assetManager.load(fileHandleResolver.unresolve(fileHandle), BitmapFont.class);
        }
    }

    @Override
    public void show(Bundle bundle) {
        super.show(bundle);
        Gdx.gl20.glClearColor(0,0,0,1);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        loaderBar.resize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        loaderBar.render(assetManager.getProgress());

        if (assetManager.update()) {
            setupFonts();
            listener.onLoadComplete(assetManager);
        }
    }

    @Override
    public void dispose() {
        loaderBar.dispose();
    }

    private void setupFonts() {
        Array<BitmapFont> fonts = new Array<>(8);
        assetManager.getAll(BitmapFont.class, fonts);

        for (BitmapFont font : fonts) {
            // Enable markup
            font.getData().markupEnabled = true;

            // Add spacing to font if it has "spacing" in name
            if (font.getData().fontFile.name().contains("spacing")) {
                for (BitmapFont.Glyph[] subGlyphs : font.getData().glyphs) {
                    if (subGlyphs == null) continue;
                    for (BitmapFont.Glyph glyph : subGlyphs) {
                        if (glyph == null) continue;
                        glyph.xadvance *= 1.2f;
                    }
                }
            }
        }
    }

    public interface Listener {
        void onLoadComplete(AssetManager assets);
    }

}