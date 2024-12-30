package org.durmiendo.sueno.type.weapon;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import java.util.Comparator;

public class MultiBulletWeapon extends Weapon {
    public Seq<BulletType> bulletTypes = new Seq<>();

    public void addStats(UnitType u, Table t){
        for (BulletType b : bulletTypes) {
            bullet = b;
            super.addStats(u, t);
        }
    }

    public float dps(){
        float d = 0;
        for (BulletType b : bulletTypes) {
            bullet = b;
            d += super.dps();
        }
        return d;
    }

    public float shotsPerSec(){
        float s = 0;
        for (BulletType b : bulletTypes) {
            bullet = b;
            s += super.shotsPerSec();
        }
        return s;
    }

    public float range(){
        float r = 0;
        for (BulletType b : bulletTypes) {
            if (b.range > r) r = b.range;
        }
        return r;
    }

    public void add(BulletType b){
        bulletTypes.add(b);
        bulletTypes.sort(Comparator.comparing((a) -> a.range));
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
        float x = mount.target.x();
        float y = mount.target.y();
        float d = Mathf.dst(x, y, shootX, shootY);
        BulletType b;

        for (int i = 0; i < bulletTypes.size; i++) {
            b = bulletTypes.get(i);
            if (b.range > d) {
                bullet = b;
                break;
            }
        }
        super.shoot(unit, mount, shootX, shootY, rotation);
    }
}
