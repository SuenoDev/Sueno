package org.durmiendo.sueno.entities.bullet;

import arc.util.pooling.Pools;
import mindustry.gen.Bullet;

public class SBullet extends Bullet {
    public short ancestor = 0;

    public static Bullet create() {
        return Pools.obtain(SBullet.class, SBullet::new);
    }
}
