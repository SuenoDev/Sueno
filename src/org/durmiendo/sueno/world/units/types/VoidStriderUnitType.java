package org.durmiendo.sueno.world.units.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;

public class VoidStriderUnitType extends UnitType {
    public SEffect collapseEffect = SFx.voidStriderCollapseEffect;
    public float collapseRadius = 80f;
    public float priority = -1;
    public float cost = -1;
    public TextureRegion vessel;
    public float voidSubstance = 100f;

    public VoidStriderUnitType(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        vessel = Core.atlas.find(name + "-vessel");
    }

    @Override
    public void drawCell(Unit unit) {
        super.drawCell(unit);

        Draw.z(SLayers.voidspace);
        if (unit instanceof VoidStriderc u) {
            float s = u.voidSubstance() / voidSubstance;
            Draw.rect(vessel, unit.x, unit.y, vessel.width * vessel.scl() * Draw.xscl * s, vessel.height * vessel.scl() * Draw.yscl, unit.rotation - 90f);
        }

    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        if (unit instanceof VoidStriderc u) {
            if (u.voidSubstance() <= 0) u.destroy();
        }
    }

    @Override
    public void killed(Unit unit) {
        super.killed(unit);
        if (unit instanceof VoidStriderc u && u.voidSubstance() > 0) {
            float rad = u.voidSubstance() / voidSubstance;
            VoidStriderCollapseEffectController.at(unit.x, unit.y, collapseEffect, rad);
            Geometry.circle(Mathf.round(unit.x / 8f), Mathf.round(unit.y / 8f), Mathf.round(collapseRadius/8f/rad), (x, y) -> {
                Building b = Vars.world.build(x, y);
                if (b != null) {
                    b.kill();
                }
            });
            Groups.unit.each(un -> un.dst(unit.x, unit.y) < collapseRadius * rad, Unitc::remove);
        }

    }

}
