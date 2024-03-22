package org.durmiendo.sueno.world.units.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Shaders;
import mindustry.type.UnitType;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.SFx;

public class VoidStriderUnitType extends UnitType {
    public SEffect collapseEffect = SFx.voidStriderCollapseEffect;
    public float collapseRadius = 80f;
    public float priority = -1;
    public float cost = -1;
    public TextureRegion vessel;
    public TextureRegion wing;

    public VoidStriderUnitType(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        vessel = Core.atlas.find("sueno-" + name + "-vessel");
        wing = SVars.core.getRegion(name + "-wing");
    }

    @Override
    public void drawCell(Unit unit) {
        super.drawCell(unit);
        Draw.rect(vessel, unit.x, unit.y, unit.rotation - 90);
        Draw.draw(Layer.groundUnit+0.1f, () -> {
            Draw.shader(Shaders.water);

        });
        Draw.rect(wing, unit.x, unit.y, unit.rotation - 90);
        Draw.rect(wing, unit.x, unit.y, unit.rotation + 90);
        Draw.reset();
    }

    @Override
    public void drawBody(Unit unit) {

        super.drawBody(unit);

    }
}
