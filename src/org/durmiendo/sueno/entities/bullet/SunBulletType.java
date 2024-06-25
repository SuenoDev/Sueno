package org.durmiendo.sueno.entities.bullet;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;

public class SunBulletType extends BasicBulletType {
    public static float damageAbsorption = 0.35f;
    public static float damageLimit = 800f;
    public void update(Bullet b) {
        super.update(b);
        Groups.bullet.each(bb -> {
            if (bb.team != b.team) {
                float d = Mathf.dst(bb.x, bb.y, b.x, b.y);
                if (d < 96f) {
                    float a = Mathf.atan2(b.x - bb.x, b.y - bb.y);
                    bb.vel.add(Mathf.cos(a) / (d / 14f) / 2.2f * Time.delta, Mathf.sin(a) / (d / 14f) / 1.4f * Time.delta);
                }
                if (d < 4f && b.damage < damageLimit) {
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
