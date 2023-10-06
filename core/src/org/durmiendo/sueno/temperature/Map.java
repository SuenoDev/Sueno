package org.durmiendo.sueno.temperature;

import arc.struct.Seq;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Map{
    public Seq<Float> value;
    public int weight, height;

    public Map(Seq<Float> value, int weight, int height) {
        this.value = value;
        this.weight = weight;
        this.height = height;
    }


    public Map(int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    public int getIndex(int x, int y) {
        return x*weight+y;
    }

    public float getUnit(int x, int y) {
        return value.get(getIndex(x, y));
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }


    public void setValue(int x, int y, float t) {
        value.set(getIndex(x, y), t);
    }
}
