package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import mindustry.entities.Effect;
import mindustry.gen.Bullet;

public class SFx {
    // TODO make it or/and shader better
    public static SEffect voidStriderCollapseEffect = new SEffect(30, e -> {
        Draw.color(Color.white);
        float fin = e.life / e.effect.lifeTime;
        float scale = fin < 0.8f ? 1f - fin : fin / 4f;
        float r = 90;

        TextureAtlas.AtlasRegion region = Core.atlas.find("sueno-void-strider-collapse-effect");
        Draw.rect(region, e.x, e.y, r * scale, r * scale);
    });
    public static Effect sun = new Effect(10, e -> {
        Draw.color(Color.white);
        
    }){
        public void render(EffectContainer e){
            renderer.get(e);
            e.lifetime = ((Bullet) e.data).lifetime;
        }
    };
}
