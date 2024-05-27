package org.durmiendo.sueno.world.units.comps;

import arc.util.Time;
import mindustry.annotations.Annotations;
import mindustry.gen.Unitc;

@Annotations.Component
public abstract class TimedDestroyComp implements Unitc{
    @Override
    public void update() {
        damage(maxHealth() / 3.5f * 0.005f * Time.delta, false);
    }
}
