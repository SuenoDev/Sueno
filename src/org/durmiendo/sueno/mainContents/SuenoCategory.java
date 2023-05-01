package org.durmiendo.sueno.mainContents;

import mindustry.type.Category;

public enum SuenoCategory {
    turret,
    production,
    distribution,
    liquid,
    power,
    defense,
    crafting,
    units,
    effect,
    logic,
    test;

    public static final SuenoCategory[] all = values();

    public SuenoCategory prev(){
        return all[(ordinal() - 1 + all.length) % all.length];
    }

    public SuenoCategory next(){
        return all[(ordinal() + 1) % all.length];
    }
}
