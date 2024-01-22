package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.crashinvaders.common.eventmanager.EventManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Model {

    private final EventManager eventManager;
    private FontDocument fontDocument;

    public Model(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void initTestDocument() {
        BitmapFont font = FontDocument.loadBitmapFont(Gdx.files.internal("test/pixola-cursiva-shadow.fnt").file());
//        BitmapFont font = FontDocument.loadBitmapFont(Gdx.files.absolute("test/nokia8.fnt").file());
//        BitmapFont font = FontDocument.loadBitmapFont(Gdx.files.absolute("C:/Projects/libgdx/flippyhex/assets/res/fonts/fivenok.fnt").file());
//        BitmapFont font = FontDocument.loadBitmapFont(Gdx.files.absolute("C:/Projects/libgdx/flippyhex/assets/res/fonts/saint11.fnt").file());
//        BitmapFont font = FontDocument.loadBitmapFont(Gdx.files.absolute("C:/Projects/libgdx/getreact/assets/res/fonts/nokia8.fnt").file());

        // Enable missing glyph.
        BitmapFont.Glyph missingGlyph = font.getData().getGlyph('�');
        if (missingGlyph != null) {
            font.getData().missingGlyph = missingGlyph;
        }

        fontDocument = new FontDocument(eventManager, font);
    }

    public FontDocument getFontDocument() {
        return fontDocument;
    }

    public void saveDocument(FileHandle fileHandle) {
        if (fontDocument == null) return;

        String fontName = fileHandle.nameWithoutExtension();
        BitmapFont font = fontDocument.getFont();
        TextureRegion pageRegion = font.getRegion();
        BitmapFont.BitmapFontData fontData = font.getData();
        String[] imagePaths = new String [fontData.imagePaths.length];

        fontData.setScale(1f);

        // Copy page textures
        {
            File targetDir = fileHandle.file().getParentFile();
            for (int i = 0; i < fontData.imagePaths.length; i++) {
                String imagePath = fontData.imagePaths[i];

                File sourceFile = Gdx.files.absolute(imagePath).file();
//                File targetFile = new File(targetDir, fontName+"-"+i + getFileExtension(sourceFile));
                File targetFile = new File(targetDir, sourceFile.getName());
                try {
                    Files.copy(sourceFile.toPath(), targetFile.toPath());
                } catch (IOException e) {
                    Gdx.app.error("Model", "Can't save page texture: " + imagePath, e);
                }
                imagePaths[i] = targetFile.getName();
            }
        }

        //TODO correct font info
        BitmapFontWriter.FontInfo fontInfo = new BitmapFontWriter.FontInfo(fontName, 8);
        BitmapFontWriter.writeFont(fontData, imagePaths, fileHandle, fontInfo,
                pageRegion.getRegionWidth(), pageRegion.getRegionHeight());

        Gdx.app.log("Model", "Font \"" + fontName+ "\" has been saved!");
    }

    public void dispose() {
        if (fontDocument != null) {
            fontDocument.dispose();
        }
    }

    private static String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getAbsolutePath();

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('/'));

        if (i > p) {
            extension = fileName.substring(i);
        }
        return extension;
    }
}
