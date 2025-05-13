package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.bullet.BulletType;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import org.durmiendo.sueno.core.SVars;

public class SItemTurret extends ItemTurret {
    public int shootMax = 14;
    public int shootMin = 7;

    public SItemTurret(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
        shoot.shots += 2;
    }

    public class ItemTurretBuild extends ItemTurret.ItemTurretBuild {
        public float getRandomShoot() {
            float r = Mathf.random(shootMin, shootMax);
            if (SVars.temperatureController.getRelativeTemperatureAt(Mathf.round(x), Mathf.round(y)) < 0.3f) {
                float rc = Mathf.random(shootMin, shootMax);
                if (r > rc) r = rc;
            }
            return r+1;
        }
        @Override
        protected void shoot(BulletType type) {
            float
                    bulletX = x + Angles.trnsx(rotation - 90, shootX, shootY),
                    bulletY = y + Angles.trnsy(rotation - 90, shootX, shootY);

            if (shoot.firstShotDelay > 0) {
                chargeSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
                type.chargeEffect.at(bulletX, bulletY, rotation);
            }

            float r = getRandomShoot();

            shoot.shoot(barrelCounter, (xOffset, yOffset, angle, delay, mover) -> {
                queuedBullets++;
                int barrel = barrelCounter;

                if (delay > 0f) {
                    Time.run(delay, () -> {
                        //hack: make sure the barrel is the same as what it was when the bullet was queued to fire
                        int prev = barrelCounter;
                        barrelCounter = barrel;
                        bullet(type, xOffset, yOffset, angle, mover, r);
                        barrelCounter = prev;
                    });
                } else {
                    bullet(type, xOffset, yOffset, angle, mover, r);
                }
            }, () -> barrelCounter++);

            if (consumeAmmoOnce) {
                useAmmo();
            }
        }

        protected void bullet(BulletType type, float xOffset, float yOffset, float angleOffset, Mover mover, float r) {
            queuedBullets--;

            if (dead || (!consumeAmmoOnce && !hasAmmo()) || r <= queuedBullets || queuedBullets == 0) return;

            byte side = 1;
            if (queuedBullets % 2 == 1) side = -1;
            rotation -= 25 * side;

            float
                    xSpread = Mathf.range(xRand),
                    bulletX = x + Angles.trnsx(rotation - 90, shootX + xOffset + xSpread, shootY + yOffset),
                    bulletY = y + Angles.trnsy(rotation - 90, shootX + xOffset + xSpread, shootY + yOffset),
                    shootAngle = rotation + Mathf.range(inaccuracy + type.inaccuracy);

            float lifeScl = type.scaleLife ? Mathf.clamp(Mathf.dst(bulletX, bulletY, targetPos.x, targetPos.y) / type.range, minRange / type.range, range() / type.range) : 1f;

            //TODO aimX / aimY for multi shot turrets?
            handleBullet(type.create(this, team, bulletX, bulletY, shootAngle, -1f, (1f - velocityRnd) + Mathf.random(velocityRnd), lifeScl, null, mover, targetPos.x, targetPos.y), xOffset, yOffset, shootAngle - rotation);

            (shootEffect == null ? type.shootEffect : shootEffect).at(bulletX, bulletY, rotation + angleOffset, type.hitColor);
            (smokeEffect == null ? type.smokeEffect : smokeEffect).at(bulletX, bulletY, rotation + angleOffset, type.hitColor);
            shootSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));

            ammoUseEffect.at(
                    x - Angles.trnsx(rotation, ammoEjectBack),
                    y - Angles.trnsy(rotation, ammoEjectBack),
                    rotation * Mathf.sign(xOffset)
            );

            if (shake > 0) {
                Effect.shake(shake, shake, this);
            }

            curRecoil = 1f;
            if (recoils > 0) {
                curRecoils[barrelCounter % recoils] = 1f;
            }
            heat = 1f;
            totalShots++;

            if (!consumeAmmoOnce) {
                useAmmo();
            }
        }
    }
}
