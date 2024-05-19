package org.durmiendo.sueno.world.units.comps;

import arc.util.ArcRuntimeException;
import mindustry.annotations.Annotations;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import org.durmiendo.sueno.world.units.types.VoidStriderUnitType;

@Annotations.Component
public abstract class VoidStriderComp implements Unitc {
    public boolean destroyed = false;

    @Override
    public void setType(UnitType type) {
        if (!(type instanceof VoidStriderUnitType))
            throw new ArcRuntimeException("VoidStriderc units must be attached to the VoidStriderUnitType, not "
                    + type.getClass() + " (" + type.name + ").");
    }

    @Override
    public void destroy() {
        if (destroyed) return;
        destroyed = true;
        //SCall.voidStriderCollapse((VoidStriderc) this);
    }
}
