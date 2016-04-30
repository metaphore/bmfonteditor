package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class GradientFillWidget extends Widget {
    private static final Color tmpColor0 = new Color();
    private static final Color tmpColor1 = new Color();

    private final Color colorStart = new Color(Color.WHITE);
    private final Color colorEnd = new Color(Color.WHITE);

    private ShapeRenderer shapeRenderer;
    private Direction direction = Direction.BOTTOM_TOP;

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            shapeRenderer = new ShapeRenderer();
        } else {
            if (shapeRenderer != null) {
                shapeRenderer.dispose();
                shapeRenderer = null;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (shapeRenderer == null) { return; }

        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Color colorStart = tmpColor0.set(this.colorStart).mul(getColor()).mul(1f, 1f, 1f, parentAlpha);
        Color colorEnd = tmpColor1.set(this.colorEnd).mul(getColor()).mul(1f, 1f, 1f, parentAlpha);
        Color col0, col1, col2, col3;
        switch (direction) {
            case LEFT_RIGHT:
                col0 = col3 = colorStart;
                col1 = col2 = colorEnd;
                break;
            case BOTTOM_TOP:
                col0 = col1 = colorStart;
                col2 = col3 = colorEnd;
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight(), col0, col1, col2, col3);

        shapeRenderer.end();
        batch.begin();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setStartColor(Color color) {
        colorStart.set(color);
    }

    public void setEndColor(Color color) {
        colorEnd.set(color);
    }

    public enum Direction {
        LEFT_RIGHT,
        BOTTOM_TOP
    }
}
