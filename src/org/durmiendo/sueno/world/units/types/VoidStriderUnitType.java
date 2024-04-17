package org.durmiendo.sueno.world.units.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.graphics.SShaders;

public class VoidStriderUnitType extends UnitType {
    public SEffect collapseEffect = SFx.voidStriderCollapseEffect;
    public float collapseRadius = 80f;
    public float priority = -1;
    public float cost = -1;
    public TextureRegion vessel;

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

        Draw.draw(Layer.groundUnit + 0.12f, () -> {
            Draw.shader(SShaders.voidSpaceShader, true);
            Draw.rect(vessel, unit.x, unit.y, unit.rotation - 90f);
        });
    }
}
