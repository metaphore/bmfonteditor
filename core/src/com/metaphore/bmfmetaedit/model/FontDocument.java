package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.model.events.GlyphModelChangedEvent;

import java.io.File;

public class FontDocument {
    public static final int CHUNK_SIZE = 512;
    private final BitmapFont font;
    private final EventManager eventManager;
    private final Array<GlyphModel> glyphs;

    private final Array<PageModel> pages;

    public FontDocument(EventManager eventManager, BitmapFont font) {
        this.eventManager = eventManager;
        this.font = font;

        glyphs = new Array<>(1024);
        for (BitmapFont.Glyph[] glyphChunk : font.getData().glyphs) {
            if (glyphChunk == null) continue;
            for (BitmapFont.Glyph glyph : glyphChunk) {
                if (glyph == null) continue;

                GlyphModel glyphModel = GlyphUtils.toGlyphModel(font, new GlyphModel(glyph), glyph);
                glyphs.add(glyphModel);
            }
        }

        String[] pageImagePaths = font.getData().imagePaths;
        pages = new Array<>(pageImagePaths.length);
        for (String pageImagePath : pageImagePaths) {
            PageModel pageModel = new PageModel(eventManager, Gdx.files.absolute(pageImagePath));
            pages.add(pageModel);
        }
    }

    void dispose() {
        font.dispose();

        for (PageModel page : pages) {
            page.dispose();
        }
    }

    public BitmapFont getFont() {
        return font;
    }

    public Array<PageModel> getPages() {
        return pages;
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

        GlyphUtils.fromGlyphModel(font, glyphModel, glyph);
        GlyphUtils.toGlyphModel(font, glyphModel, glyph);
        eventManager.dispatchEvent(new GlyphModelChangedEvent(glyphModel, GlyphModelChangedEvent.Type.UPDATED));
    }

    private BitmapFont.Glyph obtainFontGlyph(int code) {
        int glyphChunkIdx = code / CHUNK_SIZE;
        int glyphIdx = code % CHUNK_SIZE;

        BitmapFont.Glyph[] chunk = ensureChunkExists(glyphChunkIdx);

        return chunk[glyphIdx];
    }

    private BitmapFont.Glyph[] ensureChunkExists(int chunkIndex) {
        BitmapFont.Glyph[] chunk = font.getData().glyphs[chunkIndex];
        if (chunk == null) {
            chunk = new BitmapFont.Glyph[CHUNK_SIZE];
            font.getData().glyphs[chunkIndex] = chunk;
        }
        return chunk;
    }

    //region Factory methods
    public static BitmapFont loadBitmapFont(File file) {
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

        return bitmapFont;
    }

    public GlyphModel createGlyph(int code) {
        int chunkIdx = code / CHUNK_SIZE;
        int glyphIdx = code % CHUNK_SIZE;
        BitmapFont.Glyph[] chunk = ensureChunkExists(chunkIdx);

        // Check if glyph is not exists yet
        BitmapFont.Glyph glyph = chunk[glyphIdx];
        if (glyph != null) {
            Gdx.app.error("FontDocument", "Glyph with code: " + code + " already exists");
            return null;
        }

        glyph =  new BitmapFont.Glyph();
        glyph.id = code;
        chunk[glyphIdx] = glyph;

        GlyphModel glyphModel = GlyphUtils.toGlyphModel(font, new GlyphModel(glyph), glyph);
        glyphs.add(glyphModel);

        eventManager.dispatchEvent(new GlyphModelChangedEvent(glyphModel, GlyphModelChangedEvent.Type.CREATED));
        return glyphModel;
    }

    public void deleteGlyph(GlyphModel glyphModel) {
        boolean result = glyphs.removeValue(glyphModel, true);
        if (!result) {
            throw new IllegalArgumentException("Glyph is not exists: " + glyphModel.code);
        }

        // Remove from array
        ensureChunkExists(glyphModel.code / CHUNK_SIZE)[glyphModel.code % CHUNK_SIZE] = null;

        eventManager.dispatchEvent(new GlyphModelChangedEvent(glyphModel, GlyphModelChangedEvent.Type.REMOVED));
    }

    public GlyphModel findGlyphByCode(int code) {
        for (GlyphModel glyph : glyphs) {
            if (glyph.code == code) return glyph;
        }
        return null;
    }
    //endregion
}
