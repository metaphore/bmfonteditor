# BMFont Editor

Small tool to add/edit glyphs in bitmap font (angel code format).


I spent few days all over the net trying to find a tool that will help me extend already generated bitmap font with a new characters.
Of course I could do that in text markup by hand, but when you need to add ~100 glyphs, correct their params and see if they look fine in a text line - it's a suicide.
My searches brought me to the conclusion that this world in total lack of such a thing and I decided to write it by my own.

## Still in development

<img src="http://i.imgur.com/SN9ZNXR.png" width="512">

### Early preview version

Early preview asseble available from [releases page](https://github.com/Metaphore/bmfonteditor/releases).

This is very raw version that can work only with predefined bitmap font to let you play with it and understand how it works.

It has find BB algorithm, that helps to define base character params. It works like this:

1. Select any character
2. Click on any non empty pixel in right pane
3. Program will find BB for that glyph

#### Hot keys:

`CTRL+N` create new glyph

`CTRL+SHIFT+N` create span of new glyphs

`CTRL+DEL` delete selected glyph

`CTRL+F` find glyph by pressing approapriate key

`CTRL+T` open preview window to try current font

`CTRL+SPACE` save changes you made within bottom input fields

`CTRL+S` save current document in angel code font format
