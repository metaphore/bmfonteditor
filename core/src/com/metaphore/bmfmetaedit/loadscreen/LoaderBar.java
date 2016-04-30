package com.metaphore.bmfmetaedit.loadscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

class LoaderBar implements Disposable {

    private final ShapeRenderer renderer = new ShapeRenderer();
    private final Color colorBack = new Color(0x444444ff);
    private final Color colorFill = new Color(0x888888ff);
    private float barWidth, barHeight;

    public void render(float progress) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(colorBack);
        renderer.rect(0, 0, barWidth, barHeight);

        renderer.setColor(colorFill);
        renderer.rect(0, 0, barWidth*progress, barHeight);

        renderer.end();
    }

    public void resize() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        barWidth = screenW;
        barHeight = screenH * 0.1f;
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
