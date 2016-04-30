package com.crashinvaders.common;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MathUtils2 {

    public static float maxAbs(float x, float y) {
        return Math.abs(x) >= Math.abs(y) ? x : y;
    }

    public static float minAbs(float x, float y) {
        return Math.abs(x) <= Math.abs(y) ? x : y;
    }

    public static float lerpCut(float progress, float progressLowCut, float progressHighCut, float fromValue, float toValue) {
        progress = MathUtils.clamp(progress, progressLowCut, progressHighCut);
        float a = (progress - progressLowCut) / (progressHighCut - progressLowCut);
        return MathUtils.lerp(fromValue, toValue, a);
    }

    public static float lerpCut(float progress, float progressLowCut, float progressHighCut, float fromValue, float toValue, Interpolation interpolation) {
        progress = MathUtils.clamp(progress, progressLowCut, progressHighCut);
        float a = (progress - progressLowCut) / (progressHighCut - progressLowCut);
        return MathUtils.lerp(fromValue, toValue, interpolation.apply(a));
    }
}
