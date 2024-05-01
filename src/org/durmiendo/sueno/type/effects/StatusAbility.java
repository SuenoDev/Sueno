package org.durmiendo.sueno.type.effects;

import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import org.durmiendo.sueno.entities.effect.StatusEntity;

public class StatusAbility extends Ability{
    public Seq<StatusEntity> statuses;

    @Override
    public void update(Unit unit) {
        super.update(unit);
    }
}
