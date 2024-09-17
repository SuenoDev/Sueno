package org.durmiendo.sueno.entities.bullet;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Posc;

public class SunBulletType extends BasicBulletType {
    public static float damageAbsorption = 0.35f;
    public static float damageLimit = 800f;
    public void update(Bullet b) {
        super.update(b);
        Groups.bullet.each(bb -> {
            boolean ins = bb.type instanceof SubSunBulletType;
            if (bb.team != b.team || ins) {
                float d = Mathf.dst(bb.x, bb.y, b.x, b.y);
                if (d < 96f && ins) {
                    float a = Mathf.atan2(b.x - bb.x, b.y - bb.y);
                    d += 1.2f;
                    bb.vel.add(Mathf.cos(a) / (d / 16f) / 2.4f * Time.delta, Mathf.sin(a) / (d / 16f) / 2.4f * Time.delta);
                } else if (d < 40f) {
                    if (b.owner() instanceof Posc o) {
                        float a = Mathf.atan2(o.x() - bb.x, o.y() - bb.y);
                        d += 1.2f;
                        bb.vel.add(Mathf.cos(a) / (d / 12f) / 2.4f * Time.delta, Mathf.sin(a) / (d / 12f) / 2.4f * Time.delta);
                    }
                }
                if (d < 4f && b.damage < damageLimit && !ins) {
                    b.damage += bb.damage * damageAbsorption;
                    bb.remove();
                }
            }
        });
    }
}

class Temp {
    public static Vec2 v1 = new Vec2();
}
