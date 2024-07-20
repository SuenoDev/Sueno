package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Align;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.ui.Fonts;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.utils.SStrings;

public class MachineGunTurret extends ItemTurret {
    public float minReload = 6f;
    private float reloadBuff = 4.2f;
    private float reloadDEBuff = 1.2f / 60f;

    public MachineGunTurret(String name) {
        super(name);
    }

    public class MachineGunTurretBuild extends ItemTurretBuild {
        public float a = reload;

        protected void updateShooting(){
            if(reloadCounter >= a && !charging() && shootWarmup >= minWarmup){
                BulletType type = peekAmmo();

                shoot(type);

                reloadCounter %= a;
                a -= reloadBuff;
                a = Math.max(a, minReload);
            }
        }

        @Override
        public void update() {
            super.update();
            if (!isShooting()) {
                if (SVars.temperatureController.at(tileX(), tileY()) < 0.5f) a += reloadDEBuff * Time.delta;
                else a += reloadDEBuff * Time.delta / 2f;
                a = Math.min(a, reload);
            }
        }

        @Override
        public float range() {
            return 34.72f * 8f;
        }

        public void draw() {
            super.draw();
            Fonts.def.draw(SStrings.fixed(a, 2), x, y + 32, Color.red, 0.4f, true, Align.left);
        }

        protected void handleBullet(@Nullable Bullet bullet, float offsetX, float offsetY, float angleOffset){
            super.handleBullet(bullet, offsetX, offsetY, angleOffset);
            float r = 45f / a;
            bullet.lifetime += Mathf.random(r, r * 0.9f);
            bullet.lifetime *= Mathf.random(0.95f, 1.1f);
        }

        @Override
        public void readBase(Reads read) {
            super.readBase(read);
            a = read.f();
        }

        @Override
        public void writeBase(Writes write) {
            super.writeBase(write);
            write.f(a);
        }
    }
}
