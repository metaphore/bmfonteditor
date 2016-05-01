package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GlyphUtils {

    public static GlyphModel toGlyphModel(BitmapFont font, GlyphModel glyphModel, BitmapFont.Glyph glyph) {
        glyphModel.name = "#" + glyph.id;
        glyphModel.code = glyph.id;
        glyphModel.hex = hexCode(glyph.id);
        glyphModel.x = glyph.srcX;
        glyphModel.y = glyph.srcY;
        glyphModel.width = glyph.width;
        glyphModel.height = glyph.height;
        glyphModel.xoffset = glyph.xoffset;
        glyphModel.yoffset = glyph.yoffset;
        glyphModel.xadvance = glyph.xadvance;
        return glyphModel;
    }

    public static GlyphModel fromGlyphModel(BitmapFont font, GlyphModel glyphModel, BitmapFont.Glyph glyph) {
        glyph.id = glyphModel.code;
        glyph.srcX = glyphModel.x;
        glyph.srcY = glyphModel.y;
        glyph.width = glyphModel.width;
        glyph.height = glyphModel.height;
        glyph.xoffset = glyphModel.xoffset;
        glyph.yoffset = glyphModel.yoffset;
        glyph.xadvance = glyphModel.xadvance;
        return glyphModel;
    }

    public static String hexCode(int unicode) {
        String hexValue = Integer.toHexString(unicode).toUpperCase();
        while (hexValue.length() < 4) {
            hexValue = 0 + hexValue;
        }
        return hexValue;
    }
}
