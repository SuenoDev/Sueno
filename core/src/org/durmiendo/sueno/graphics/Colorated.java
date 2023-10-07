package org.durmiendo.sueno.graphics;

import arc.graphics.Color;
import arc.math.Interp;

public class Colorated {
    public static Color gradient(Color start, Color end, float v) {
        float red = Interp.linear.apply(start.r, end.r, v);
        float green = Interp.linear.apply(start.g, end.g, v);
        float blue = Interp.linear.apply(start.b, end.b, v);
        float alpha = Interp.linear.apply(start.a, end.a, v);
        return new Color(red, green, blue, alpha);
    }
}
