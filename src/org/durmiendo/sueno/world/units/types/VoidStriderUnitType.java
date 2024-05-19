package org.durmiendo.sueno.world.units.types;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import org.durmiendo.sueno.graphics.*;

public class VoidStriderUnitType extends UnitType {
    public SEffect collapseEffect = SFx.voidStriderCollapseEffect;
    public float collapseRadius = 80f;
    public float priority = -1;
    public float cost = -1;
    public TextureRegion vessel;

    public VoidStriderUnitType(String name) {
        super(name);

        Events.run(EventType.Trigger.drawOver, () -> {
            Draw.drawRange(SLayers.voidspace, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
                Vars.renderer.effectBuffer.end();
                Vars.renderer.effectBuffer.blit(SShaders.voidSpaceShader);
            });
        });
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
        Draw.rect(vessel, unit.x, unit.y, unit.rotation - 90f);
    }

    @Override
    public void killed(Unit unit) {
        super.killed(unit);

        VoidStriderCollapseEffectController.at(unit.x, unit.y, collapseEffect);
        Geometry.circle(Mathf.round(unit.x / 8f), Mathf.round(unit.y / 8f), Mathf.round(collapseRadius/8f), (x, y) -> {
            Building b = Vars.world.build(x, y);
            if (b != null) {
                b.kill();
            }
        });
        Groups.unit.each(u -> u.dst(unit.x, unit.y) < collapseRadius, Unitc::remove);
    }
}
