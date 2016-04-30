package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class VerticalGradientWidget extends Widget {

    private final VerticalGradientDrawable.Builder gradientData;
    private VerticalGradientDrawable gradientDrawable;

    public VerticalGradientWidget(VerticalGradientDrawable.Builder gradientData) {
        this.gradientData = gradientData;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            gradientDrawable = gradientData.get();
        } else {
            if (gradientDrawable != null) {
                gradientDrawable.dispose();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (gradientDrawable != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            gradientDrawable.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }
}
