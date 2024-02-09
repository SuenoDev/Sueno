package org.durmiendo.sueno.world.units.comps;

import mindustry.annotations.Annotations;
import mindustry.gen.Posc;
import mindustry.gen.Unitc;
import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.VoidStriderc;

@Annotations.Component
public abstract class VoidStriderComp implements Unitc, Posc {
    @Override
    public void destroy() {
        SCall.voidStriderCollapse((VoidStriderc) this);
    }
}
