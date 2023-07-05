package org.durmiendo.sueno.controllers.resurs;

import mindustry.type.Item;

public class ResMapUnit {
    public Item[] avail = new Item[7];
    public float[] counts = new float[7];

    public ResMapUnit(Item[] i,float[] c) {
        avail = i;
        counts = c;
    }

    public ResMapUnit() {}
}
