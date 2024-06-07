package org.durmiendo.sueno.world.units.types;

import arc.struct.ObjectMap;
import mindustry.type.UnitType;
import org.durmiendo.sueno.type.effects.SStatusEffect;

public class SUnitType extends UnitType {
    public ObjectMap<SStatusEffect, Float> resistances = new ObjectMap<>();
    public SUnitType(String name) {
        super(name);
    }
}

