package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.math.geom.Rect;
import mindustry.annotations.Annotations;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Legsc;
import mindustry.gen.Mechc;
import mindustry.gen.UnitEntity;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;


public class SUnits {
    public static @Annotations.EntityDef({Unitc.class, VoidStriderc.class, Legsc.class}) UnitType voidStrider;

    public static UnitType spark, singe, sear, sun;

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
            rotateSpeed = 3.5f;
            health = 6800f;
            armor = 8f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("singe-weapon"){{
                layerOffset = 0.0001f;
                reload = 50f;
                shootY = 4.5f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 2.2f;
                mirror = false;
                x = 0f;
                y = -0.75f;
                heatColor = Color.valueOf("f9350f");
                cooldownTime = 30f;
                bullet = new BasicBulletType(){{
                    shootOnDeath = true;
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

        voidStrider = new UnitType("void-strider"){{
            hitSize = 32f;
            speed = 8f;
            rotateSpeed = .5f;
            health = 8000f;
            armor = 80f;
        }};
    }
}
