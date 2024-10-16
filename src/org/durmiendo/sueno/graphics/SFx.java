package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureAtlas;
import arc.math.Mathf;
import mindustry.entities.Effect;
import mindustry.gen.Bullet;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

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

    public static Effect destroy = new Effect(150f, e -> {
        Draw.color(e.color);

        Mathf.rand.setSeed(e.id);
        stroke(e.foutpowdown()*0.7f);
        randLenVectors(e.id, 2, 42f*e.fin()+1.3f, e.rotation, 100f, (x,y) -> {
            Lines.lineAngle(e.x+x,e.y+y,e.rotation+Mathf.random(20,-20f)*e.fin()*4f,7.5f*e.foutpowdown());
        });
    });

    private static final float c1 = 7.9f, c2 = 6.1f, c3 = 5.5f,
            d1 = 1f, d2 = 1/3f, d3 = 2/3f;

    private static final Color
            sunColor1 = Color.valueOf("FFF2B3"),
            sunColor2 = Color.valueOf("D9C283"),
            sunColor3 = Color.valueOf("D3A068");

    public static Effect sun = new Effect(10, e -> {
        Draw.color(Color.white);
        float fin = e.fin();

        float fin1;
        float fin2;
        float fin3;

        fin1 = 1f - (Mathf.floor(fin*c1 - d1) - (fin*c1 - d1) + 1f);
        fin2 = 1f - (Mathf.floor(fin*c2 - d2) - (fin*c2 - d2) + 1f);
        fin3 = 1f - (Mathf.floor(fin*c3 - d3) - (fin*c3 - d3) + 1f);

        color(sunColor1);
        stroke(2.3f * (1f-fin1) - 0.1f);
        randLenVectors(e.id + Mathf.floor(fin*c1 - d1), 22, 1f + 24f * fin1, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.2f + fin1 * 4.2f);
        });

        color(sunColor2);
        stroke(2.8f * (1f-fin2) - 0.1f);
        randLenVectors(e.id + Mathf.floor(fin*c2 - d2) + Mathf.ceil(c2), 18, 1f + 18f * fin2, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.1f + fin2 * 2.6f);
        });

        color(sunColor3);
        stroke(3.7f * (1f-fin3) - 0.1f);
        randLenVectors(e.id + Mathf.floor(fin*c3 - d3) + Mathf.ceil(2*c3), 16, 1f + 12f * fin3, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + fin3 * 1.8f);
        });
    }){
        public void render(EffectContainer e){
            renderer.get(e);
            e.lifetime = ((Bullet) e.data).lifetime;
        }
    };

    public static Effect deadSun = new Effect(42f, e -> {
        Draw.color(Color.white);
        float fin = e.fin();

        e.scaled(7, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 3f + i.fin() * 10f);
        });

        color(Color.gray);

        randLenVectors(e.id + 1, 8, 2f + 19f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 7f + 0.5f);
            Fill.circle(e.x + x / 2f, e.y + y / 2f, e.fout() * 4f);
        });


        color(sunColor1);
        stroke(2.3f * (1f-fin) - 0.1f);
        randLenVectors(e.id + 3, 28, 1f + 48f * fin, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.2f + fin * 28f);
        });

        color(sunColor2);
        stroke(2.8f * (1f-fin) - 0.1f);
        randLenVectors(e.id + 4, 22, 1f + 24f * fin, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.1f + fin * 12f);
        });

        color(sunColor3);
        stroke(3.7f * (1f-fin) - 0.1f);
        randLenVectors(e.id + 5, 14, 1f + 18f * fin, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + fin * 6f);
        });
    });
}
