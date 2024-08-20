package org.durmiendo.sueno.math.area;


import java.util.Objects;

public class RArea implements Area {
    public float x, y;
    public float w, h;

    public RArea(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean insidePoint(float a, float b) {
        return a >= x && a <= w && b >= y && b <= h;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, w, h);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RArea d)) return false;
        return x == d.x && y == d.y && w == d.w && h == d.h;
    }
}
