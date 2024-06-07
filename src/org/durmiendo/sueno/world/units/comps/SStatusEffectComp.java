package org.durmiendo.sueno.world.units.comps;


import arc.struct.Seq;
import mindustry.annotations.Annotations;
import mindustry.gen.Unitc;
import org.durmiendo.sueno.entities.units.SStatusEntry;
import org.durmiendo.sueno.type.effects.SStatusEffect;
import org.durmiendo.sueno.world.units.types.SUnitType;

@Annotations.Component
public abstract class SStatusEffectComp implements Unitc {
    Seq<SStatusEntry> statuses = new Seq<>(4);
    @Annotations.Import SUnitType type;

    SStatusEntry add(SStatusEffect effect){
        SStatusEntry entry = new SStatusEntry();
        entry.make(effect);
        entry.level = type.resistances.get(effect);
        statuses.add(entry);
        return entry;
    }

    void clearSStatuses(){
        statuses.clear();
    }


    void remove(SStatusEffect effect){
        statuses.remove((e) -> e.effect == effect);
    }

    void touch(SStatusEffect effect){
        SStatusEntry entry = statuses.find(e -> e.effect == effect);
        if (entry == null) entry = add(effect);
        entry.touch(effect.power);
    }
}
