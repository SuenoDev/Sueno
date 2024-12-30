package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.math.Mathf;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.world.blocks.defense.turrets.PowerTurret;

public class VioTurret extends PowerTurret {
    Seq<BulletType> bulletTypes = new Seq<>();
    FloatSeq changes = new FloatSeq();
    float a = 0;

    public VioTurret(String name) {
        super(name);
    }

    public void addb(BulletType bulletType, float c) {
        bulletTypes.add(bulletType);
        changes.add(c);
        a += c;
    }

    public class VioTurretBuild extends PowerTurret.PowerTurretBuild {
        public BulletType peekAmmo() {
            if (bulletTypes.size == 0) return null;
            float randomNumber = Mathf.rand.nextFloat();
            float cumulativeProbability = 0;
            for (int i = 0; i < changes.size; i++) {
                cumulativeProbability += changes.get(i)/a;
                if (randomNumber <= cumulativeProbability) {
                    return bulletTypes.get(i);
                }
            }

            return bulletTypes.get(bulletTypes.size -1);
        }
    }
}
