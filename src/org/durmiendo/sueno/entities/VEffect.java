package org.durmiendo.sueno.entities;

import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.entities.Effect;
import org.durmiendo.sueno.graphics.SLayers;

public class VEffect extends Effect {
    private static final EffectContainer container = new EffectContainer();
    public VEffect(float life, Cons<EffectContainer> r) {
        super(life, r);
        layer = SLayers.voidspace;
    }

    public float render(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data) {
        container.set(id, color, life, lifetime, rotation, x, y, data);
        Draw.z(SLayers.voidspace);
        Draw.reset();
        this.render(container);
        Draw.reset();
        return container.lifetime;
    }
}
