# BMFont Editor

A small tool to modify bitmap fonts glyphs ([angel code format](https://www.angelcode.com/products/bmfont)).

<img src="http://i.imgur.com/SN9ZNXR.png" width="512">

# Demo build

An early preview is available from the [releases page](https://github.com/Metaphore/bmfonteditor/releases).

This is a very raw version that can work only with the predefined bitmap font to let you play with it and understand how it works.

It has a simple _bounding box discovery_ algorithm, that helps to define base character params (like size and position). It works like this:

1. Select any character
2. Click on any non empty pixel on the right pane
3. Program will find BB for that glyph

#### Hot keys:

`CTRL+N` create a new glyph

`CTRL+SHIFT+N` create a span of new glyphs

`CTRL+DEL` delete the selected glyph

`CTRL+F` find a glyph by pressing they related key-code

`CTRL+T` open a preview window to try the current font

`CTRL+SPACE` save changes you made for the glyph (within edit panel at the bottom)

`CTRL+S` save the current document in angel code font format
