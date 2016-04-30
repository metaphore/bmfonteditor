package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GlyphUtils {

    public static GlyphModel toGlyphModel(GlyphModel glyphModel, BitmapFont.Glyph glyph) {
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

    public static String hexCode(int unicode) {
        String hexValue = Integer.toHexString(unicode).toUpperCase();
        while (hexValue.length() < 4) {
            hexValue = 0 + hexValue;
        }
        return hexValue;
    }
}
