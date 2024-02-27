package org.durmiendo.sueno.world.units.types;

import mindustry.type.UnitType;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.SFx;

public class VoidStriderUnitType extends UnitType {
    public SEffect collapseEffect = SFx.voidStriderCollapseEffect;
    public float collapseRadius = 80f;

    public VoidStriderUnitType(String name) {
        super(name);
    }
}
