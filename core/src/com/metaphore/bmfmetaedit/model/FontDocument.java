package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.model.events.GlyphDataChangedEvent;

import java.io.File;

public class FontDocument {
    public static final int CHUNK_SIZE = 512;
    private final BitmapFont font;
    private final Array<GlyphModel> glyphs;
    private EventManager eventManager;

    public FontDocument(BitmapFont font) {
        this.font = font;

        glyphs = new Array<>(1024);
        for (BitmapFont.Glyph[] glyphChunk : font.getData().glyphs) {
            if (glyphChunk == null) continue;
            for (BitmapFont.Glyph glyph : glyphChunk) {
                if (glyph == null) continue;

                GlyphModel glyphModel = GlyphUtils.toGlyphModel(new GlyphModel(glyph), glyph);
                glyphs.add(glyphModel);
            }
        }
    }

    void dispose() {
        font.dispose();
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Array<GlyphModel> getGlyphs() {
        return glyphs;
    }

    public void saveGlyphData(GlyphModel glyphModel) {
        int code = glyphModel.glyph.id;

        int glyphChunkIdx = code / CHUNK_SIZE;
        int glyphIdx = code % CHUNK_SIZE;

        BitmapFont.Glyph[] glyphChunk = font.getData().glyphs[glyphChunkIdx];
        if (glyphChunk == null) {
            throw new IllegalStateException("Can't find glyph chunk for code: " + code);
        }

        BitmapFont.Glyph glyph = glyphChunk[glyphIdx];
        if (glyph == null) {
            throw new IllegalStateException("Can't find glyph with code: " + code);
        }

        if (glyph != glyphModel.glyph) {
            throw new IllegalStateException("Glyphs do not match");
        }

        if (glyphModel.code != glyph.id) {
            int newCode = glyphModel.code;
            int newChunkIdx = newCode / CHUNK_SIZE;
            int newGlyphIdx = newCode % CHUNK_SIZE;

            BitmapFont.Glyph[] newChunk = ensureChunkExists(newChunkIdx);
            if (newChunk[newChunkIdx] != null) {
                Gdx.app.log("FontDocument", "This char code is occupied: " + newCode);
                return;
            }

            // Move our glyph to new place
            glyph.id = newCode;
            glyphChunk[glyphIdx] = null;
            newChunk[newGlyphIdx] = glyph;
        }

        GlyphUtils.fromGlyphModel(glyphModel, glyph);
        GlyphUtils.toGlyphModel(glyphModel, glyph);
        if (eventManager != null) {
            eventManager.dispatchEvent(new GlyphDataChangedEvent(glyphModel));
        }
    }

    private BitmapFont.Glyph obtainFontGlyph(int code) {
        int glyphChunkIdx = code / CHUNK_SIZE;
        int glyphIdx = code % CHUNK_SIZE;

        BitmapFont.Glyph[] chunk = ensureChunkExists(glyphChunkIdx);

        return chunk[glyphIdx];
    }

    private BitmapFont.Glyph[] ensureChunkExists(int chunkIndex) {
        BitmapFont.Glyph[] chunk = font.getData().glyphs[chunkIndex];;
        if (chunk == null) {
            chunk = new BitmapFont.Glyph[CHUNK_SIZE];
        }
        return chunk;
    }

    //region Factory methods
    public static FontDocument createFromFont(File file) {
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
