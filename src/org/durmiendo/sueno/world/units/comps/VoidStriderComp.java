package org.durmiendo.sueno.world.units.comps;

import arc.util.ArcRuntimeException;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.annotations.Annotations;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import org.durmiendo.sueno.utils.SLog;
import org.durmiendo.sueno.world.units.types.VoidStriderUnitType;

@Annotations.Component
public abstract class VoidStriderComp implements Unitc {
    public boolean destroyed = false;
    public float voidSubstance;

    @Override
    public void setType(UnitType type) {
        if (!(type instanceof VoidStriderUnitType))
            throw new ArcRuntimeException("VoidStriderc units must be attached to the VoidStriderUnitType, not "
                    + type.getClass() + " (" + type.name + ").");

        if (voidSubstance == 0) voidSubstance = ((VoidStriderUnitType) type).voidSubstance;
    }

    @Override
    public void destroy() {
        if (destroyed) return;
        destroyed = true;
    }

    @Override
    public void write(Writes write) {
        write.f(voidSubstance);
    }

    @Override
    public void read(Reads read) {
        voidSubstance = read.f();
        SLog.info("read " + voidSubstance);
    }
}
