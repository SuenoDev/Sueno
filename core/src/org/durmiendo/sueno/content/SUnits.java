package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.struct.Seq;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Bullet;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;


public class SUnits {

    public static UnitType spark,singe,sear,sun;
    public static void load() {
        spark = new TankUnitType("spark"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 3.5f;
            health = 1800f;
            armor = 3f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 1f;
                capacity = 120f;
                minSafeTemperature = 70f;
                damageRange = 7f;
                dpsOverTemperature = 0.2f;
                damage = 20f;
                regeneration = 20f;
            }}));
        }};

        singe = new TankUnitType("singe"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 8f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("singe-weapon"){{
                layerOffset = 0.0001f;
                reload = 5f;
                shootY = 4.5f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 6f;
                mirror = false;
                x = 0f;
                y = -0.75f;

                heatColor = Color.valueOf("f9350f");
                cooldownTime = 30f;
                bullet = new BasicBulletType(){{
                    speed=5f;
                    damage=77f;
                    fragBullets=4;
                    fragAngle=8f;
                    lifetime = 100f;
                    fragBullet = new BasicBulletType(){{
                       damage=22f;
                       speed=1.4f;
                       drawSize=3f;
                    }};
                }};
            }});

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 1.5f;
                capacity = 130f;
                overArmor = 4f;
                minSafeTemperature = 100f;
                overRegeneration = 120f;
            }}));
        }};

        sear = new TankUnitType("sear"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 8f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;
            weapons = new Seq<Weapon>().add(new Weapon("sear-gatling"){{
                name = "sear-gatling";
                reload = 60f;
                shootY = 4.5f;
                recoil = 3f;
                rotate = true;
                rotateSpeed = 6f;
                mirror = false;
                x = 0f;
                y = -1.25f;
                shoot = new ShootAlternate(){
                    {
                    shotDelay = 9f;
                    shots = 7;
                    bullet =new BasicBulletType(){{
                        speed = 4f;
                        damage = 77f;
                        lifetime = 100f;
                    }
                        @Override
                        public void removed(Bullet b) {
                            super.removed(b);
                            SVars.tempTemperatureController.at(Mathf.round(b.x), Mathf.round(b.y), 11110.1f);
                        }
                    };
                }};
                inaccuracy = 7f;
            }});
            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 2.5f;
                capacity = 150f;
            }}));
        }};

        sun = new TankUnitType("sun"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 8f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;
        }};
    }
}