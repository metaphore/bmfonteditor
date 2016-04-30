package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.File;

public class FontDocument {
    private final BitmapFont font;

    public FontDocument(BitmapFont font) {
        this.font = font;
    }

    public void dispose() {
        font.dispose();
    }

    public BitmapFont getFont() {
        return font;
    }

    //region Factory methods
    public static FontDocument createFromFont(File file) {
//        InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
//        BitmapFont.BitmapFontData data = new BitmapFont.BitmapFontData(Gdx.files.internal("test/nokia8.fnt"), false);
        FileHandle fileHandle = Gdx.files.absolute(file.getAbsolutePath());
        BitmapFont.BitmapFontData data = new BitmapFont.BitmapFontData(fileHandle, false);
        int pages = data.getImagePaths().length;

        Array<Texture> textures = new Array<>(true, pages);
        for (int i = 0; i < data.getImagePaths().length; i++) {
            String path = data.getImagePath(i);
            Texture texture = new Texture(Gdx.files.absolute(path));
            textures.add(texture);
        }

        Array<TextureRegion> regs = new Array<>(pages);
        for (int i = 0; i < pages; i++) {
            regs.add(new TextureRegion(textures.get(i)));
        }
        BitmapFont bitmapFont = new BitmapFont(data, regs, true);
        bitmapFont.setOwnsTexture(true);

        return new FontDocument(bitmapFont);
    }
    //endregion
}
