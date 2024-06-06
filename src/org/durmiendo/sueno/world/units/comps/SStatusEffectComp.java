package org.durmiendo.sueno.world.units.comps;


import mindustry.annotations.Annotations;
import mindustry.gen.Posc;
import mindustry.type.UnitType;

@Annotations.Component
public abstract class SStatusEffectComp implements Posc {
    @Annotations.Import UnitType type;
    @Annotations.Import float maxHealth;
}
