package com.metaphore.bmfmetaedit.mainscreen.view.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.mainscreen.MainScreenContext;
import com.metaphore.bmfmetaedit.model.GlyphModel;

class AdjacentPixelsBBGeneratorInputHandler extends InputListener {

    private final MainScreenContext ctx;

    public AdjacentPixelsBBGeneratorInputHandler(MainScreenContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            return tryBuildBB(MathUtils.floor(x), MathUtils.ceil(y));
        } else {
            return false;
        }
    }

    private boolean tryBuildBB(int x, int y) {
        GlyphModel selectedGlyph = ctx.getSelectionManager().getSelectedGlyph();
        if (selectedGlyph == null) return false;

        Texture texture = App.inst().getModel().getFontDocument().getFont().getRegion().getTexture();
        TextureData textureData = texture.getTextureData();
        if (!textureData.isPrepared()) textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();
        try {
            if (isEmpty(pixmap, x, y)) {
                return false;
            }

            Scope scope = new Scope(pixmap, x, y);
            scope.processCycle();

            selectedGlyph.x = scope.getX();
            selectedGlyph.y = pixmap.getHeight() - scope.getY() - scope.getHeight() + 1 - 1;
            selectedGlyph.width = scope.getWidth();
            selectedGlyph.height = scope.getHeight() + 1;
            selectedGlyph.xoffset = 0;
            selectedGlyph.yoffset = 10;
            selectedGlyph.xadvance = selectedGlyph.width-1;
            App.inst().getModel().getFontDocument().saveGlyphData(selectedGlyph);

            return true;
        } finally {
            pixmap.dispose();
        }
    }

    private static boolean isEmpty(Pixmap pixmap, int x, int y) {
        int pixel = pixmap.getPixel(x, pixmap.getHeight() - y);
        int alpha = pixel & 0x000000ff;
        return alpha == 0;
    }

    private static class Scope {
        public static final int TOP = 0;
        public static final int RIGHT = 1;
        public static final int BOT = 2;
        public static final int LEFT = 3;

        private static int MAX_SIDE = 32;

        private final Pixmap pixmap;
        private int left, right, top, bot;

        private int direction = -1;
        private int emptySides;

        public Scope(Pixmap pixmap, int x, int y) {
            if (isEmpty(pixmap, x, y)) throw new IllegalArgumentException("Pixmap is empty at initial point");

            this.pixmap = pixmap;
            left = right = x;
            bot = top = y;
        }

        public void processCycle() {
            if (emptySides == 4) {
                return;
            }

            if (right - left >= MAX_SIDE || top - bot >= MAX_SIDE) {
                Gdx.app.error("AdjacentBB", "Scope is calculation stopped due to exceed max size");
                return;
            }

            direction = (direction+1)%4;

            switch (direction) {
                case TOP:
                    if (hasPixelH(top+1)) {
                        top++;
                        emptySides = 0;
                    } else emptySides++;
                    break;
                case RIGHT:
                    if (hasPixelV(right+1)) {
                        right++;
                        emptySides = 0;
                    } else emptySides++;
                    break;
                case BOT:
                    if (hasPixelH(bot-1)) {
                        bot--;
                        emptySides = 0;
                    } else emptySides++;
                    break;
                case LEFT:
                    if (hasPixelV(left-1)) {
                        left--;
                        emptySides = 0;
                    } else emptySides++;
                    break;
            }
            processCycle();
        }

        private boolean hasPixelH(int y) {
            for (int x = Math.max(left-1, 0); x < Math.min(right+1, pixmap.getWidth()); x++) {
                if (!isEmpty(pixmap, x, y)) return true;
            }
            return false;
        }

        private boolean hasPixelV(int x) {
            for (int y = Math.max(bot-1, 0); y < Math.min(top+1, pixmap.getHeight()); y++) {
                if (!isEmpty(pixmap, x, y)) return true;
            }
            return false;
        }

        public int getX() {
            return left;
        }
        public int getY() {
            return bot;
        }
        public int getWidth() {
            return right-left+1;
        }
        public int getHeight() {
            return top-bot+1;
        }
    }
}
