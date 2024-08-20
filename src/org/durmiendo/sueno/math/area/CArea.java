package org.durmiendo.sueno.math.area;

import arc.math.Mathf;

import java.util.Objects;

public class CArea implements Area{
    public float x, y, r;

    public CArea(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public boolean insidePoint(float a, float b) {
        return Mathf.dst(x, y, a, b) <= r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, r);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CArea d)) return false;
        return x == d.x && y == d.y && r == d.r;
    }
}
