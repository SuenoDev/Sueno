package org.durmiendo.sueno.math;

import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Log;

public class Map{
    public FloatSeq value;
    public int width, height;

    public Map(FloatSeq value, int width, int height) {
        this.value = value;
        this.width = width;
        this.height = height;
    }


    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        value = new FloatSeq(width*height);
        value.setSize(width*height);
    }

    private int getIndex(int x, int y) {
        return y * width + x;
    }

    public float getUnit(int x, int y) {
        if (x > width || x < 0 || y > height || y < 0) return 0;
        return value.get(getIndex(x, y));
    }

    public float getUnit(int p) {
        if (p < 0 || p > value.size) return 0;
        return value.get(p);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void setValue(int x, int y, float t) {
        if (x > width || x < 0 || y > height || y < 0) return;
        value.set(getIndex(x, y), t);
    }

    public void setValue(int p, float t) {
        if (p < 0 || p > value.size) return;
        value.set(p, t);
    }
}
