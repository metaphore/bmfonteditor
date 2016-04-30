package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class VerticalGradientDrawable extends BaseDrawable implements Disposable {
    private static final Color TMP_COL0 = new Color();
    private static final Color TMP_COL1 = new Color();

    private final Array<ColorStop> colorStops;
    private final ShapeRenderer shapeRenderer;

    private VerticalGradientDrawable(Array<ColorStop> colorStops) {
        this.colorStops = colorStops;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(batch.getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Remember current blending flag and enable blending
        boolean prevBlendState = Gdx.gl.glIsEnabled(GL20.GL_BLEND);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        float nextY = y;
        for (int i = 0; i < colorStops.size-1; i++) {
            ColorStop colorStop = colorStops.get(i);
            float segmentHeight = colorStop.weight * height;

            Color col0 = TMP_COL0.set(colorStop.color).mul(shapeRenderer.getColor());
            Color col1 = TMP_COL1.set(colorStops.get(i+1).color).mul(shapeRenderer.getColor());

            shapeRenderer.rect(x, nextY, width, segmentHeight, col0, col0, col1, col1);

            nextY += segmentHeight;
        }

        shapeRenderer.end();
        // Restore blending state to one it has just before
        if (prevBlendState) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        batch.begin();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    public static class Builder {
        private final Array<ColorStop> colorStops = new Array<>();

        public Builder colorStop(Color color) {
            return colorStop(color, 1f);
        }

        public Builder colorStop(Color color, float weight) {
            colorStops.add(new ColorStop(color, weight));
            return this;
        }

        public VerticalGradientDrawable get() {
            normalizeColorWeights();
            return new VerticalGradientDrawable(colorStops);
        }

        private void normalizeColorWeights() {
            float weightSum = 0f;
            // Do not pay attention at last value
            for (int i = 0; i < colorStops.size-1; i++) {
                ColorStop colorStop = colorStops.get(i);
                weightSum += colorStop.weight;
            }
            float weightFactor = 1f / weightSum;
            for (ColorStop colorStop : colorStops) {
                colorStop.weight *= weightFactor;
            }
        }
    }

    private static class ColorStop {
        final Color color;
        float weight;

        public ColorStop(Color color, float weight) {
            this.color = color;
            this.weight = weight;
        }
    }
}
