package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureAtlas;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntMap;
import arc.struct.ObjectSet;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import mindustry.entities.Effect;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import org.durmiendo.sueno.graphics.g3d.wobj.Obj;
import org.durmiendo.sueno.world.blocks.production.VoidExtractor;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.randLenVectors;

public class SFx {
    static Vec2[] t = new Vec2[] {
            new Vec2(), new Vec2(), new Vec2(), new Vec2(), new Vec2()
    };
    
    static Color a1 = Color.valueOf("DCC936");
    static Color a2 = Color.valueOf("DEE075");
    public static Effect redLight = new Effect(22f, e -> {
        float ef = e.foutpowdown();
        float fe = e.finpow();
        float f  = e.fin();
        
        
        t[0].set(1, 0).setAngle(e.rotation + Mathf.randomSeed(e.id+0, -20,   20)).scl(17 * f).add(e.x, e.y).add(Mathf.randomSeed(e.id+5 , -2, 2)*ef, Mathf.randomSeed(e.id+6 , -2, 2)*ef).add(Mathf.cosDeg(e.rotation)*f*72.4f, Mathf.sinDeg(e.rotation)*f*72.4f);
        t[1].set(1, 0).setAngle(e.rotation + Mathf.randomSeed(e.id+1, -40,   40)).scl(14 * f).add(e.x, e.y).add(Mathf.randomSeed(e.id+7 , -2, 2)*fe, Mathf.randomSeed(e.id+8 , -2, 2)*fe).add(Mathf.cosDeg(e.rotation)*f*32.1f, Mathf.sinDeg(e.rotation)*f*32.1f);
        t[2].set(1, 0).setAngle(e.rotation + Mathf.randomSeed(e.id+2, -360, 360)).scl(10 * f).add(e.x, e.y).add(Mathf.randomSeed(e.id+9 , -2, 2)*fe, Mathf.randomSeed(e.id+10, -2, 2)*fe).add(Mathf.cosDeg(e.rotation)*f*26.8f, Mathf.sinDeg(e.rotation)*f*26.8f);
        t[3].set(1, 0).setAngle(e.rotation + Mathf.randomSeed(e.id+3, -40,   40)).scl(6  * f).add(e.x, e.y).add(Mathf.randomSeed(e.id+11, -2, 2)*fe, Mathf.randomSeed(e.id+12, -2, 2)*fe).add(Mathf.cosDeg(e.rotation)*f*20.5f, Mathf.sinDeg(e.rotation)*f*20.5f);
        t[4].set(1, 0).setAngle(e.rotation + Mathf.randomSeed(e.id+4, -20,   20)).scl(3  * f).add(e.x, e.y).add(Mathf.randomSeed(e.id+13, -2, 2)*ef, Mathf.randomSeed(e.id+14, -2, 2)*ef).add(Mathf.cosDeg(e.rotation)*f*10.3f, Mathf.sinDeg(e.rotation)*f*10.3f);
        
        
        Draw.color(a1);
        Lines.stroke(0.7f * ef);
        Lines.line(t[0].x, t[0].y, t[1].x, t[1].y);
        Lines.stroke(1.6f * ef);
        Lines.line(t[1].x, t[1].y, t[2].x, t[2].y);
        Lines.stroke(1.4f * ef);
        Lines.line(t[2].x, t[2].y, t[3].x, t[3].y);
        Lines.stroke(0.8f * ef);
        Lines.line(t[3].x, t[3].y, t[4].x, t[4].y);
        
        Draw.color(a2);
        Lines.stroke(0.1f * ef);
        Lines.line(t[0].x, t[0].y, t[1].x, t[1].y);
        Lines.stroke(0.6f * ef);
        Lines.line(t[1].x, t[1].y, t[2].x, t[2].y);
        Lines.stroke(0.4f * ef);
        Lines.line(t[2].x, t[2].y, t[3].x, t[3].y);
        Lines.stroke(0.2f * ef);
        Lines.line(t[3].x, t[3].y, t[4].x, t[4].y);
        
        Draw.reset();
    });
    
    static Interp parabolicHump = a -> 4 * a * (1 - a);
    static Color col = Color.valueOf("C29420");
    public static Effect phosevil = new Effect(20f, e -> {
        Draw.color(col);
        
        Lines.stroke(e.fin(parabolicHump));
        randLenVectors(e.id, Mathf.randomSeed(e.id, 2, 3), 12f * e.finpow() + 1f, e.rotation, 90f, (x, y) -> {
            float a = Mathf.angle(x, y) - 90f;
            Lines.lineAngle(e.x + x, e.y + y, a, 2f);
        });
    });
    
    public static Effect phosevil2 = new Effect(24f, e -> {
        Draw.color(col);
        
        Lines.stroke(e.fin(parabolicHump) * 0.8f);
        randLenVectors(e.id, Mathf.randomSeed(e.id, 1, 2), 82f * e.finpow(), e.rotation, 25f, (x, y) -> {
            float a = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, a, 2f);
        });
    });
    
    
    // TODO make it or/and shader better
    public static SEffect voidStriderCollapseEffect = new SEffect(30, e -> {
        Draw.color(Color.white);
        float fin = e.life / e.effect.lifeTime;
        float scale = fin < 0.8f ? 1f - fin : fin / 4f;
        float r = e.rad * 90f;

        TextureAtlas.AtlasRegion region = Core.atlas.find("sueno-void-strider-collapse-effect");
        Draw.rect(region, e.x, e.y, r * scale, r * scale);
    });

    public static Effect s1 = new Effect(40f, e -> {
        Draw.color(e.color);
        stroke(e.foutpowdown()*0.7f);

        randLenVectors(e.id, 3, 42f * e.foutpowdown() + 1.3f, e.rotation, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 1f - 16f * Mathf.pow(e.fin() - 0.5f, 4));
        });
    }).layer(SLayers.voidspace);

    public static Effect s12 = new Effect(40f, e -> {
        Draw.color(e.color);
        stroke(e.foutpowdown()*0.7f);

        randLenVectors(e.id, 3, 42f * (1f - e.foutpowdown()) + 1.3f, e.rotation, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 1f - 16f * Mathf.pow(e.fin() - 0.5f, 4));
        });
    }).layer(SLayers.voidspace);

    public static Effect s2 = new Effect(40f, e -> {
        Draw.color(e.color);
        stroke(e.foutpowdown()*0.7f);

        randLenVectors(e.id, 1, 12f * e.fin() + 1.3f, e.rotation-180f, 2f, (x, y) -> {
            float f = 1f - 16f * Mathf.pow(e.fin() - 0.5f, 4);
            Fill.circle(e.x + x, e.y + y, f * 1.2f);
            float a = Mathf.angle(x, y)-180f;
            Drawf.tri(e.x + x, e.y + y, 1.2f * e.foutpowdown(), 3f * f, a - 90f * e.foutpowdown());
            Drawf.tri(e.x + x, e.y + y, 1.2f * e.foutpowdown(), 3f * f, a + 90f * e.foutpowdown());
        });
    }).layer(SLayers.voidspace);

    public static Effect s3 = new Effect(40f, e -> {
        Draw.color(e.color);
        stroke(e.foutpowdown()*0.7f);

        randLenVectors(e.id, 3, 12f * e.fin() + 1.3f, e.rotation-180f, 12f, (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, e.rotation, e.foutpowdown() * 3.5f);
        });
    }).layer(SLayers.voidspace);

    private static Vec2 tmp = new Vec2();

    public static Effect s3l = new Effect(40f, e -> {
        Draw.color(e.color);
        Object[] dat = (Object[]) e.data();
        VoidExtractor.VoidExtractorBuild b = (VoidExtractor.VoidExtractorBuild) dat[0];
        VoidExtractor bl = (VoidExtractor) b.block;
        float tx = (float) dat[1];
        float ty = (float) dat[2];

        if (b.progress > 0) {
            float angle = tmp.set(tx, ty).sub(b.x, b.y).angle();
            float portKey = 340f;
            boolean first = true;

            for (Float pp : VoidExtractor.ports.keys()) {
                float diff = Math.abs(angle - pp);
                diff = Math.min(diff, 360 - diff);

                if (first || diff < Math.abs(angle - portKey)) {
                    portKey = pp;
                    first = false;
                }
            }

            float diff = angle - portKey;

            Vec2 port = VoidExtractor.ports.get(portKey);
            float xx = Mathf.cosDeg(angle-180f+diff) * 12f + tx;
            float yy = Mathf.sinDeg(angle-180f+diff) * 12f + ty;

            Lines.stroke(3f * b.voidSubstance / bl.voidCap * e.foutpowdown());
            Fill.circle(tx, ty, 3f * b.voidSubstance / bl.voidCap * e.foutpowdown());

            curve(b.x, b.y, port.x+b.x, port.y+b.y, xx, yy, tx, ty, 15);
        }
    }).layer(SLayers.voidspace);

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
