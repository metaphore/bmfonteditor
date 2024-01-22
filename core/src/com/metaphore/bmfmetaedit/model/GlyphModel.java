package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GlyphModel {
    final BitmapFont.Glyph glyph;

    public int code;
    public String name;
    public String hex;
    public int x, y, width, height;
    public int xoffset, yoffset;
    public int xadvance;

    GlyphModel(BitmapFont.Glyph glyph) {
        this.glyph = glyph;
    }

    public void copyFrom(GlyphModel other) {
        this.code = other.code;
        this.name = other.name;
        this.hex = other.hex;
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
        this.xoffset = other.xoffset;
        this.yoffset = other.yoffset;
        this.xadvance = other.xadvance;
    }
}
