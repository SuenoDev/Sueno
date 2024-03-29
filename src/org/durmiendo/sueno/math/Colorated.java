package org.durmiendo.sueno.math;

import arc.graphics.Color;
import arc.math.Interp;

public class Colorated {
    public static Color gradient(Color start, Color end, float v) {
        return applyToColor(Interp.linear, start, end, v);
    }

    public static Color applyToColor(Interp interpolation, Color start, Color end, float v) {
        return new Color(
                interpolation.apply(start.r, end.r, v),
                interpolation.apply(start.g, end.g, v),
                interpolation.apply(start.b, end.b, v),
                interpolation.apply(start.a, end.a, v)
        );
    }
}
