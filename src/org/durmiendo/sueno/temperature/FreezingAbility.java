package org.durmiendo.sueno.temperature;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;

public class FreezingAbility extends Ability {

    public FreezingData fd;
    public FreezingAbility(FreezingData fd) {
        this.fd = fd;
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);

    }
}
