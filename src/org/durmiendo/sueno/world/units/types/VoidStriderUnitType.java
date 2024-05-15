package org.durmiendo.sueno.world.units.types;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.graphics.SShaders;

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
}
