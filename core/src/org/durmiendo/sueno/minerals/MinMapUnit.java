package org.durmiendo.sueno.minerals;

import mindustry.type.Item;

public class MinMapUnit {
    public Item[] avail = new Item[7];
    public float[] counts = new float[7];

    public MinMapUnit(Item[] i, float[] c) {
        avail = i;
        counts = c;
    }

    public MinMapUnit() {}
}
